package com.xuxiaolei.springsecurityjwt.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @Author: xuxiaolei
 * @Description: TODO:
 * @CreatTime: 2025/08/08 09:26
 **/
@Component("permission")
public class PermissionCheck {
    /**
     * 判断当前用户是否具有某个权限
     * @param authentication 当前认证对象
     * @param permission 要检查的权限值，例如 "sys:user:list"
     * @return true 表示有权限
     */
    //显式写 authentication 参数
    public boolean hasPermission(Authentication authentication, String permission) {
        if (authentication == null || permission == null) {
            return false;
        }
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    //隐式写 authentication 参数
    public  boolean hasPermission(String permission) {
        // 从 SecurityContextHolder 获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || permission == null) {
            return false;
        }
        // 遍历当前用户的权限集合
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
