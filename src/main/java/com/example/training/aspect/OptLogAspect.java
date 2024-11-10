package com.example.training.aspect;

import com.alibaba.fastjson.JSON;
import com.example.training.annotation.OptLog;
import com.example.training.dao.OperationLogDao;
import com.example.training.entity.OperationLog;
import com.example.training.util.IpUtils;
import com.example.training.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * operation log aspect implementation
 * @author Xinyuan Xu
 */
@Aspect
@Component
public class OptLogAspect {
    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * set the operation log entry point
     * record the operation log
     * enter the code at the annotation position
     */
    @Pointcut("@annotation(com.example.training.annotation.OptLog)")
    public void optLogPointCut() {}


    /**
     * return normal notifications
     * intercept user operation logs
     * execute after the connection point is executed normally
     * If the connection point throws an exception, it will not be executed.
     *
     * @param joinPoint entry point
     * @param keys      return value
     */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        // get RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // get HttpServletRequest message from RequestAttributes
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).
                resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // obtain the weaving point from the weaving point of the cut surface through the reflection mechanism
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // method to get the entry point
        Method method = signature.getMethod();
        // get operation
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        // operation module
        operationLog.setOptModule(api.tags()[0]);
        // operation type
        operationLog.setOptType(optLog.optType());
        // operation description
        operationLog.setOptDesc(apiOperation.value());
        // class name
        String className = joinPoint.getTarget().getClass().getName();
        // method name
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // request method
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        // request way
        operationLog.setOptMethod(methodName);
        // request param
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // return value
        operationLog.setResponseData(JSON.toJSONString(keys));
        // request UserID
        operationLog.setUserId(UserUtils.getLoginUser().getId());
        // request user
        operationLog.setNickname(UserUtils.getLoginUser().getNickname());
        // request IP
        String ipAddress = IpUtils.getIpAddress(request);
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(IpUtils.getIpSource(ipAddress));
        // request URL
        operationLog.setOptUrl(request.getRequestURI());
        operationLogDao.insert(operationLog);
    }
}
