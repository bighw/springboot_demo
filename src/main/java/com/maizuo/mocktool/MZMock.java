package com.maizuo.mocktool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiyang
 * @ClassName: MZMock
 * @Description: 注解，只允许本地在开发阶段使用，上线一律删除mock注解
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MZMock {
    String value() default "";
}
