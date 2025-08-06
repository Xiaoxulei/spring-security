package com.xuxiaolei.springsecurityjwt.filter;

import com.xuxiaolei.springsecurityjwt.entity.LoginUser;
import com.xuxiaolei.springsecurityjwt.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.xuxiaolei.springsecurityjwt.utils.RedisConstants.LOGIN_USER_KEY_PREFIX;

// 标记为 Spring 组件，让 Spring 扫描并注册到容器中
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        try {
            // 1. 获取请求头中的 Authorization
            String token = request.getHeader("Authorization");

            // 2. 检查 token 是否存在且格式正确
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                // 3. 去掉前缀，获取实际 token
                token = token.substring(7);

                // 4. 从 token 中解析出用户Id（或用户名）
                String userId = jwtUtil.extractUserId(token);
                if (!Objects.isNull(userId)) {
                    // 5. 查询 Redis 中的用户信息
                    String redisKey = LOGIN_USER_KEY_PREFIX + userId;
                    Object cachedUser = redisTemplate.opsForValue().get(redisKey);
                    System.out.println("反序列化类型：" + (cachedUser == null ? "null" : cachedUser.getClass().getName()));
                    if (cachedUser instanceof LoginUser loginUser) {
                        // 6. 构造 Authentication 并注入上下文
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        loginUser,
                                        null,
                                        null
                                );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Redis 中没有用户信息（可能未登录或已被登出）
                        // 此处可记录日志，也可不处理，走匿名流程
                        log.info("用户未登录或登录信息已过期：" + userId);
                    }
                }
            }
        } catch (Exception e) {
            // JWT 解析失败或 Redis 查询异常，记录日志但不抛出异常
            System.err.println("JWT 校验失败: " + e.getMessage());
            // 也可以选择响应 401，但通常交给全局异常或权限拦截处理器统一处理
        }

        // 继续处理后续过滤器链（不管有没有登录）
        filterChain.doFilter(request, response);
    }
}


