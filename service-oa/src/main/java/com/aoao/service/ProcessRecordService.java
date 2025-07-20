package com.aoao.service;

/**
 * @author aoao
 * @create 2025-07-19-18:05
 */
public interface ProcessRecordService {

    void record(Long processId,Integer status,String description);
}
