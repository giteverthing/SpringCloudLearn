package com.microservice.ruohan.aspect;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.ruohan.annotation.CustomLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class CustomLogAspect {

    @Pointcut("@annotation(com.microservice.ruohan.annotation.CustomLog)")
    public void pointCut() {

    }

//    @Before("pointCut()")
//    public void doLog(JoinPoint joinPoint) {
//        System.out.println("...before....");
//        handleLog(joinPoint, null);
//    }

    @Around("pointCut()")
    public Object doLogAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        System.out.println("...around before....");
        handleLog(joinPoint, null);
        Object[] args = joinPoint.getArgs();
        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable throwable) {
//            throwable.printStackTrace();
        }
        System.out.println("...around after....");

        return obj;
    }

    private void handleLog(final JoinPoint joinPoint, final Exception o) {
        CustomLog customLog = getAnnotationLog(joinPoint);
        if (customLog == null) {
            return;
        }
        //通过jointPoint获取类信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        System.out.print(className + "." + methodName);
        System.out.print(",url=" + ((HttpServletRequest) getRequest()).getRequestURL().toString());
        System.out.print(",name=" + customLog.name());
        //获取参数
        String paramMap = getMethodDescription(customLog);
        System.out.println(",parameters:" + paramMap);

    }

    private String getMethodDescription(CustomLog customLog) {
        String params = "";
        if (customLog.isSaveRequestData()) {
            params = getRequestValues();
        }

        return params;
    }

    private String getRequestValues() {
        Map<String, String[]> map = getRequest().getParameterMap();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            return "";
        }
    }

    private ServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private CustomLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(CustomLog.class);
        }

        return null;
    }

}
