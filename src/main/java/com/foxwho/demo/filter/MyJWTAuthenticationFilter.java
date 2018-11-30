package com.foxwho.demo.filter;

import com.foxwho.demo.consts.JwtConsts;
import com.foxwho.demo.exception.HttpAesException;
import com.foxwho.demo.service.impl.GrantedAuthorityImpl;
import com.foxwho.demo.util.JwtUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义JWT认证过滤器
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
public class MyJWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyJWTAuthenticationFilter.class);

    public MyJWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtConsts.HEADER_AUTHORIZATION_NAME);
        // 如果请求头中没有Authorization信息则直接放行了
        if (header == null || !header.startsWith(JwtConsts.HEADER_AUTHORIZATION_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
        chain.doFilter(request, response);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     *
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        long start = System.currentTimeMillis();
        String token = request.getHeader(JwtConsts.HEADER_AUTHORIZATION_NAME);
        if (token == null || token.isEmpty()) {
            throw new HttpAesException("Token为空");
        }
        String username = null;
        try {
            username = JwtUtils.getUsername(token);
            long end = System.currentTimeMillis();
            logger.info("执行时间: {}", (end - start) + " 毫秒");
            if (username != null) {
                String role = JwtUtils.getUserRole(token);
//                List role = JwtUtils.getUserRole(token);
//                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//                for (int i = 0; i < role.size(); i++) {
//                    authorities.add(new GrantedAuthorityImpl(role.get(i).toString()));
//                }
//                return new UsernamePasswordAuthenticationToken(username, null, authorities);
                return new UsernamePasswordAuthenticationToken(username, null,
                        Collections.singleton(new SimpleGrantedAuthority(role))
                );
            }

        } catch (ExpiredJwtException e) {
            logger.error("Token已过期: {} " + e);
            throw new HttpAesException("Token已过期");
        } catch (UnsupportedJwtException e) {
            logger.error("Token格式错误: {} " + e);
            throw new HttpAesException("Token格式错误");
        } catch (MalformedJwtException e) {
            logger.error("Token没有被正确构造: {} " + e);
            throw new HttpAesException("Token没有被正确构造");
        } catch (SignatureException e) {
            logger.error("签名失败: {} " + e);
            throw new HttpAesException("签名失败");
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常: {} " + e);
            throw new HttpAesException("非法参数异常");
        }

        return null;
    }

}
