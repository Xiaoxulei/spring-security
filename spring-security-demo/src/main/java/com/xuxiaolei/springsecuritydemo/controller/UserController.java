package com.xuxiaolei.springsecuritydemo.controller;

import com.xuxiaolei.springsecuritydemo.entity.User;
import com.xuxiaolei.springsecuritydemo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuxiaolei
 * @Description: TODO: UserController
 * @CreatTime: 2025/07/31 17:16
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @PreAuthorize("hasAuthority('USER_LIST')")
    @GetMapping("/list")
    public List<User> getList() {
        return userService.list();
    }

    @PreAuthorize("hasRole('ADMIN') and authentication.name.equals('xuxiaolei') ")
    @PostMapping("/create")
    public void create(@RequestBody User user) {
        userService.createUser(user);
    }
}
