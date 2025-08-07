package com.xuxiaolei.springsecurityjwt.handler;

import com.alibaba.fastjson.JSON;
import com.xuxiaolei.springsecurityjwt.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description:  认证失败处理器，主要用于处理未登录或认证失败时的响应，返回统一的 JSON错误消息
 * @CreatTime: 2025/08/01 15:45
 **/
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
//        String localizedMessage = authException.getLocalizedMessage();
        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNAUTHORIZED.value());
        result.put("message", "用户认证失败，请重新登录！" + authException.getMessage());
        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
