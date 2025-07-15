package com.aoao.service.impl;

import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.mapper.SysUserMapper;
import com.aoao.model.system.SysUser;
import com.aoao.result.PageResult;
import com.aoao.service.SysUserService;
import com.aoao.vo.system.SysUserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-14-22:13
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 分页展示数据
     * @param page
     * @param limit
     * @param sysUserQueryDto
     * @return
     */
    @Override
    public PageResult<SysUserVo> page(Integer page, Integer limit, SysUserQueryDto sysUserQueryDto) {

        // 处理null情况
        sysUserQueryDto = Optional.ofNullable(sysUserQueryDto).orElseGet(SysUserQueryDto::new);



        // 开启分页
        PageHelper.startPage(page,limit);
        // 根据传入的条件返回符合的user
//        List<SysUser> userList = sysUserMapper.selectList(new QueryWrapper<SysUser>()
//                .lt(StringUtil.isNotEmpty(begin),"create_time", begin)
//                .lt(StringUtil.isNotEmpty(end),"create_time", end)
//                .like(StringUtil.isNotEmpty(keyword),"username", keyword));
        List<SysUserVo> userVoList = sysUserMapper.selectListByDto(sysUserQueryDto.getKeyword(),
                sysUserQueryDto.getCreateTimeBegin(),
                sysUserQueryDto.getCreateTimeEnd());
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(userVoList);
        // 获取分页信息并转换为VO列表
//        List<SysUserVo> userVoList = pageInfo.getList().stream()
//                .map(item -> {
//                    SysUserVo vo = new SysUserVo(); // 先创建目标VO对象
//                    BeanUtils.copyProperties(item, vo); // 属性拷贝
//                    return vo; // 返回转换后的对象
//                })
//                .collect(Collectors.toList()); // 收集为List
        return new PageResult<>(pageInfo.getTotal(),userVoList);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        sysUserMapper.updateStatusById(id,status);
    }


    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public void save(SysUser user) {
        sysUserMapper.insert(user);
    }

    @Override
    public void updateById(SysUser user) {
        sysUserMapper.updateById(user);
    }

    @Override
    public void removeById(Long id) {
        sysUserMapper.deleteById(id);
    }
}
