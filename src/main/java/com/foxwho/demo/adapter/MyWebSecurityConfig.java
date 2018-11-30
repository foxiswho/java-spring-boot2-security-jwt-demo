package com.foxwho.demo.adapter;

import com.foxwho.demo.filter.MyJWTAuthenticationFilter;
import com.foxwho.demo.filter.JWTAuthenticationFilter;
import com.foxwho.demo.handler.Http401AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * SpringSecurity的配置
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 需要放行的URL
     */
    private static final String[] AUTH_WHITELIST = {
            // -- register login url
            "/auth/register",
            "/auth/login",
            // -- swagger ui
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/other/**"
    };

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    /**
     * 登录的时候,使用自定义身份验证组件
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new MyAuthenticationProvider(userDetailsService));
    }

    /***
     * 设置 HTTP 验证规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //跨域
        http.cors().and().csrf().disable()
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //需要放行的URL
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        new Http401AuthenticationEntryPoint("Basic xxxx"))
                .and()
//                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler) // 自定义访问失败处理器
//                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new MyJWTAuthenticationFilter(authenticationManager()))
                // 默认注销行为为logout，可以通过下面的方式来修改
                .logout()
                .logoutUrl("/logout")
                // 设置注销成功后跳转页面，默认是跳转到登录页面;
                .logoutSuccessUrl("/login")
//                .logoutSuccessHandler(customLogoutSuccessHandler)
                //其他都放行
                .permitAll();
    }


}
