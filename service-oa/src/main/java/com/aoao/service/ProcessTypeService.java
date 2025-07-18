package com.aoao.service;

import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-17-16:26
 */
public interface ProcessTypeService {
    PageResult<ProcessType> list(int page, int limit);

    void save(ProcessTypeDto processTypeDto);

    void removeById(Long id);

    ProcessType getById(Long id);

    void update(ProcessTypeDto processTypeDto);

    List<ProcessType> getList();

}
