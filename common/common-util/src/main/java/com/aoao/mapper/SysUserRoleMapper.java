package com.aoao.mapper;

import com.aoao.model.system.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author aoao
 * @create 2025-07-10-15:47
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertBatch(@Param("userRoleList") List<SysUserRole> userRoleList);

    List<Map<String, Object>> selectRoleNamesByUserIds(@Param("userIds") List<Long> ids);

    List<String> selectRoleNamesByUserId(Long userId);
}
