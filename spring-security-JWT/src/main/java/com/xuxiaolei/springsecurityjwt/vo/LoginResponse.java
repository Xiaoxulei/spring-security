package com.xuxiaolei.springsecurityjwt.vo;

import com.xuxiaolei.springsecurityjwt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
//登录响应体结构
@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private User user;
}
