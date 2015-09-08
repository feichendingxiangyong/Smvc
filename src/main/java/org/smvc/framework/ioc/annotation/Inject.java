/**
 * @filename Inject.java
 * @createtime 2015年8月5日 下午11:19:48
 * @author dingxiangyong
 * @comment 
 */
package org.smvc.framework.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Big Martin
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    public String value() default "";
    public Class<?> clazz() default Inject.class;
}
