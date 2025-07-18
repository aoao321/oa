package com.aoao.vo.process;

import com.aoao.model.base.BaseEntity;
import lombok.Data;

/**
 * @author aoao
 * @create 2025-07-18-9:52
 */
@Data
public class ProcessTemplateQueryListVo extends BaseEntity {

    private String name;

    private String iconUrl;

    private String processTypeName;

    private String description;
}
