package com.aoao.mapper;

import com.aoao.model.system.SysRole;
import com.aoao.model.system.SysUser;
import com.aoao.vo.system.SysUserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author aoao
 * @create 2025-07-10-15:46
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    List<SysUserVo> selectListByDto(@Param("keyword") String keyword, @Param("begin") Date begin, @Param("end") Date end);

    @Update("UPDATE sys_user SET status = #{status} WHERE id = #{id}")
    void updateStatusById(@Param("id") Long id,@Param("status") Integer status);
}
