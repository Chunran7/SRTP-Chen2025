package com.chun.back.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.chun.back.pojo.Article;
import com.chun.back.pojo.Result;
import com.chun.back.service.ArticleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // no token-based admin check; login handled separately

    @GetMapping("/{id}")
    public Result getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            return Result.success(article);
        } else {
            return Result.error("未找到文章");
        }
    }

    @PostMapping("")
    public Result createArticle(@RequestBody Article article, HttpServletRequest request) {
        // 从 request 中获取 claims 信息（由拦截器设置）
        Map<String, Object> claims = (Map<String, Object>) request.getAttribute("claims");

        // 验证是否为管理员登录（检查 claims 中是否包含管理员信息）
        if (claims == null || !claims.containsKey("username")) {
            return Result.error("未授权访问");
        }

        int rows = articleService.insertArticle(article);
        if (rows > 0) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @GetMapping("/list")
    public Result getArticleList() {
        List<Article> articles = articleService.getArticleList();
        if (articles.isEmpty()) {
            return Result.error("没有文章");
        } else {
            return Result.success(articles);
        }
    }

    @GetMapping("/latest")
    public Result getLatestArticleList(@RequestParam(defaultValue = "3") Integer count) {
        List<Article> articles = articleService.getLatestArticleList(count);
        if (articles.isEmpty()) {
            return Result.error("没有文章");
        } else {
            return Result.success(articles);
        }

    }
    
    @DeleteMapping("/{id}")
    public Result deleteArticle(@PathVariable Long id, HttpServletRequest request) {
        // 从 request 中获取 claims 信息（由拦截器设置）
        Map<String, Object> claims = (Map<String, Object>) request.getAttribute("claims");

        // 验证是否为管理员登录（检查 claims 中是否包含管理员信息）
        if (claims == null || !claims.containsKey("username")) {
            return Result.error("未授权访问");
        }

        int rows = articleService.deleteArticle(id);
        if (rows > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
