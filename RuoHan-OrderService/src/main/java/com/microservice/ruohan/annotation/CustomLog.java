package com.microservice.ruohan.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface CustomLog {
    String name() default "";

    boolean isSaveRequestData() default true;
}
