package com.itzhang.management.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.itzhang.management.aop.annotation.LogOperation;
import com.itzhang.management.entity.pojo.SysLog;
import com.itzhang.management.mapper.LogMapper;
import com.itzhang.management.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogOperationAspect {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private LogMapper logMapper;

    /**
     * @param
     * @return void
     * @Description 切点：标记了 @LogOperation 注解的方法都会被切
     * @Author weiloong_zhang
     */
    @Pointcut("@annotation(com.itzhang.management.aop.annotation.LogOperation)")
    public void logPointCut() {
    }

    /**
     * @param
     * @return void
     * @Description 方法返回后通知
     * @Author weiloong_zhang
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.info("切面方法执行完毕日志记录");
        saleLog(joinPoint, result, null);
    }

    /**
     * @param joinPoint ex
     * @return void
     * @Description 方法抛出异常后通知
     * @Author weiloong_zhang
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("切面方法执行异常日志记录");
        saleLog(joinPoint, null, ex);
    }

    /**
     * @param
     * @return void
     * @Description 日志保存方法
     * @Author weiloong_zhang
     */
    private void saleLog(JoinPoint joinPoint, Object result, Exception ex) {
        try {
            //获取方法签名
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

            Method method = methodSignature.getMethod();

            //获取方法上的注解
            LogOperation logOperation = method.getAnnotation(LogOperation.class);

            //判断注解是否存在
            if (logOperation == null) {
                return;
            }

            //开始获取注解上的模块名称和操作行为
            String moduleName = logOperation.module();
            String operation = logOperation.operation();

            //获取完整方法名、类名
            String methodName = joinPoint.getTarget().getClass().getName() + "." + method.getName();

            log.info("正在执行的方法为：{}", methodName);

            //获取请求参数
            Object[] args = joinPoint.getArgs();
            String requestParams = JSON.toJSONString(
                    Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> !(arg instanceof HttpServletRequest))
                    .filter(arg -> !(arg instanceof HttpServletResponse))
                    .toArray());

            //获取响应结果
            String response = result != null ? JSON.toJSONString(result) : "";

            //获取异常信息
            String errorMsg = ex != null ? ex.getMessage() : "";

            //获取 HttpServletRequest，用于获取客户端 IP
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

            String ip = httpUtil.getIp(request);

            // 从请求头中获取用户信息
            String userId = "";
            String userName = "";

            if (request != null) {
                userId = request.getHeader("X-User-Id") != null ? request.getHeader("X-User-Id") : "";
                userName = request.getHeader("X-User-Name") != null ? request.getHeader("X-User-Name") : "";
            }

            SysLog sysLog = SysLog.builder()
                    .userId(userId)
                    .userName(userName)
                    .moduleName(moduleName)
                    .operation(operation)
                    .operationId(request != null ? request.getParameter("mainId") : null)
                    .requestParams(requestParams)
                    .responseResult(response)
                    .exceptionMsg(errorMsg)
                    .requestIp(ip)
                    .build();

            logMapper.insertLog(sysLog);
        } catch (Exception e) {
            log.error("日志记录异常,{}", e.getMessage(), e);
        }

    }

}
