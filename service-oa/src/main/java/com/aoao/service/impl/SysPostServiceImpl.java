package com.aoao.service.impl;

import com.aoao.dto.system.SysPostQueryDto;
import com.aoao.dto.system.SysUserQueryDto;
import com.aoao.mapper.SysPostMapper;
import com.aoao.model.system.SysPost;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.SysPostService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aoao
 * @create 2026-02-02-18:16
 */
@Service
public class SysPostServiceImpl implements SysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public PageResult<SysPost> page(Integer page, Integer limit, SysPostQueryDto sysPostQueryDto) {
        // 开启分页
        PageHelper.startPage(page, limit);
        // 查询
        String keyword = sysPostQueryDto.getKeyword();
        List<SysPost> postList = sysPostMapper.selectListByKeyWord(keyword);
        PageInfo<SysPost> pageInfo = new PageInfo<>(postList);
        PageResult<SysPost> pageResult = new PageResult<>(pageInfo.getTotal(),postList);
        return pageResult;
    }
}
