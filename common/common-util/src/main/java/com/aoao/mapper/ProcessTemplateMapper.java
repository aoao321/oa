package com.aoao.mapper;


import com.aoao.model.process.ProcessTemplate;
import com.aoao.vo.process.ProcessTemplateQueryListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-10-15:41
 */
@Mapper
public interface ProcessTemplateMapper extends BaseMapper<ProcessTemplate> {
    List<ProcessTemplateQueryListVo> selectListWithTypeName();

    List<ProcessTemplate> selectListByTypeIds(@Param("list")List<Long> typeIds);
}
