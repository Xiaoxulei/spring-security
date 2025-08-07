package com.xuxiaolei.springsecurityjwt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuxiaolei.springsecurityjwt.entity.User;
import com.xuxiaolei.springsecurityjwt.mapper.MenuMapper;
import com.xuxiaolei.springsecurityjwt.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SpringSecurityJwtApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MenuMapper menuMapper;
    @Test
    void contextLoads() {

        String encode = passwordEncoder.encode("123456");

        System.out.println("encode = " + encode);

    }

    @Test
    void insert() {
        User user = new User();

//        user.setUsername("xuxiaolei");
        user.setPassword(passwordEncoder.encode("xuxiaolei"));
        user.setEmail("xuxiaolei@qq.com");
       /* LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, 1);*/
        userMapper.update(user,new LambdaQueryWrapper<User>().eq(User::getId,1));

    }

    @Test
    void select() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, 1);
        User user = userMapper.selectOne(queryWrapper);
        userMapper.updateById(user);
        System.out.println(user);
    }

    @Test
    void selectById() {
        System.out.println(menuMapper.selectPermsByUserId(1L));
    }

}
