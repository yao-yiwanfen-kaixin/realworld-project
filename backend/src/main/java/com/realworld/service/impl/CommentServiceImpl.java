package com.realworld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.realworld.entity.Article;
import com.realworld.entity.Comment;
import com.realworld.entity.User;
import com.realworld.mapper.ArticleMapper;
import com.realworld.mapper.CommentMapper;
import com.realworld.mapper.UserMapper;
import com.realworld.service.CommentService;
import com.realworld.vo.AuthorVO;
import com.realworld.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public CommentVO addComment(String userEmail, String slug, String body) {
        User user = getUserByEmail(userEmail);
        Article article = getArticleBySlug(slug);

        Comment comment = new Comment();
        comment.setBody(body);
        comment.setArticleId(article.getId());
        comment.setAuthorId(user.getId());

        commentMapper.insert(comment);

        return convertToVO(comment, user);
    }

    @Override
    public List<CommentVO> getComments(String slug) {
        Article article = getArticleBySlug(slug);

        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, article.getId())
                .orderByDesc(Comment::getCreatedAt);

        List<Comment> comments = commentMapper.selectList(wrapper);

        return comments.stream()
                .map(comment -> {
                    User author = userMapper.selectById(comment.getAuthorId());
                    return convertToVO(comment, author);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(String userEmail, String slug, Long id) {
        User user = getUserByEmail(userEmail);
        Comment comment = commentMapper.selectById(id);

        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (!comment.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("无权限删除此评论");
        }

        commentMapper.deleteById(id);
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

    private CommentVO convertToVO(Comment comment, User author) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setBody(comment.getBody());
        vo.setCreatedAt(comment.getCreatedAt());
        vo.setUpdatedAt(comment.getUpdatedAt());

        AuthorVO authorVO = new AuthorVO();
        authorVO.setUsername(author.getUsername());
        authorVO.setBio(author.getBio());
        authorVO.setImage(author.getImage());
        authorVO.setFollowing(false);
        vo.setAuthor(authorVO);

        return vo;
    }
}