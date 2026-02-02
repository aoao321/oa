package com.aoao.aspect;

import java.lang.annotation.*;

/**
 * @author aoao
 * @create 2026-02-02-19:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OperLog {

    // 模块名称
    String title() default "";

    // 业务类型：0-其它 1-新增 2-修改 3-删除 4-查询
    String businessType() default "";
}
