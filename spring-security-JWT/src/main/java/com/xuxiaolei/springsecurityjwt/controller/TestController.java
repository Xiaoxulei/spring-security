package com.xuxiaolei.springsecurityjwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    //这里的 authentication是 Spring Security 自动注入的参数
    @PreAuthorize("@permission.hasPermission('user:list')")
//    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/hello")
    public String hello() {
        return "你好，小徐磊！你已通过JWT认证！";
    }
}
