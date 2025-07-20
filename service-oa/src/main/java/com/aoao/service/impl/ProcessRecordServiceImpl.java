package com.aoao.service.impl;

import com.aoao.context.UserContext;
import com.aoao.mapper.ProcessRecordMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.model.process.ProcessRecord;
import com.aoao.model.system.SysUser;
import com.aoao.service.ProcessRecordService;
import com.aoao.utils.JwtTokenHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author aoao
 * @create 2025-07-19-18:05
 */
@Service
public class ProcessRecordServiceImpl implements ProcessRecordService {

    @Autowired
    private ProcessRecordMapper processRecordMapper;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void record(Long processId, Integer status, String description) {
        String username = UserContext.getUsername();
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        ProcessRecord build = new ProcessRecord();
        build.setProcessId(processId);
        build.setStatus(status);
        build.setDescription(description);
        build.setOperateUserId(sysUser.getId());
        build.setOperateUser(sysUser.getName());

        processRecordMapper.insert(build);
    }

}
