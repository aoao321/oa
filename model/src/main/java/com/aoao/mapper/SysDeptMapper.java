package com.aoao.mapper;

import com.aoao.dto.system.SysDeptQueryDto;
import com.aoao.model.system.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:42
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    List<SysDept> selectListByDto(@Param("dto") SysDeptQueryDto sysDeptQueryDto);
}
