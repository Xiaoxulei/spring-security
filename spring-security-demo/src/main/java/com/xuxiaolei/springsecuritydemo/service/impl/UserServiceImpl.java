package com.xuxiaolei.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuxiaolei.springsecuritydemo.config.DBUserDetailsManager;
import com.xuxiaolei.springsecuritydemo.entity.User;
import com.xuxiaolei.springsecuritydemo.mapper.UserMapper;
import com.xuxiaolei.springsecuritydemo.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author: xuxiaolei
 * @Description: TODO: UserServiceImpl
 * @CreatTime: 2025/07/31 17:11
 **/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private DBUserDetailsManager dbUserDetailsManager;

    @Override
    public void createUser(User user) {
        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if(user1 == null){
            log.info("用户已经存在");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        dbUserDetailsManager.createUser(userDetails);
    }
}
