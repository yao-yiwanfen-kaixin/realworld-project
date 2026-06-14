package com.realworld.vo;

import lombok.Data;

@Data
public class AuthorVO {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}