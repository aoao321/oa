package com.aoao.service.impl;

import com.aoao.dto.system.AssginRoleDto;
import com.aoao.dto.system.SysRoleDto;
import com.aoao.mapper.SysRoleMapper;
import com.aoao.mapper.SysUserRoleMapper;
import com.aoao.model.system.SysRole;
import com.aoao.model.system.SysUserRole;
import com.aoao.result.PageResult;
import com.aoao.service.SysRoleService;
import com.aoao.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
@author aoao
@create 2025-07-12-10:40
*/
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public PageResult<SysRole> queryPage(int page, int limit, String roleName) {
        // 开启分页
        PageHelper.startPage(page,limit);

        List<SysRole> sysRoles = sysRoleMapper.selectList(new QueryWrapper<SysRole>()
                .eq(StringUtils.isNotBlank(roleName), "role_name",roleName));

        PageInfo<SysRole> pageInfo = new PageInfo<>(sysRoles);
        // 封装分页信息
        PageResult<SysRole> pageResult = new PageResult<>(pageInfo.getTotal(),pageInfo.getList());

        return pageResult;
    }

    @Override
    public void removeById(Long id) {
        sysRoleMapper.deleteById(id);
    }

    @Override
    public void save(SysRoleDto sysRoleDto) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDto,sysRole);
        // 设置属性
        sysRole.setCreateTime(new Date());
        // TODO: 由当前管理员创建
        sysRole.setDescription("");
        sysRoleMapper.insert(sysRole);

    }

    @Override
    public SysRoleQueryVo getById(Long id) {
        // 查询
        SysRole sysRole = sysRoleMapper.selectById(id);
        SysRoleQueryVo sysRoleQueryVo = new SysRoleQueryVo();
        sysRoleQueryVo.setId(id);
        // 转化为vo展示
        BeanUtils.copyProperties(sysRole,sysRoleQueryVo);

        return sysRoleQueryVo;

    }

    @Override
    public void update(SysRoleDto sysRoleDto) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDto,sysRole);
        sysRoleMapper.update(sysRole,new QueryWrapper<SysRole>().eq("id",sysRole.getId()));

    }

    @Override
    public void removeByBatch(List<Long> ids) {
        sysRoleMapper.deleteBatchIds(ids);

    }

    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        //查询所有的角色
        List<SysRole> allRolesList = sysRoleMapper.selectList(null);

        //拥有的角色id
        List<SysUserRole> existUserRoleList = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId));
        List<Long> existRoleIdList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole role : allRolesList) {
            //已分配
            if(existRoleIdList.contains(role.getId())) {
                assginRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Override
    @Transactional
    public void updateUserRole(AssginRoleDto assginRoleDto) {
        Long userId = assginRoleDto.getUserId();
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        // 先删除用户的角色
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        // 便利roleIdList
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole(roleId,userId);
            userRoleList.add(sysUserRole);
        }
        sysUserRoleMapper.insertBatch(userRoleList);

    }

}
