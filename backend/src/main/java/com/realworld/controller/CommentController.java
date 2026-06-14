package com.realworld.controller;

import com.realworld.service.CommentService;
import com.realworld.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Map<String, CommentVO>> addComment(@PathVariable String slug, @RequestBody Map<String, Map<String, String>> request) {
        String userEmail = getCurrentUserEmail();
        String body = request.get("comment").get("body");
        CommentVO commentVO = commentService.addComment(userEmail, slug, body);
        Map<String, CommentVO> response = new HashMap<>();
        response.put("comment", commentVO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<CommentVO>>> getComments(@PathVariable String slug) {
        List<CommentVO> comments = commentService.getComments(slug);
        Map<String, List<CommentVO>> response = new HashMap<>();
        response.put("comments", comments);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String slug, @PathVariable Long id) {
        String userEmail = getCurrentUserEmail();
        commentService.deleteComment(userEmail, slug, id);
        return ResponseEntity.ok().build();
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        return (String) authentication.getPrincipal();
    }
}