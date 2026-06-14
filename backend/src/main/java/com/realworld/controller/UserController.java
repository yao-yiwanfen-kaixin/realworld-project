package com.realworld.controller;

import com.realworld.dto.LoginRequest;
import com.realworld.dto.RegisterRequest;
import com.realworld.service.UserService;
import com.realworld.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Map<String, UserVO>> register(@Validated @RequestBody Map<String, RegisterRequest> request) {
        UserVO userVO = userService.register(request.get("user"));
        Map<String, UserVO> response = new HashMap<>();
        response.put("user", userVO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Map<String, UserVO>> login(@Validated @RequestBody Map<String, LoginRequest> request) {
        UserVO userVO = userService.login(request.get("user"));
        Map<String, UserVO> response = new HashMap<>();
        response.put("user", userVO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, UserVO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        UserVO userVO = userService.getCurrentUser(email);
        Map<String, UserVO> response = new HashMap<>();
        response.put("user", userVO);
        return ResponseEntity.ok(response);
    }
}