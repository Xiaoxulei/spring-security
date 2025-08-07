package com.xuxiaolei.springsecurityjwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: xuxiaolei
 * @Description: TODO: LoginUser
 * @CreatTime: 2025/08/05 14:23
 **/
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails {

    private User user;

    private List<String> permissions; // 或 roles

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @JsonIgnore
    private  List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

   /* // 账户是否没过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否没锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 凭证是否没过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否启用
    @Override
    public boolean isEnabled() {
        return true;
    }*/
}
