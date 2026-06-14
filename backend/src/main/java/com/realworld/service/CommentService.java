package com.realworld.service;

import com.realworld.vo.CommentVO;
import java.util.List;

public interface CommentService {
    CommentVO addComment(String userEmail, String slug, String body);
    List<CommentVO> getComments(String slug);
    void deleteComment(String userEmail, String slug, Long id);
}