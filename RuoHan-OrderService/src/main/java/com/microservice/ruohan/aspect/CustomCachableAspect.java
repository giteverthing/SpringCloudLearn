package com.microservice.ruohan.aspect;

import com.microservice.ruohan.annotation.CustomCacheable;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


@Aspect
@Order(1)
@Component
public class CustomCachableAspect {
    private static ReentrantLock lock;

    static {
        lock = new ReentrantLock();
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut(value = "@annotation(customCacheable)")//com.microservice.ruohan.annotation.CustomCacheable
    public void pointCut(CustomCacheable customCacheable) {

    }

    @Around("pointCut(customCacheable)")
    public Object process(ProceedingJoinPoint joinPoint, CustomCacheable customCacheable) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        CustomCacheable customCacheable = methodSignature.getMethod().getAnnotation(CustomCacheable.class);
        String cacheKey = "";
        Object cacheObject = null;

        if (customCacheable != null) {
            String realKey = customCacheable.key();
            cacheKey = customCacheable.value() + "::" + realKey;
            cacheObject = redisTemplate.opsForValue().get(cacheKey);

            if (cacheObject == null) {
                try {
                    lock.tryLock(500, TimeUnit.MILLISECONDS);
                    cacheObject = joinPoint.proceed(joinPoint.getArgs());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                redisTemplate.opsForValue().set(cacheKey, cacheObject, customCacheable.expire(), customCacheable.timeUnit());
            }
            lock.unlock();
        }

        return cacheObject;
    }

    /**
     * 获取缓存的key
     * key 定义在注解上，支持SPEL表达式
     *
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {

        if (StringUtils.isEmpty(key)) return null;

        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // 创建计算上下文的根对象
        //SPEL上下文
        EvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
