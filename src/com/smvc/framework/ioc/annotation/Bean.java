/**
 * @filename Bean.java
 * @createtime 2015年8月5日
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Big Martin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Bean {

}
