package com.aoao.service.impl;

import com.aoao.dto.system.SysDeptQueryDto;
import com.aoao.mapper.SysDeptMapper;
import com.aoao.model.system.SysDept;
import com.aoao.service.SysDeptService;
import com.aoao.vo.system.SysDeptQueryVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aoao
 * @create 2026-01-31-18:08
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public SysDept findNodes(SysDeptQueryDto sysDeptQueryDto) {
        // 查询数据库
        List<SysDept> sysDeptList = sysDeptMapper.selectListByDto(sysDeptQueryDto);
        // 构建vo
        SysDept dept = new SysDept();
        for (SysDept sysDept : sysDeptList) {
            if (sysDept.getParentId() == 0) {
                sysDept.setChildren(findChildren(sysDept.getId(), sysDeptList));
                dept = sysDept;
            }
        }
        return dept;
    }

    private List<SysDept> findChildren(Long id, List<SysDept> sysDepts) {
        List<SysDept> children = new ArrayList<>();
        // 遍历sysMenus，如果id==父id
        for (SysDept sysDept : sysDepts) {
            // 如果是子节点
            if (sysDept.getParentId().equals(id)) {
                // 继续递归找子节点的子节点
                sysDept.setChildren(findChildren(sysDept.getId(), sysDepts));
                children.add(sysDept);
            }
        }
        return children;
    }
}
