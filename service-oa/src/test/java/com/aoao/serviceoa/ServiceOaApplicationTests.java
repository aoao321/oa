package com.aoao.serviceoa;

import com.aoao.mapper.SysRoleMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.mapper.SysUserRoleMapper;
import com.aoao.model.system.SysRole;
import com.aoao.model.system.SysUser;
import com.aoao.model.system.SysUserRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceOaApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Test
    void mpTest() {

        sysUserMapper
                .selectList(new QueryWrapper<SysUser>().between("id", 1, 10))
                .forEach(user -> {
                    user.setPassword("");
                    System.out.println(user);
                });

        //sysUserMapper.delete(new QueryWrapper<SysUser>().eq("id", 7));

    }

    @Test
    void mpTest2() {
        System.out.println(sysUserRoleMapper.selectMaps(new QueryWrapper<SysUserRole>().eq("user_id", 1l)));
    }

}
