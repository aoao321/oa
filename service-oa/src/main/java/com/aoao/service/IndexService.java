package com.aoao.service;

import com.aoao.dto.system.LoginDto;
import com.aoao.vo.system.LoginVo;

/**
 * @author aoao
 * @create 2025-07-16-9:41
 */
public interface IndexService {

    LoginVo login(LoginDto loginDto);



}
