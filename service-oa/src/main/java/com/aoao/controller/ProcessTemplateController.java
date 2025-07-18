package com.aoao.controller;

import com.aoao.dto.process.ProcessTemplateDto;
import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.model.process.ProcessTemplate;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.ProcessTemplateService;
import com.aoao.vo.process.ProcessTemplateQueryListVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-17-16:18
 */
@RestController
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation("查询审批模板列表")
    @GetMapping("/{page}/{limit}")
    public Result<PageResult<ProcessTemplateQueryListVo>> queryPage(@PathVariable int page, @PathVariable int limit) {
        PageResult<ProcessTemplateQueryListVo> pageResult  =  processTemplateService.list(page,limit);
        return Result.ok(pageResult);
    }

    @ApiOperation("新增模板")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.ok();
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    public Result remove(@PathVariable Long id) {
        processTemplateService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据id获取模板")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    public Result<ProcessTemplate> get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    @ApiOperation("更新模板")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('bnt.processTemplate.pubilsh')")
    public Result update(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.update(processTemplate);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "上传流程定义" )
    @PostMapping("/uploadProcessDefinition")
    public Result<Map<String, Object>> uploadProcessDefinition(MultipartFile file) {
        try {
            Map<String,Object> map = processTemplateService.uploadProcessDefinition(file);
            return Result.ok(map);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation("发布")
    @PutMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    public Result publish(@PathVariable Long id) {
        // 模板的status==1说明该模板已经发布
        processTemplateService.publish(id);
        return Result.ok();
    }


}
