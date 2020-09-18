package com.microservice.ruohan.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface CustomCacheable {
    String key() default "__defaultCacheKey";

    String value() default "__defaultCacheValue";

    int expire() default -1;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
