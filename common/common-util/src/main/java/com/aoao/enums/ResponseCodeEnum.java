package com.aoao.enums;

import lombok.Getter;

/**
 * @author aoao
 * @create 2025-07-11-9:27
 */
@Getter
public enum ResponseCodeEnum {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR(500, "出错啦，后台小哥正在努力修复中..."),
    FILE_UPLOAD_ERROR(300,"文件上传失败"),

    LOGIN_FAIL(400, "登录失败"),
    UNAUTHORIZED(402,"用户未授权"),
    USERNAME_OR_PWD_IS_NULL(403,"用户名或密码为空"),
    PWD_ERROR(401, "密码错误"),
    REPEAT_LOGIN(404,"用户重复登录"),
    FORBIDDEN(406, "权限不足"),
    STATUS_DOWN(405,"账号被禁用" ),
    EXPIRED_LOGIN(407,"登录超时，请重新登录");

    ResponseCodeEnum(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // 异常码
    private Integer errorCode;
    // 错误信息
    private String errorMessage;


}
