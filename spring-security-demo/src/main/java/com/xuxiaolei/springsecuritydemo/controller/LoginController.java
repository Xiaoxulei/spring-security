package com.xuxiaolei.springsecuritydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: xuxiaolei
 * @Description: TODO: LoginController
 * @CreatTime: 2025/08/01 14:38
 **/
@Controller
public class LoginController {

    @GetMapping("login")
    public String login(){

        return "login";
    }
}
