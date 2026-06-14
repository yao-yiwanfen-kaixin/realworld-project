package com.realworld.vo;

import lombok.Data;

@Data
public class UserVO {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;
}