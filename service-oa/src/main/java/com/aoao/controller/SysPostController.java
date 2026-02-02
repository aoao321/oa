package com.aoao.controller;

import com.aoao.dto.system.SysPostQueryDto;
import com.aoao.model.system.SysDept;
import com.aoao.model.system.SysPost;
import com.aoao.result.PageResult;
import com.aoao.result.Result;
import com.aoao.service.SysPostService;
import com.aoao.vo.system.SysUserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author aoao
 * @create 2026-02-02-18:18
 */
@RestController
@RequestMapping("/admin/system/sysPost")
public class SysPostController {

    @Autowired
    private SysPostService sysPostService;

    @ApiOperation("查询岗位")
    @GetMapping("/findPage/{page}/{limit}")
    public Result<PageResult<SysPost>> findPage(@PathVariable Integer page,
                                                @PathVariable Integer limit,
                                                SysPostQueryDto sysPostQueryDto) {
        PageResult<SysPost> pageResult = sysPostService.page(page, limit, sysPostQueryDto);
        return Result.ok(pageResult);
    }

}
