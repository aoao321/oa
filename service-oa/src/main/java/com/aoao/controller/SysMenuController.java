package com.aoao.controller;


import com.aoao.dto.system.AssignMenuDto;
import com.aoao.model.system.SysMenu;
import com.aoao.result.Result;
import com.aoao.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-14-16:52
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
@Api("菜单管理")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("查询所有菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @GetMapping("/findNodes")
    public Result<List<SysMenu>> findNodes() {
        List<SysMenu> nodes = sysMenuService.findNodes();
        return Result.ok(nodes);
    }

    @ApiOperation("新增菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation("删除菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("修改菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return Result.ok();
    }

    @ApiOperation("根据角色获取菜单")
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @GetMapping("/toAssign/{roleId}")
    public Result<List<SysMenu>> toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation("修改菜单权限")
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignMenuDto assignMenuDto) {
        sysMenuService.doAssign(assignMenuDto);
        return Result.ok();
    }
}
