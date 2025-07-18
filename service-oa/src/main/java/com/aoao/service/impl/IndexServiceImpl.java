package com.aoao.service.impl;

import com.aoao.dto.system.LoginDto;

import com.aoao.enums.ResponseCodeEnum;
import com.aoao.mapper.SysMenuMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.model.system.SysUser;
import com.aoao.result.Result;
import com.aoao.service.IndexService;
import com.aoao.service.SysMenuService;
import com.aoao.service.SysUserService;
import com.aoao.utils.JwtTokenHelper;
import com.aoao.vo.system.LoginUser;
import com.aoao.vo.system.LoginVo;
import com.aoao.vo.system.MetaVo;
import com.aoao.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuService sysMenuService;

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

    @Override
    public Map<String, Object> info(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("JWT Token is missing");
        }
        // Bearer前缀，需要去掉
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 从token中获取用户名
        String username = jwtTokenHelper.getUsernameByToken(token);

        // 获取用户
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        Long userId = sysUser.getId();

        // 根据userId获取路由信息，返回用户可以操作的左侧菜单
        List<RouterVo> routerVoList = sysMenuService.findSysMenuByUserId(userId);




        // 根据userId获取按钮权限，返回用户可以操作的按钮
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        // 封装返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("roles", Collections.singletonList("admin"));
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers", routerVoList);
        map.put("buttons", permsList);

        return map;
    }







}
