package com.aoao.vo.process;

import com.aoao.model.base.BaseEntity;
import com.aoao.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-18-23:43
 */
@Data
public class ProcessTypeWithTemplateVo extends BaseEntity {

    private String name;

    private String description;

    List<ProcessTemplate> processTemplateList;
}
