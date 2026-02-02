package com.aoao.mapper;

import com.aoao.dto.system.SysOperLogQueryDto;
import com.aoao.model.system.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:44
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
    List<SysOperLog> selectListByDto(@Param("dto") SysOperLogQueryDto sysOperLogQueryDto);
}
