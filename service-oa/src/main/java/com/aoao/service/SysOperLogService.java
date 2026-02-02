package com.aoao.service;

import com.aoao.dto.system.SysOperLogQueryDto;
import com.aoao.model.system.SysOperLog;
import com.aoao.result.PageResult;
import org.springframework.stereotype.Service;

/**
 * @author aoao
 * @create 2026-02-02-19:09
 */
public interface SysOperLogService {
    PageResult<SysOperLog> page(Integer page, Integer limit, SysOperLogQueryDto sysOperLogQueryDto);
}
