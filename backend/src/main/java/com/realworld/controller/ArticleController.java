package com.realworld.controller;

import com.realworld.dto.ArticleRequest;
import com.realworld.service.ArticleService;
import com.realworld.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Map<String, ArticleVO>> createArticle(@RequestBody Map<String, ArticleRequest> request) {
        String userEmail = getCurrentUserEmail();
        ArticleVO articleVO = articleService.createArticle(userEmail, request.get("article"));
        Map<String, ArticleVO> response = new HashMap<>();
        response.put("article", articleVO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Map<String, ArticleVO>> getArticle(@PathVariable String slug) {
        ArticleVO articleVO = articleService.getArticle(slug);
        Map<String, ArticleVO> response = new HashMap<>();
        response.put("article", articleVO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Map<String, ArticleVO>> updateArticle(@PathVariable String slug, @RequestBody Map<String, ArticleRequest> request) {
        String userEmail = getCurrentUserEmail();
        ArticleVO articleVO = articleService.updateArticle(userEmail, slug, request.get("article"));
        Map<String, ArticleVO> response = new HashMap<>();
        response.put("article", articleVO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String slug) {
        String userEmail = getCurrentUserEmail();
        articleService.deleteArticle(userEmail, slug);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {

        String userEmail = null;
        try {
            userEmail = getCurrentUserEmail();
        } catch (Exception e) {
            // 未登录用户
        }

        List<ArticleVO> articles = articleService.listArticles(userEmail, tag, author, favorited, limit, offset);
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("articlesCount", articles.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<Map<String, ArticleVO>> favoriteArticle(@PathVariable String slug) {
        String userEmail = getCurrentUserEmail();
        ArticleVO articleVO = articleService.favoriteArticle(userEmail, slug);
        Map<String, ArticleVO> response = new HashMap<>();
        response.put("article", articleVO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<Map<String, ArticleVO>> unfavoriteArticle(@PathVariable String slug) {
        String userEmail = getCurrentUserEmail();
        ArticleVO articleVO = articleService.unfavoriteArticle(userEmail, slug);
        Map<String, ArticleVO> response = new HashMap<>();
        response.put("article", articleVO);
        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        return (String) authentication.getPrincipal();
    }
}