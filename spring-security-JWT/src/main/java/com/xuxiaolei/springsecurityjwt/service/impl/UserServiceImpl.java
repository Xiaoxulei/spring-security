package com.xuxiaolei.springsecurityjwt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuxiaolei.springsecurityjwt.common.Result;
import com.xuxiaolei.springsecurityjwt.dto.LoginRequest;
import com.xuxiaolei.springsecurityjwt.dto.UserDTO;
import com.xuxiaolei.springsecurityjwt.entity.LoginUser;
import com.xuxiaolei.springsecurityjwt.entity.User;
import com.xuxiaolei.springsecurityjwt.mapper.UserMapper;
import com.xuxiaolei.springsecurityjwt.service.UserService;
import com.xuxiaolei.springsecurityjwt.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

import static com.xuxiaolei.springsecurityjwt.utils.RedisConstants.LOGIN_USER_KEY_PREFIX;

/**
 * @Author: xuxiaolei
 * @Description: TODO:
 * @CreatTime: 2025/08/04 12:52
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<?> login(LoginRequest loginRequest) {
        try {
            // 1. 登录认证（会调用 loadUserByUsername）
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2. 登录成功，获取用户信息
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String userId = loginUser.getUser().getId().toString();

            // 3. 生成 JWT
            String token = jwtUtil.generateToken(userId);

            /*UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(loginUser.getUser(), userDTO);//将User转为UserDTO*/
            // 4. 用户信息保存到 Redis
            redisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + userId, loginUser, Duration.ofMinutes(10));

            // 5. 返回 token 和 user
            return Result.success(Map.of(
                    "token", token,
                    "id", userId,
                    "email",loginUser.getUser().getEmail()
            ));

        } catch (Exception e) {
            return Result.fail("登录失败：" + e.getMessage());
        }
    }

    @Override
    public Result<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            String userId = loginUser.getUser().getId().toString();
            //JwtAuthenticationFilter中已经判断redis中是否有用户信息，如果没有是不会执行
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            /*boolean hasKey = redisTemplate.hasKey(LOGIN_USER_KEY_PREFIX + userId);
            if(Boolean.FALSE.equals(hasKey)){
                return Result.error("注销失败，登录信息已失效");
            }*/
            redisTemplate.delete(LOGIN_USER_KEY_PREFIX + userId); // 删除 Redis 中的登录信息
            return Result.result("注销成功");
        }
        return Result.error("用户未登录或已注销");
    }
}

