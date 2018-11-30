package com.foxwho.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxwho.demo.consts.JwtConsts;
import com.foxwho.demo.model.JwtUser;
import com.foxwho.demo.model.User;
import com.foxwho.demo.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //以下 如果不设置，那么默认的 登录就是  /login
        super.setFilterProcessesUrl("/auth/login");
    }

    /**
     * 解析用户凭证,获取登录信息
     *
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户成功登录后，这个方法会被调用，在这里生成token
     *
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = "";
        try {
            System.out.println("auth.getPrincipal()");
            System.out.println(auth.getAuthorities());
            System.out.println(auth.getCredentials());
            System.out.println(auth.getName());
            System.out.println(auth.getPrincipal());
            JwtUser jwtUser = (JwtUser) auth.getPrincipal();
            System.out.println("jwtUser:" + jwtUser.toString());

            // 定义存放角色集合的对象
            List roleList = new ArrayList<>();
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                roleList.add(grantedAuthority.getAuthority());
            }
            token = JwtUtils.createToken(jwtUser.getUsername(), roleList, false);
            // 登录成功后，返回token到header里面
            response.addHeader(JwtConsts.HEADER_AUTHORIZATION_NAME, JwtConsts.HEADER_AUTHORIZATION_PREFIX + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 错误时候
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }

}
