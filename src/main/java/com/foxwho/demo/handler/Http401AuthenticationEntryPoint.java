package com.foxwho.demo.handler;

import com.foxwho.demo.consts.JwtConsts;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证拦截器
 */
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String headerValue;

    public Http401AuthenticationEntryPoint(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader(JwtConsts.HEADER_AUTHORIZATION_NAME, this.headerValue);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}
