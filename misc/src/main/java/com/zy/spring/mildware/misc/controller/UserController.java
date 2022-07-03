package com.zy.spring.mildware.misc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 分别模拟登录和登出
 */
@RestController
@RequestMapping("/user/")
public class UserController {
    public static final String USER_ATTRIBUTE_NAME = "user_session";
    @RequestMapping("login")
    public ResponseEntity<String> login(@RequestBody String user, HttpServletRequest request) {
        request.getSession().setAttribute(USER_ATTRIBUTE_NAME, user);
        return ResponseEntity.ok().body("successfully to login.\n sessionId is: " + request.getSession().getId());
    }
    @RequestMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        return ResponseEntity.ok().body("successfully to logout..\n sessionId is: " + request.getSession().getId());
    }
}
