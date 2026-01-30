package com.aoao.aspect;

import com.aoao.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;



import java.util.UUID;

/**
 * @author aoao
 * @create 2025-07-10-16:14
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.aoao.aspect.Log)")
    public void logPointcut() {}

    /**
     * 打印日志
     * */
    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 请求开始时间
            long startTime = System.currentTimeMillis();

            // MDC
            MDC.put("traceId", UUID.randomUUID().toString());

            // 获取被请求的类和方法
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // 获取入参
            Object[] args = joinPoint.getArgs();
            // 转换成json
            String arg = JsonUtil.toJson(args);

            // 获取描述信息
            String description = getDescription(joinPoint);


            // 执行切点方法
            Object result = joinPoint.proceed();
            log.info(
                    "\n====== 请求开始 ======\n" +
                            "业务描述: {}\n" +
                            "请求类: {}\n" +
                            "请求方法: {}\n" +
                            "入参: {}\n" +
                            "======================",
                    description, className, methodName, arg
            );


            // 结束计算消耗时间
            long endTime = System.currentTimeMillis();

            log.info(
                    "\n====== 请求结束 ======\n" +
                            "业务描述: {}\n" +
                            "耗时: {} ms\n" +
                            "出参: {}\n" +
                            "======================",
                    description, (endTime - startTime), JsonUtil.toJson(result)
            );


            return result;
        } finally {
            MDC.remove("traceId");
        }
    }

    private String getDescription(ProceedingJoinPoint joinPoint) {
        // 获得方法签名
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 获得方法
        return signature.getMethod().getAnnotation(Log.class).description();
    }


}
