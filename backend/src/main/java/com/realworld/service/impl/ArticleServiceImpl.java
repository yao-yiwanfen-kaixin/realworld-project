package com.realworld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realworld.dto.ArticleRequest;
import com.realworld.entity.Article;
import com.realworld.entity.ArticleFavorite;
import com.realworld.entity.User;
import com.realworld.mapper.ArticleFavoriteMapper;
import com.realworld.mapper.ArticleMapper;
import com.realworld.mapper.UserMapper;
import com.realworld.service.ArticleService;
import com.realworld.vo.ArticleVO;
import com.realworld.vo.AuthorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleFavoriteMapper articleFavoriteMapper;

    @Override
    public ArticleVO createArticle(String userEmail, ArticleRequest request) {
        User author = getUserByEmail(userEmail);
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setDescription(request.getDescription());
        article.setBody(request.getBody());
        article.setAuthorId(author.getId());
        article.setSlug(generateSlug(request.getTitle()));
        articleMapper.insert(article);
        return convertToVO(article, author, null);
    }

    @Override
    public ArticleVO getArticle(String slug) {
        Article article = getArticleBySlug(slug);
        User author = userMapper.selectById(article.getAuthorId());
        return convertToVO(article, author, null);
    }

    @Override
    public ArticleVO updateArticle(String userEmail, String slug, ArticleRequest request) {
        User user = getUserByEmail(userEmail);
        Article article = getArticleBySlug(slug);
        if (!article.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("无权限修改此文章");
        }
        if (request.getTitle() != null) {
            article.setTitle(request.getTitle());
            article.setSlug(generateSlug(request.getTitle()));
        }
        if (request.getDescription() != null) {
            article.setDescription(request.getDescription());
        }
        if (request.getBody() != null) {
            article.setBody(request.getBody());
        }
        articleMapper.updateById(article);
        return convertToVO(article, user, null);
    }

    @Override
    public void deleteArticle(String userEmail, String slug) {
        User user = getUserByEmail(userEmail);
        Article article = getArticleBySlug(slug);
        if (!article.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("无权限删除此文章");
        }
        articleMapper.deleteById(article.getId());
    }

    @Override
    public List<ArticleVO> listArticles(String userEmail, String tag, String author, String favorited, Integer limit, Integer offset) {
        // 设置默认值，避免空指针和除零错误
        int pageLimit = (limit != null && limit > 0) ? limit : 20;
        int pageOffset = (offset != null && offset >= 0) ? offset : 0;
        
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getCreatedAt);
        
        // 计算页码：MyBatis Plus 页码从1开始
        int currentPage = pageOffset / pageLimit + 1;
        Page<Article> page = new Page<>(currentPage, pageLimit);
        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
        
        User currentUser = userEmail != null ? getUserByEmail(userEmail) : null;
        return articlePage.getRecords().stream()
                .map(article -> {
                    User articleAuthor = userMapper.selectById(article.getAuthorId());
                    return convertToVO(article, articleAuthor, currentUser);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArticleVO favoriteArticle(String userEmail, String slug) {
        User user = getUserByEmail(userEmail);
        Article article = getArticleBySlug(slug);

        LambdaQueryWrapper<ArticleFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleFavorite::getUserId, user.getId())
                .eq(ArticleFavorite::getArticleId, article.getId());

        if (articleFavoriteMapper.selectOne(wrapper) == null) {
            ArticleFavorite favorite = new ArticleFavorite();
            favorite.setUserId(user.getId());
            favorite.setArticleId(article.getId());
            articleFavoriteMapper.insert(favorite);
        }

        User author = userMapper.selectById(article.getAuthorId());
        return convertToVO(article, author, user);
    }

    @Override
    public ArticleVO unfavoriteArticle(String userEmail, String slug) {
        User user = getUserByEmail(userEmail);
        Article article = getArticleBySlug(slug);

        LambdaQueryWrapper<ArticleFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleFavorite::getUserId, user.getId())
                .eq(ArticleFavorite::getArticleId, article.getId());

        ArticleFavorite favorite = articleFavoriteMapper.selectOne(wrapper);
        if (favorite != null) {
            articleFavoriteMapper.deleteById(favorite.getId());
        }

        User author = userMapper.selectById(article.getAuthorId());
        return convertToVO(article, author, user);
    }

    private User getUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    private Article getArticleBySlug(String slug) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getSlug, slug);
        Article article = articleMapper.selectOne(wrapper);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        return article;
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-") + "-" + System.currentTimeMillis();
    }

    private ArticleVO convertToVO(Article article, User author, User currentUser) {
        ArticleVO vo = new ArticleVO();
        vo.setSlug(article.getSlug());
        vo.setTitle(article.getTitle());
        vo.setDescription(article.getDescription());
        vo.setBody(article.getBody());
        vo.setCreatedAt(article.getCreatedAt());
        vo.setUpdatedAt(article.getUpdatedAt());
        vo.setTagList(new ArrayList<>());

        AuthorVO authorVO = new AuthorVO();
        authorVO.setUsername(author.getUsername());
        authorVO.setBio(author.getBio());
        authorVO.setImage(author.getImage());
        authorVO.setFollowing(false);
        vo.setAuthor(authorVO);

        // 计算点赞数和是否点赞
        LambdaQueryWrapper<ArticleFavorite> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(ArticleFavorite::getArticleId, article.getId());
        int favoritesCount = articleFavoriteMapper.selectCount(countWrapper).intValue();
        vo.setFavoritesCount(favoritesCount);

        if (currentUser != null) {
            LambdaQueryWrapper<ArticleFavorite> favoriteWrapper = new LambdaQueryWrapper<>();
            favoriteWrapper.eq(ArticleFavorite::getUserId, currentUser.getId())
                    .eq(ArticleFavorite::getArticleId, article.getId());
            vo.setFavorited(articleFavoriteMapper.selectOne(favoriteWrapper) != null);
        } else {
            vo.setFavorited(false);
        }

        return vo;
    }
}