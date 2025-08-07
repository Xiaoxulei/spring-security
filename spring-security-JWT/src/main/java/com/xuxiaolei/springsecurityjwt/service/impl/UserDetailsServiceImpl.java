package com.xuxiaolei.springsecurityjwt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuxiaolei.springsecurityjwt.entity.LoginUser;
import com.xuxiaolei.springsecurityjwt.entity.User;
import com.xuxiaolei.springsecurityjwt.mapper.MenuMapper;
import com.xuxiaolei.springsecurityjwt.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private  UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
        //将User封装为UserDetails
        return new LoginUser(user,permissions);
    }
}
