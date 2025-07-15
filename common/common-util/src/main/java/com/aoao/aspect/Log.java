package com.aoao.aspect;

import java.lang.annotation.*;

/**
 * @author aoao
 * @create 2025-07-10-16:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Log {
    String description() default "";
}
