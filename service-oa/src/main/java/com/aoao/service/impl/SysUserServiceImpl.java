package com.aoao.service.impl;

import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.mapper.SysRoleMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.mapper.SysUserRoleMapper;
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
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-14-22:13
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


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
        List<SysUserVo> userVoList = sysUserMapper.selectListByDto(sysUserQueryDto.getKeyword(),
                sysUserQueryDto.getCreateTimeBegin(),
                sysUserQueryDto.getCreateTimeEnd());
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(userVoList);

        // 根据所有id查询角色名字
        List<Long> ids = userVoList
                .stream()
                .map(sysUserVo -> sysUserVo.getId()).collect(Collectors.toList());

        // 通过键值对形式返回角色信息
        // {role_name=普通管理员, user_id=1}
        List<Map<String, Object>> mapList = sysUserRoleMapper.selectRoleNamesByUserIds(ids);

        // 获取到userId对应的roleName集合
        // {1={"普通管理员","..."}}
        Map<Long, List<String>> userIdToRoleNamesMap = new HashMap<>();
        for (Map<String, Object> map : mapList) {
            Long userId = ((Number) map.get("user_id")).longValue();
            String roleName = (String) map.get("role_name");
            // 根据userId查询list是否存在
            if (!userIdToRoleNamesMap.containsKey(userId)) {
                userIdToRoleNamesMap.put(userId, new ArrayList<>());
            }
            // 存在直接把列名加入
            userIdToRoleNamesMap.get(userId).add(roleName);
        }

        // 给每个userVo设置roleName属性
        for (SysUserVo userVo : userVoList) {
            List<String> roleList = userIdToRoleNamesMap.getOrDefault(userVo.getId(), Collections.emptyList());
            userVo.setRoleList(roleList);
        }
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
