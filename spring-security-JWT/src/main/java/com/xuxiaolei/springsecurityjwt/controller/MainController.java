package com.xuxiaolei.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuxiaolei
 * @Description: TODO:
 * @CreatTime: 2025/08/06 11:40
 **/
@RestController
@RequestMapping("/user")
public class MainController {

    @GetMapping("/hello")
    public String test(){

        return "hello";
    }
}
