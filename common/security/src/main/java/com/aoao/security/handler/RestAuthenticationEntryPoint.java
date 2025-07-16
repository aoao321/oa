package com.aoao.security.handler;

/**
 * @author aoao
 * @create 2025-07-13-22:18
 */


import com.aoao.enums.ResponseCodeEnum;
import com.aoao.result.Result;
import com.aoao.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: aoao
 * @description: 用户未登录访问受保护的资源
 **/
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("用户未登录访问受保护的资源: {}", authException.getMessage());
        if (authException instanceof InsufficientAuthenticationException) {
            HttpResultUtil.fail(response, HttpStatus.UNAUTHORIZED.value(), Result.fail(ResponseCodeEnum.UNAUTHORIZED));
            return;
        }

        HttpResultUtil.fail(response, HttpStatus.UNAUTHORIZED.value(), Result.fail(authException.getMessage()));
    }
}
