package com.xuxiaolei.springsecurityjwt.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description: 登录认证失败时的处理器，用来定制当用户登录失败时的响应行为，比如返回统一的 JSON 提示、记录日志等
 * @CreatTime: 2025/08/01 15:30
 **/
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String localizedMessage = exception.getLocalizedMessage();
        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNAUTHORIZED.value());
        result.put("message", localizedMessage);

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
