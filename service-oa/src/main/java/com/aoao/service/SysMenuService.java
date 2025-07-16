package com.aoao.service;

import com.aoao.dto.system.AssignMenuDto;
import com.aoao.model.system.SysMenu;
import com.aoao.vo.system.RouterVo;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-15-14:33
 */
public interface SysMenuService {
    List<SysMenu> findNodes();

    void save(SysMenu sysMenu);

    void removeById(Long id);

    void update(SysMenu sysMenu);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssignMenuDto assignMenuDto);

    List<RouterVo> findSysMenuByUserId(Long userId);

    List<String> findUserPermsList(Long id);
}
