package com.chun.back.controller;

import java.util.List;

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
        // 如果文章不存在或已被删除，返回错误信息
        if (article == null || article.getIsDeleted() == 1) {
            return Result.error("文章不存在或已被删除");
        } else {
            return Result.success(article);
        }
    }

    @PostMapping("")
    public Result createArticle(@RequestBody Article article, HttpServletRequest request) {
        // 鉴权由拦截器统一处理，这里只执行业务逻辑
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
        // 鉴权由拦截器统一处理，这里只执行业务逻辑
        int rows = articleService.deleteArticle(id);
        if (rows > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
