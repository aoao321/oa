package com.aoao.mapper;

import com.aoao.model.system.SysPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:45
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {
    List<SysPost> selectListByKeyWord(@Param("keyword") String keyword);
}
