package com.chun.back.service;

import com.chun.back.pojo.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(Long id);

    List<Article> getArticleList();

    List<Article> getLatestArticleList(Integer count);

    // 插入文章
    int insertArticle(Article article);
    
    // 删除文章
    int deleteArticle(Long id);

    // 软删除文章
    int softDeleteArticle(Long id);

    // 恢复已删除的文章
    int restoreArticle(Long id);

    // 彻底删除文章
    int hardDeleteArticle(Long id);

    // 分页获取文章列表
    List<Article> getArticleListByPage(Integer page, Integer pageSize, String keyword, Integer includeDeleted);

    // 统计文章总数
    Long countArticles(String keyword, Integer includeDeleted);
}
