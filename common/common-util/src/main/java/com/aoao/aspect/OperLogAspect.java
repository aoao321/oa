package com.aoao.aspect;

import com.aoao.mapper.SysOperLogMapper;
import com.aoao.model.system.SysOperLog;
import com.aoao.utils.JsonUtil;
import com.aoao.utils.JwtTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author aoao
 * @create 2026-02-02-19:45
 */
@Component
@Aspect
@Slf4j
public class OperLogAspect {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Pointcut("@annotation(com.aoao.aspect.OperLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            saveOperLog(joinPoint, result, exception);
        }
    }

    private void saveOperLog(ProceedingJoinPoint joinPoint, Object result, Exception e) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperLog operLog = method.getAnnotation(OperLog.class);

            SysOperLog log = new SysOperLog();
            log.setTitle(operLog.title());
            log.setBusinessType(operLog.businessType());

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            log.setMethod(className + "." + methodName + "()");

            log.setRequestMethod(request.getMethod());
            log.setOperUrl(request.getRequestURI());
            log.setOperIp("127.0.0.1");
            log.setOperTime(new Date());

            // 从请求头获取 Authorization
            String authHeader = request.getHeader("Authorization");

            String username = "anonymous";
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // 去掉 "Bearer "
                username = jwtTokenHelper.getUsernameByToken(token);
            }
            log.setOperName(username);

            // 操作类别（默认后台用户）
            log.setOperatorType("1");


            // 请求参数
            Object[] args = joinPoint.getArgs();
            log.setOperParam(JsonUtil.toJson(args));

            // 返回参数
            if (result != null) {
                log.setJsonResult(JsonUtil.toJson(result));
            }

            // 状态
            if (e == null) {
                log.setStatus(0);  // 正常
            } else {
                log.setStatus(1);  // 异常
                log.setErrorMsg(e.getMessage());
            }

            sysOperLogMapper.insert(log);
        } catch (Exception ex) {
            log.error("记录操作日志失败", ex);
        }
    }
}