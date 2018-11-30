package com.foxwho.demo.adapter;

import com.foxwho.demo.consts.JwtConsts;
import com.foxwho.demo.service.impl.GrantedAuthorityImpl;
import com.foxwho.demo.util.Password;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 自定义身份认证验证组件
 */
public class MyAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public MyAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null != userDetails) {
            if (password(password, userDetails)) {
                // 这里设置权限和角色
//                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
//                authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
                // 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
//                Authentication auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
//                return auth;
//                return new UsernamePasswordAuthenticationToken(username, null,
//                        Collections.singleton(new SimpleGrantedAuthority(JwtConsts.ROLE_DEFAULT))
//                );
                return new UsernamePasswordAuthenticationToken(userDetails, null,
                        Collections.singleton(new SimpleGrantedAuthority(JwtConsts.ROLE_DEFAULT))
                );
            } else {
                throw new BadCredentialsException("密码错误");
            }
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    /**
     * 密码验证
     *
     * @param password
     * @param userDetails
     * @return
     */
    public boolean password(String password, UserDetails userDetails) {
        if (password == null) {
            return false;
        }
        if (password.length() < 1) {
            return false;
        }
        String pwd = Password.md5(password);
        if (pwd.equals(userDetails.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 是否可以提供输入类型的认证服务
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
