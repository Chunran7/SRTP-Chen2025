package com.chun.back.controller;

import com.chun.back.pojo.Admin;
import com.chun.back.pojo.Article;
import com.chun.back.pojo.Introduction;
import com.chun.back.pojo.Result;
import com.chun.back.service.AdminService;
import com.chun.back.service.ArticleService;
import com.chun.back.service.IntroductionService;
import com.chun.back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private IntroductionService introductionService;

    /**
     * 获取统计数据（用户总数、文章总数）
     */
    @GetMapping("/stats")
    public Result getStats() {
        try {
            // 统计管理员总数
            Long userCount = adminService.countAdmins();
            
            // 统计文章总数（只统计未删除的）
            Long articleCount = articleService.countArticles("", 0);
            
            Map<String, Object> data = new HashMap<>();
            data.put("userCount", userCount);
            data.put("articleCount", articleCount);
            
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 管理员登录接口
     * @param body 包含 username 和 password 的请求体
     * @return 登录结果，成功返回 token，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        // 1. 获取请求参数
        String username = body.get("username");
        String password = body.get("password");
        
        // 2. 校验输入格式
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return Result.error("密码不能为空");
        }
        
        // 3. 查询管理员是否存在
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            return Result.error("用户不存在");
        }
        
        // 4. 验证密码
        Admin loginAdmin = adminService.login(username, password);
        if (loginAdmin == null) {
            return Result.error("密码错误");
        }
        
        // 5. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginAdmin.getId());
        claims.put("username", loginAdmin.getUsername());
        String token = JwtUtil.genToken(claims);
        
        // 6. 返回登录成功及 token
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("adminInfo", loginAdmin);
        
        return Result.success(data);
    }

    /**
     * 管理员获取文章列表（支持分页、搜索、包含已删除）
     */
    @GetMapping("/article/list")
    public Result getArticleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer includeDeleted) {
        
        List<Article> articles = articleService.getArticleListByPage(page, pageSize, keyword, includeDeleted);
        Long total = articleService.countArticles(keyword, includeDeleted);
        
        Map<String, Object> response = new HashMap<>();
        response.put("list", articles);
        response.put("total", total);
        
        return Result.success(response);
    }

    /**
     * 管理员根据 ID 获取文章详情
     */
    @GetMapping("/article/{id}")
    public Result getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            return Result.success(article);
        } else {
            return Result.error("未找到文章");
        }
    }

    /**
     * 管理员软删除文章
     */
    @DeleteMapping("/article/{id}/soft")
    public Result softDeleteArticle(@PathVariable Long id) {
        int rows = articleService.softDeleteArticle(id);
        if (rows > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 管理员恢复已删除的文章
     */
    @PutMapping("/article/{id}/restore")
    public Result restoreArticle(@PathVariable Long id) {
        int rows = articleService.restoreArticle(id);
        if (rows > 0) {
            return Result.success("恢复成功");
        } else {
            return Result.error("恢复失败");
        }
    }

    /**
     * 管理员彻底删除文章（硬删除）
     */
    @DeleteMapping("/article/{id}/hard")
    public Result hardDeleteArticle(@PathVariable Long id) {
        int rows = articleService.hardDeleteArticle(id);
        if (rows > 0) {
            return Result.success("彻底删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 获取介绍信息
     */
    @GetMapping("/introduction")
    public Result getIntroduction() {
        Introduction introduction = introductionService.getIntroduction();
        if (introduction != null) {
            return Result.success(introduction);
        } else {
            return Result.error("未找到介绍信息");
        }
    }

    /**
     * 更新介绍信息
     */
    @PutMapping("/introduction")
    public Result updateIntroduction(@RequestBody Map<String, String> body) {
        try {
            Introduction introduction = new Introduction();
            introduction.setTitle(body.get("title"));
            introduction.setContent(body.get("content"));
            introduction.setImageUrl(body.get("imageUrl"));
            
            int rows = introductionService.updateIntroduction(introduction);
            if (rows > 0) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 管理员更新文章
     */
    @PutMapping("/article/{id}")
    public Result updateArticle(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Article article = new Article();
            article.setId(id);
            
            // 从 Map 中获取各个字段值，允许为 null
            if (body.containsKey("title")) {
                article.setTitle((String) body.get("title"));
            }
            if (body.containsKey("firstPicture")) {
                article.setFirstPicture((String) body.get("firstPicture"));
            }
            if (body.containsKey("description")) {
                article.setDescription((String) body.get("description"));
            }
            if (body.containsKey("content")) {
                article.setContent((String) body.get("content"));
            }
            if (body.containsKey("author")) {
                article.setAuthor((String) body.get("author"));
            }
            
            // 执行更新，不检查返回值，直接认为成功
            articleService.updateArticle(article);
            
            return Result.success("修改成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败：" + e.getMessage());
        }
    }
}
