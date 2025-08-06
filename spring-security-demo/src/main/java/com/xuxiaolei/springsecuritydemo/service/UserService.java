package com.xuxiaolei.springsecuritydemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuxiaolei.springsecuritydemo.entity.User;

/**
* @Author: xuxiaolei
* @Description: TODO: UserService
* @CreatTime: 2025/07/31 17:09
**/
public interface UserService extends IService<User> {
    void createUser(User user);
}
