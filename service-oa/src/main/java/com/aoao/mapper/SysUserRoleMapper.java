package com.aoao.mapper;

import com.aoao.model.system.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:47
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    void insertBatch(@Param("userRoleList") List<SysUserRole> userRoleList);
}
