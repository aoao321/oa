package com.aoao.controller;

import com.aoao.aspect.OperLog;
import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.model.system.SysUser;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.SysRoleService;
import com.aoao.service.SysUserService;
import com.aoao.vo.system.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-14-16:55
 */
@RestController
@RequestMapping("/admin/system/sysUser")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;


    @ApiOperation("用户条件分页查询")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<SysUserVo>> page(@PathVariable Integer page,
                                              @PathVariable Integer limit,
                                              SysUserQueryDto sysUserQueryDto) {
        PageResult<SysUserVo> pageResult = sysUserService.page(page,limit,sysUserQueryDto);
        return Result.ok(pageResult);
    }

    @ApiOperation(value = "获取用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @OperLog(title = "新增用户", businessType = "INSERT")
    @ApiOperation(value = "保存用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.ok();
    }

    @OperLog(title = "更新用户", businessType = "UPDATE")
    @ApiOperation(value = "更新用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @OperLog(title = "删除用户", businessType = "DELETE")
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @OperLog(title = "更新用户状态", businessType = "UPDATE")
    @ApiOperation(value = "更新用户状态")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }


}
