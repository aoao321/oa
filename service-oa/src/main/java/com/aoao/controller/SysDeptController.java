package com.aoao.controller;

import com.aoao.dto.system.SysDeptQueryDto;
import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.model.system.SysDept;
import com.aoao.result.Result;
import com.aoao.service.SysDeptService;
import com.aoao.vo.system.SysDeptQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author aoao
 * @create 2026-01-31-18:06
 */
@RestController
@RequestMapping("/admin/system/sysDept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/findNodes")
    @ApiOperation("查询部门")
    public Result<SysDept> findNodes(SysDeptQueryDto sysDeptQueryDto){
        SysDept vo = sysDeptService.findNodes(sysDeptQueryDto);
        return Result.ok(vo);
    }
}
