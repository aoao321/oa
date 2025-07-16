package com.aoao.service.impl;

import com.aoao.dto.system.LoginDto;

import com.aoao.enums.ResponseCodeEnum;
import com.aoao.result.Result;
import com.aoao.service.IndexService;
import com.aoao.utils.JwtTokenHelper;
import com.aoao.vo.system.LoginUser;
import com.aoao.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author aoao
 * @create 2025-07-16-9:42
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public LoginVo login(LoginDto loginDto) throws BadCredentialsException, DisabledException {
            // authenticate用户认证
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            // 使用authenticationManager执行密码认证
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 没通过，给出提示
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("登录失败");
            }
            // 获取认证的结果
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            String token = jwtTokenHelper.generateToken(loginUser.getUsername());
            return new LoginVo(token);


    }

}
