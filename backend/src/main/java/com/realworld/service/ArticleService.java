package com.realworld.service;

import com.realworld.dto.ArticleRequest;
import com.realworld.vo.ArticleVO;
import java.util.List;

public interface ArticleService {
    ArticleVO createArticle(String userEmail, ArticleRequest request);
    ArticleVO getArticle(String slug);
    ArticleVO updateArticle(String userEmail, String slug, ArticleRequest request);
    void deleteArticle(String userEmail, String slug);
    List<ArticleVO> listArticles(String userEmail, String tag, String author, String favorited, Integer limit, Integer offset);
    ArticleVO favoriteArticle(String userEmail, String slug);
    ArticleVO unfavoriteArticle(String userEmail, String slug);
}