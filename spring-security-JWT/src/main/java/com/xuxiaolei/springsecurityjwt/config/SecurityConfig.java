package com.xuxiaolei.springsecurityjwt.config;

import com.xuxiaolei.springsecurityjwt.filter.JwtAuthenticationFilter;
import com.xuxiaolei.springsecurityjwt.handler.MyAccessDeniedHandler;
import com.xuxiaolei.springsecurityjwt.handler.MyAuthenticationEntryPoint;
import com.xuxiaolei.springsecurityjwt.handler.MyAuthenticationFailureHandler;
import com.xuxiaolei.springsecurityjwt.handler.MyLogoutSuccessHandler;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor  //Lombok 自动注入 final 字段的，当然你也可以用 @Autowired。
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login","/api/logout").permitAll()
                        /*.requestMatchers("/api/hello").hasAuthority("user:list")*/
                        .anyRequest().authenticated()
                );
        //添加过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //登录
        http.formLogin(login->{
           login
                   .failureHandler(myAuthenticationFailureHandler);
        });
        //异常处理
        http.exceptionHandling(exception->{
           exception
                   .authenticationEntryPoint(new MyAuthenticationEntryPoint())//请求未认证的处理
                   .accessDeniedHandler(new MyAccessDeniedHandler()) //处理权限不足
            ;
        });
        //登出
         http.logout(logout->{
            logout
                    .logoutSuccessHandler(new MyLogoutSuccessHandler());//注销成功时的处理
        });
        return http.build();
    }

    // 必须暴露 authenticationManager Bean，否则你在 service 里无法注入
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

