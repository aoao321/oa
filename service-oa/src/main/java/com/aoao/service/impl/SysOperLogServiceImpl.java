package com.aoao.service.impl;

import com.aoao.dto.system.SysOperLogQueryDto;
import com.aoao.mapper.SysOperLogMapper;
import com.aoao.model.system.SysOperLog;
import com.aoao.result.PageResult;
import com.aoao.service.SysOperLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aoao
 * @create 2026-02-02-19:09
 */
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public PageResult<SysOperLog> page(Integer page, Integer limit, SysOperLogQueryDto sysOperLogQueryDto) {
        // 分页
        PageHelper.startPage(page, limit);
        // 查询
        List<SysOperLog> operLogList = sysOperLogMapper.selectListByDto(sysOperLogQueryDto);
        PageInfo<SysOperLog> pageInfo = new PageInfo<>(operLogList);
        PageResult<SysOperLog> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }
}
