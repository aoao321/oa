package com.aoao.controller;

import com.aoao.dto.system.SysOperLogQueryDto;
import com.aoao.dto.system.SysPostQueryDto;
import com.aoao.model.system.SysOperLog;
import com.aoao.model.system.SysPost;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.SysOperLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author aoao
 * @create 2026-02-02-19:07
 */
@RestController
@RequestMapping("/admin/system/sysOperLog")
public class SysOperLogController {

    @Autowired
    private SysOperLogService sysOperLogService;

    @ApiOperation("日志查询")
    @PostMapping("/findPage/{page}/{limit}")
    public Result<PageResult<SysOperLog>> findPage(@PathVariable Integer page,
                                                   @PathVariable Integer limit,
                                                   SysOperLogQueryDto sysOperLogQueryDto) {
        PageResult<SysOperLog> pageResult = sysOperLogService.page(page, limit, sysOperLogQueryDto);
        return Result.ok(pageResult);
    }

}
