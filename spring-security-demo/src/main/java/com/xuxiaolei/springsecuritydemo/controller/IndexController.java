package com.xuxiaolei.springsecuritydemo.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xuxiaolei
 * @Description: TODO: IndexController
 * @CreatTime: 2025/07/31 15:49
 **/

@RestController
public class IndexController {

    @GetMapping("/")
    public Map<Object, Object> index(){

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        String name = authentication.getName();
        objectObjectHashMap.put("username", name);
        objectObjectHashMap.put("authorities", authorities);
        objectObjectHashMap.put("credentials", credentials);

        return  objectObjectHashMap;
    }
}
