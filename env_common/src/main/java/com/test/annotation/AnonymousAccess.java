package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description:用于标记匿名访问方法
 *  不携带token即可访问
 *
 * @author RenShiWei
 * Date: 2020/7/12 21:58
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {
}
