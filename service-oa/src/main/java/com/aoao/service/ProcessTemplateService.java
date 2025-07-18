package com.aoao.service;

import com.aoao.dto.process.ProcessTemplateDto;
import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.model.process.ProcessTemplate;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;
import com.aoao.vo.process.ProcessTemplateQueryListVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-18-9:22
 */
public interface ProcessTemplateService {
    PageResult<ProcessTemplateQueryListVo> list(int page, int limit);

    void save(ProcessTemplateDto processTemplateDto);

    void removeById(Long id);

    ProcessTemplate getById(Long id);

    void update(ProcessTypeDto processTypeDto);

    Map<String,Object> uploadProcessDefinition(MultipartFile file) throws FileNotFoundException;
}
