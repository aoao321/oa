package com.aoao.service;

import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.model.system.SysUser;
import com.aoao.result.PageResult;
import com.aoao.vo.system.SysUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author aoao
 * @create 2025-07-14-22:13
 */
public interface SysUserService  {


    SysUser getById(Long id);

    void save(SysUser user);

    void updateById(SysUser user);

    void removeById(Long id);

    PageResult<SysUserVo> page(Integer page, Integer limit, SysUserQueryDto sysUserQueryDto);

    void updateStatus(Long id, Integer status);
}
