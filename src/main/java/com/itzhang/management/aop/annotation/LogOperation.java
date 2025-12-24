package com.itzhang.management.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @param
 * @Description 日志记录注解，只能在方法上使用
 * @return null
 * @Author weiloong_zhang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogOperation {
    /**
     * @param
     * @return java.lang.String
     * @Description 模块名称，默认为空
     * @Author weiloong_zhang
     */
    String module() default "";

    /**
     * @param
     * @return java.lang.String
     * @Description 操作行为，默认为空
     * @Author weiloong_zhang
     */
    String operation() default "";
}
