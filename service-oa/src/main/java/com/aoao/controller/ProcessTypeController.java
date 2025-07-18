package com.aoao.controller;

import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.ProcessTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-17-16:19
 */
@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;

    @GetMapping("/findAll")
    @ApiOperation("查询审批全部类型")
    public Result findAll() {
        List<ProcessType> list = processTypeService.getList();
        return Result.ok(list);
    }

    @ApiOperation("查询审批类型列表")
    @GetMapping("/{page}/{limit}")
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    public Result<PageResult<ProcessType>> queryPage(@PathVariable int page, @PathVariable int limit) {
        PageResult<ProcessType> processTypeList =  processTypeService.list(page,limit);
        return Result.ok(processTypeList);
    }

    @ApiOperation("新增类型")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('bnt.processTemplate.add')")
    public Result save(@RequestBody ProcessTypeDto processTypeDto) {
        processTypeService.save(processTypeDto);
        return Result.ok();
    }

    @ApiOperation("删除类型")
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    public Result remove(@PathVariable Long id) {
        processTypeService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据id获取类型")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    public Result<ProcessType> get(@PathVariable Long id) {
        ProcessType processType = processTypeService.getById(id);
        return Result.ok(processType);
    }

    @ApiOperation("更新类型")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('bnt.processTemplate.update')")
    public Result update(@RequestBody ProcessTypeDto processTypeDto) {
        processTypeService.update(processTypeDto);
        return Result.ok();
    }

}
