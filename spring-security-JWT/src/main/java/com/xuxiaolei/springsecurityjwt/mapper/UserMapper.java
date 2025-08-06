package com.xuxiaolei.springsecurityjwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuxiaolei.springsecurityjwt.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: xuxiaolei
 * @Description: TODO: UserMapper
 * @CreatTime: 2025/08/04 14:57
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
