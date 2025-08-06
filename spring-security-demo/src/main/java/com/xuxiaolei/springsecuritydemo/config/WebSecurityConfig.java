package com.xuxiaolei.springsecuritydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @Author: xuxiaolei
 * @Description: TODO:  WebSecurityConfig
 * @CreatTime: 2025/07/31 16:15
 **/
@Configuration
@EnableWebSecurity //在springboot项目中可以省略
@EnableMethodSecurity
public class WebSecurityConfig {

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        return manager;
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //开启授权保护
                /*.csrf(csrf -> csrf.disable())*/
                //关闭csrf攻击防御
                http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        /*.requestMatchers("/user/list").hasAuthority("USER_LIST")  // 允许匿名访问/public/**路径
                        .requestMatchers("/user/create").hasAuthority("USER_CREATE")   // 允许匿名访问/public/**路径*/
                        /*.requestMatchers("/user/**").hasRole("ADMIN")*/
                        .anyRequest().authenticated()                // 其它请求都需要认证
                );
                /*.formLogin(withDefaults())  */ // 启用默认登录表单
                http.formLogin(form ->{
                    form.loginPage("/login").permitAll() //无需授权即可访问当前页面
                            .usernameParameter("myUsername") //配置自定义表单参数，默认值 username,password
                            .passwordParameter("myPassword")
                            .failureUrl("/login?failed")//校验失败时跳转的地址，默认值是error
                            .successHandler(new MyAuthenticationSuccessHandler())//认证成功时的处理
                            .failureHandler(new MyAuthenticationFailureHandler())//认证失败时的处理
                    ;
                });
                http.logout(logout ->{
                    logout.logoutSuccessHandler(new MyLogoutSuccessHandler());//注销成功时的处理
                });
                http.exceptionHandling(exception ->{
                   exception.authenticationEntryPoint(new MyAuthenticationEntryPoint())//请求未认证的处理
                            .accessDeniedHandler(new MyAccessDeniedHandler())
                   ;
                });
                http.sessionManagement(sessionManagement ->{
                   sessionManagement.maximumSessions(1)//最大登录数
                           .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
                });
                http.cors(withDefaults());//跨域
                //.httpBasic(withDefaults());  // 启用 HTTP Basic 认证
        return http.build();
    }

   /* @Bean
    public UserDetailsService userDetailsService() {
        //创建基于数据库的用户信息管理器
        return new DBUserDetailsManager();
    }*/

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

