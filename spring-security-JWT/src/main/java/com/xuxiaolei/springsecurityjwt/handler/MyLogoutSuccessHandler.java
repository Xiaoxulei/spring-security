package com.xuxiaolei.springsecurityjwt.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description: 是 Spring Security 里用来处理 用户退出（注销）成功后逻辑 的接口。
 * @CreatTime: 2025/08/01 15:37
 **/
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "注销成功!");

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
