package com.aoao.service;

import com.aoao.dto.system.SysPostQueryDto;
import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.model.system.SysPost;
import com.aoao.result.PageResult;
import com.aoao.result.Result;

/**
 * @author aoao
 * @create 2026-02-02-18:17
 */
public interface SysPostService {
    PageResult<SysPost> page(Integer page, Integer limit, SysPostQueryDto sysPostQueryDto);
}
