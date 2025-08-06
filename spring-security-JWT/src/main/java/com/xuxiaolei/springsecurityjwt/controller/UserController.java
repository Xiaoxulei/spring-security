package com.xuxiaolei.springsecurityjwt.controller;

import com.xuxiaolei.springsecurityjwt.common.Result;
import com.xuxiaolei.springsecurityjwt.dto.LoginRequest;
import com.xuxiaolei.springsecurityjwt.entity.User;
import com.xuxiaolei.springsecurityjwt.service.UserService;
import com.xuxiaolei.springsecurityjwt.vo.LoginResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuxiaolei
 * @Description: TODO: 用户认证接口
 * @CreatTime: 2025/08/04 12:42
 **/
@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return userService.logout();
    }

}
