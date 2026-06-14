package com.realworld.service;

import com.realworld.dto.LoginRequest;
import com.realworld.dto.RegisterRequest;
import com.realworld.vo.UserVO;

public interface UserService {
    UserVO register(RegisterRequest request);
    UserVO login(LoginRequest request);
    UserVO getCurrentUser(String email);
}