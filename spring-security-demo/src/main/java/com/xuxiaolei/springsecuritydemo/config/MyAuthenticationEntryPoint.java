package com.xuxiaolei.springsecuritydemo.config;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description: TODO: MyAuthenticationEntryPoint
 * @CreatTime: 2025/08/01 15:45
 **/
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String localizedMessage = authException.getLocalizedMessage();
        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", -1);
        result.put("message", localizedMessage);

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
