package com.aoao.security.handler;


import com.aoao.enums.ResponseCodeEnum;
import com.aoao.result.Result;
import com.aoao.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aoao
 * @create 2025-07-14-10:08
 */
@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String path = request.getRequestURI();

        log.warn("已认证用户访问了受限资源: {}", path);

        // 如果是登录接口并且已认证，就提示请勿重复登录
        if ("/admin/system/index/login".equals(path)) {
            HttpResultUtil.fail(response, Result.fail(ResponseCodeEnum.REPEAT_LOGIN));
        } else {
            HttpResultUtil.fail(response, Result.fail(ResponseCodeEnum.FORBIDDEN));
        }
    }
}
