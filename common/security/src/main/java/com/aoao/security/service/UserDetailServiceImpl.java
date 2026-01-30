package com.aoao.security.service;

import com.aoao.mapper.SysMenuMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.model.system.SysUser;
import com.aoao.vo.system.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-16-9:55
 */
@Service

public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(UserDetailServiceImpl.class);
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中根据传入的username查询用户信息
        SysUser selectedUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", username));
        // 判断用户是否存在
        if (selectedUser == null) {
            throw new UsernameNotFoundException(username);
        }
        // 查询用户权限
        List<String> list = sysMenuMapper.selectUserWithMenus(selectedUser.getUsername());
        return new LoginUser(selectedUser,list);
    }
}
