package com.aoao.controller;

import com.aoao.dto.process.ProcessFormDto;
import com.aoao.model.process.ProcessTemplate;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.ProcessService;
import com.aoao.dto.process.ProcessQueryDto;
import com.aoao.service.ProcessTemplateService;
import com.aoao.vo.process.ApprovalVo;
import com.aoao.vo.process.ProcessTypeWithTemplateVo;
import com.aoao.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-18-21:05
 */
@Slf4j
@RestController
@RequestMapping("/admin/process")
@Api("审批模块")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTemplateService processTemplateService;


    @ApiOperation("启动流程")
    @PostMapping("/startUp")
    public Result startUp(@RequestBody ProcessFormDto processFormDto, HttpServletRequest request) {
        processService.startUp(processFormDto,request);
        return Result.ok();
    }

    @ApiOperation("查询审批列表")
    @GetMapping("/{page}/{limit}")
    public Result<PageResult<ProcessVo>> page(@PathVariable("page") int page, @PathVariable("limit") int limit, ProcessQueryDto processQueryDto) {
        PageResult<ProcessVo> pageResult = processService.page(page,limit,processQueryDto);
        return Result.ok(pageResult);
    }

    @ApiOperation("查询审批详情")
    @GetMapping("/show/{id}")
    public Result<Map<String,Object>> show(@PathVariable("id") Long id) {
        Map<String,Object> map = processService.show(id);
        return Result.ok(map);
    }

    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result<PageResult<ProcessVo>> findPending(@PathVariable int page, @PathVariable int limit) {
        PageResult<ProcessVo> pageResult = processService.findPending(page,limit);
        return Result.ok(pageResult);
    }

    @ApiOperation("审批")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }

    @ApiOperation("查询已处理审批列表")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result<PageResult<ProcessVo>> findProcessed(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        PageResult<ProcessVo> pageResult = processService.findProcessed(page,limit);
        return Result.ok(pageResult);
    }

    @ApiOperation("查询已发进度")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result<PageResult<ProcessVo>> findStarted(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        PageResult<ProcessVo> pageResult = processService.findStarted(page,limit);

        return Result.ok(pageResult);
    }

    @ApiOperation("查询审批分类和每个分类的模板")
    @GetMapping("findProcessType")
    public Result<List<ProcessTypeWithTemplateVo>> findProcessType() {
        List<ProcessTypeWithTemplateVo> vo = processService.findProcessTypeWithTemplate();
        return Result.ok(vo);
    }

    @ApiOperation("查询具体模板")
    @GetMapping("getProcessTemplate/{id}")
    public Result<ProcessTemplate> getProcessTemplate(@PathVariable("id") Long id) {
        ProcessTemplate template = processTemplateService.getById(id);
        return Result.ok(template);
    }
}
