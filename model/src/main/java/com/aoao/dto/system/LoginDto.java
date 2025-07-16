package com.aoao.dto.system;


import lombok.Data;

/**
 * 登录对象
 */
@Data
public class LoginDto {

    /**
     * 手机号
     */
    private String username;

    /**
     * 密码
     */
    private String password;


}
