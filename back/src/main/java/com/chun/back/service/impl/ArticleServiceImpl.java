package com.chun.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chun.back.mapper.ArticleMapper;
import com.chun.back.pojo.Article;
import com.chun.back.service.ArticleService;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    public Article getArticleById(Long id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public List<Article> getArticleList() {
        return articleMapper.getArticleList();
    }

    @Override
    public List<Article> getLatestArticleList(Integer count) {
        return articleMapper.getLatestArticleList(count);
    }

    @Override
    public int insertArticle(Article article) {
        return articleMapper.insertArticle(article);
    }

    @Override
    public int deleteArticle(Long id) {
        return articleMapper.deleteArticle(id);
    }

    @Override
    public int softDeleteArticle(Long id) {
        return articleMapper.softDeleteArticle(id);
    }

    @Override
    public int restoreArticle(Long id) {
        return articleMapper.restoreArticle(id);
    }

    @Override
    public int hardDeleteArticle(Long id) {
        return articleMapper.hardDeleteArticle(id);
    }

    @Override
    public List<Article> getArticleListByPage(Integer page, Integer pageSize, String keyword, Integer includeDeleted) {
        int offset = (page - 1) * pageSize;
        return articleMapper.getArticleListByPage(page, pageSize, keyword, includeDeleted, offset);
    }

    @Override
    public Long countArticles(String keyword, Integer includeDeleted) {
        return articleMapper.countArticles(keyword, includeDeleted);
    }

    @Override
    public int updateArticle(Article article) {
        return articleMapper.updateArticle(article);
    }

}
