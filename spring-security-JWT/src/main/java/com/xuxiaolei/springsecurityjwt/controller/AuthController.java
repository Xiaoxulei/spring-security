package com.xuxiaolei.springsecurityjwt.controller;

import com.xuxiaolei.springsecurityjwt.dto.LoginDto;
import com.xuxiaolei.springsecurityjwt.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> result = new HashMap<>();

        // 👇这里你可以换成数据库验证
        if ("admin".equals(loginDto.getUsername()) && "123456".equals(loginDto.getPassword())) {
            String token = jwtUtil.generateToken(loginDto.getUsername());
            result.put("code", 200);
            result.put("token", token);
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
        }

        return result;
    }
}
