package com.aoao.handler;

import com.aoao.enums.ResponseCodeEnum;
import com.aoao.exception.BaseException;
import com.aoao.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aoao
 * @create 2025-07-14-15:02
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获自定义业务异常
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public <T extends BaseException> Result handleBizException(HttpServletRequest request, T e) {
        log.warn("{} request fail, errorCode: {}, errorMessage: {}", request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Result.fail(e.getMessage());
    }

    /**
     * 其他类型异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public Result handleOtherException(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        return Result.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }


}
