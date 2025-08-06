package com.xuxiaolei.springsecuritydemo.config;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: xuxiaolei
 * @Description: TODO: MySessionInformationExpiredStrategy
 * @CreatTime: 2025/08/01 16:07
 **/
public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

        HashMap<Object, Object> result = new HashMap<>();
        result.put("code", -1);
        result.put("message", "该账号已从其他设备登录");

        //将结果对象转换为json字符串
        String json = JSON.toJSONString(result);
        HttpServletResponse response = event.getResponse();
        //返回json数据到前端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
