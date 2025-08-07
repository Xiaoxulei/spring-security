package com.xuxiaolei.springsecurityjwt.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;

/**
* @Author: xuxiaolei
* @Description: 处理 权限不足（即用户已登录，但没有访问某资源的权限）时的处理器
* @CreatTime: 2025/08/07 16:44
**/public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", HttpStatus.FORBIDDEN.value());
        result.put("message", "权限不足");

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
