package com.xuxiaolei.springsecuritydemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: xuxiaolei
 * @Description: TODO: User
 * @CreatTime: 2025/07/31 17:05
 **/
@Data
public class User {
    @TableId(value = "id",type = IdType.AUTO)//主键自增

    private Integer id;

    private String username;

    private String password;

    private boolean enabled;
}
