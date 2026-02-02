package com.aoao.vo.system;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author aoao
 * @create 2026-01-31-19:06
 */
@Data
public class SysDeptQueryVo {

    private Long id;

    private String name;

    private String phone;

    private String leader;

    private Integer sortValue;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private List<SysDeptQueryVo> children;
}
