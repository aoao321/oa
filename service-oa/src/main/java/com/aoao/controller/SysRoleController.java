package com.aoao.controller;

import com.aoao.dto.system.AssginRoleDto;
import com.aoao.dto.system.SysRoleDto;
import com.aoao.model.system.SysRole;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.SysRoleService;
import com.aoao.vo.system.SysRoleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-11-16:06
 */
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/{page}/{limit}")
    @ApiOperation("分页查询角色信息")
    public Result<PageResult<SysRole>> getSysRolePage(@PathVariable("page" ) int page ,
                                             @PathVariable("limit" ) int limit,
                                             @RequestParam(value = "roleName", required = false) String roleName) {
        PageResult<SysRole> sysRolePageResult = sysRoleService.queryPage(page, limit, roleName);
        return Result.ok(sysRolePageResult);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除角色")
    public Result removeSysRole(@PathVariable("id" ) Long id){
        sysRoleService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PostMapping("/save")
    @ApiOperation("新增角色")
    public Result saveSysRole(@RequestBody SysRoleDto sysRoleDto){
        sysRoleService.save(sysRoleDto);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/get/{id}")
    @ApiOperation("根据id查询角色")
    public Result<SysRoleQueryVo> getSysRoleById(@PathVariable Long id){
        SysRoleQueryVo sysRoleQueryVo = sysRoleService.getById(id);
        return Result.ok(sysRoleQueryVo);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PutMapping("/update")
    @ApiOperation("修改角色信息")
    public Result updateSysRole(@RequestBody SysRoleDto sysRoleDto){
        sysRoleService.update(sysRoleDto);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/batchRemove")
    @ApiOperation("批量删除")
    public Result batchRemoveSysRole(@RequestBody List<Long> ids){
        sysRoleService.removeByBatch(ids);
        return Result.ok();
    }

    @ApiOperation("根据用户获取角色数据")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/toAssign/{userId}")
    public Result<Map<String, Object>> toAssign(@PathVariable Long userId) {
        Map<String, Object> roles= sysRoleService.findRoleByUserId(userId);
        return Result.ok(roles);
    }


    @ApiOperation("修改用户角色")
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto){
        sysRoleService.updateUserRole(assginRoleDto);
        return Result.ok();
    }




}
