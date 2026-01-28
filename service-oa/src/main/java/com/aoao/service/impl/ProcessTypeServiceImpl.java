package com.aoao.service.impl;

import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.mapper.ProcessTypeMapper;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;
import com.aoao.service.ProcessTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-17-16:26
 */
@Service
public class ProcessTypeServiceImpl implements ProcessTypeService {

    @Autowired
    private ProcessTypeMapper processTypeMapper;

    @Override
    public PageResult<ProcessType> list(int page, int limit) {
        PageHelper.startPage(page, limit);
        List<ProcessType> processTypeList = processTypeMapper.selectList(null);
        PageInfo<ProcessType> pageInfo = new PageInfo<>(processTypeList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void save(ProcessTypeDto processTypeDto) {
        ProcessType processType = new ProcessType();
        BeanUtils.copyProperties(processTypeDto, processType);
        processTypeMapper.insert(processType);
    }

    @Override
    public void removeById(Long id) {
        processTypeMapper.deleteById(id);
    }

    @Override
    public ProcessType getById(Long id) {
        return processTypeMapper.selectById(id);
    }

    @Override
    public void update(ProcessTypeDto processTypeDto) {
        ProcessType processType = new ProcessType();
        BeanUtils.copyProperties(processTypeDto, processType);
        processTypeMapper.updateById(processType);
    }

    @Override
    public List<ProcessType> getList() {

        return processTypeMapper.selectList(null);
    }


}
