package com.aoao.controller;

import com.aoao.dto.system.LoginDto;
import com.aoao.enums.ResponseCodeEnum;
import com.aoao.result.Result;
import com.aoao.service.IndexService;
import com.aoao.vo.system.LoginVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-11-14:22
 */
@RestController
@RequestMapping("/admin/system/index")
@ApiModel("登录登出模块")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        try {
            return Result.ok(indexService.login(loginDto));
        }catch (BadCredentialsException e){
            return Result.fail(ResponseCodeEnum.PWD_ERROR);
        }catch (DisabledException e){
            return Result.fail(ResponseCodeEnum.STATUS_DOWN);
        }
    }

    @GetMapping("/info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }


}
