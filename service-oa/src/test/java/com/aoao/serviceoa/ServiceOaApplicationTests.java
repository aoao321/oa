package com.aoao.serviceoa;

import com.aoao.mapper.ProcessMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.mapper.SysUserRoleMapper;
import com.aoao.model.process.Process;
import com.aoao.model.system.SysUser;
import com.aoao.model.system.SysUserRole;
import com.aoao.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ServiceOaApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ProcessMapper processMapper;


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
        //System.out.println(sysUserRoleMapper.selectMaps(new QueryWrapper<SysUserRole>().eq("user_id", 1l)));
        List<Long> list = new ArrayList<>();
        list.add(6l);
        list.add(7l);
        list.add(8l);
        List<ProcessVo> processVoList = processMapper.selectProcessVoByProcessIds(list, "张三");
        System.out.println(processVoList);
    }

    @Test
    void tokenTest3() {
        Process process = processMapper.selectById(8L);
        System.out.println(process.getCurrentAuditor());
    }

}
