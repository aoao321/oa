package com.aoao.service;

import com.aoao.dto.system.AssginRoleDto;
import com.aoao.dto.system.SysRoleDto;
import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.model.system.SysRole;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.vo.system.SysRoleQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-12-10:39
 */
public interface SysRoleService {

    PageResult<SysRole> queryPage(int page, int limit, String roleName);

    void removeById(Long id);

    void save(SysRoleDto sysRoleDto);

    SysRoleQueryVo getById(Long id);

    void update(SysRoleDto sysRoleDto);

    void removeByBatch(List<Long> ids);

    Map<String, Object> findRoleByUserId(Long userId);

    void updateUserRole(AssginRoleDto assginRoleDto);
}
