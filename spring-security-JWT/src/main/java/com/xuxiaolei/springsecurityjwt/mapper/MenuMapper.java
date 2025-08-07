package com.xuxiaolei.springsecurityjwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuxiaolei.springsecurityjwt.entity.Menu;

import java.util.List;

/**
 * @Author: xuxiaolei
 * @Description: TODO:
 * @CreatTime: 2025/08/07 15:52
 **/
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);
}
