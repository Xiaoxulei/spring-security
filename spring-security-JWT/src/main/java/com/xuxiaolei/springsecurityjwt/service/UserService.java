package com.xuxiaolei.springsecurityjwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuxiaolei.springsecurityjwt.common.Result;
import com.xuxiaolei.springsecurityjwt.dto.LoginRequest;
import com.xuxiaolei.springsecurityjwt.entity.User;
import com.xuxiaolei.springsecurityjwt.vo.LoginResponse;


/**
* @Author: xuxiaolei
* @Description: TODO: UserService
* @CreatTime: 2025/08/04 12:50
**/
public interface UserService extends IService<User> {
    Result<?> login(LoginRequest loginRequest);

    Result<?> logout();
}
