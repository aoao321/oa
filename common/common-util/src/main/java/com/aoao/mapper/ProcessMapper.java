package com.aoao.mapper;

import com.aoao.dto.process.ProcessQueryDto;
import com.aoao.model.process.Process;
import com.aoao.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:40
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    List<ProcessVo> selectProcessVo(@Param("dto") ProcessQueryDto processQueryDto);

    List<ProcessVo> selectProcessVoByProcessIds(@Param("ids") List<Long> processIds, @Param("username") String username);


}
