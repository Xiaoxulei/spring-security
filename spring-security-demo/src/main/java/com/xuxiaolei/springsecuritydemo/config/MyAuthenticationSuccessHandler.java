package com.xuxiaolei.springsecuritydemo.config;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description: TODO: MyAuthenticationSuccessHandler
 * @CreatTime: 2025/08/01 15:11
 **/
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();//获取用户信息
        Object credentials = authentication.getCredentials();//获取用户评证信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//用户凭证信息

        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", principal);
        result.put("authorities", authorities);
        result.put("credentials", credentials);

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
