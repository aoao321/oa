package com.aoao.controller;

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


    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<SysUserVo>> page(@PathVariable Integer page,
                                              @PathVariable Integer limit,
                                              SysUserQueryDto sysUserQueryDto) {
        PageResult<SysUserVo> pageResult = sysUserService.page(page,limit,sysUserQueryDto);
        return Result.ok(pageResult);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }


}
