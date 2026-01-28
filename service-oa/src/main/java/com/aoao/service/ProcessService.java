package com.aoao.service;

import com.aoao.dto.process.ProcessFormDto;
import com.aoao.dto.process.ProcessQueryDto;
import com.aoao.result.PageResult;
import com.aoao.vo.process.ApprovalVo;
import com.aoao.vo.process.ProcessTypeWithTemplateVo;
import com.aoao.vo.process.ProcessVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-18-21:06
 */
public interface ProcessService {
    PageResult<ProcessVo> page(int page, int limit, ProcessQueryDto processQueryDto);

    void deloyByZip(String path);

    List<ProcessTypeWithTemplateVo> findProcessTypeWithTemplate();

    void startUp(ProcessFormDto processFormDto, HttpServletRequest request);

    PageResult<ProcessVo> findPending(int page, int limit);

    Map<String,Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    PageResult<ProcessVo> findProcessed(int page, int limit);

    PageResult<ProcessVo> findStarted(int page, int limit);
}
