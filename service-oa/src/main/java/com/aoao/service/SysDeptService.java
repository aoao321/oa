package com.aoao.service;

import com.aoao.dto.system.SysDeptQueryDto;
import com.aoao.model.system.SysDept;
import com.aoao.vo.system.SysDeptQueryVo;
import org.springframework.stereotype.Service;

/**
 * @author aoao
 * @create 2026-01-31-18:08
 */
@Service
public interface SysDeptService {
    SysDept findNodes(SysDeptQueryDto sysDeptQueryDto);
}
