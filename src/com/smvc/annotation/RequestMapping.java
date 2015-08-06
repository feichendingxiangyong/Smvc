/**
 * @filename Route.java
 * @time 2015.7.12
 * @author Big Martin
 * @comment 
 */
package com.smvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Big Martin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * The primary mapping expressed by this annotation.
     * <p>In a Servlet environment: the path mapping URIs (e.g. "/myPath.do").
     * Ant-style path patterns are also supported (e.g. "/myPath/*.do").
     * At the method level, relative paths (e.g. "edit.do") are supported
     * within the primary mapping expressed at the type level.
     * @return
     */
    String[] value() default {};
    
    /**
     * The parameters of the mapped request, narrowing the primary mapping.
     * @return
     */
    String[] params() default {};
}
