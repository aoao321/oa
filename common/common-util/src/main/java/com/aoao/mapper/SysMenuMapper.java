package com.aoao.mapper;

import com.aoao.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:43
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> selectUserWithMenus(Long id);
}
