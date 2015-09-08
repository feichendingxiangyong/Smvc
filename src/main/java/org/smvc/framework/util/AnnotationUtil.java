/**
 * @filename AnnotationUtil.java
 * @createtime 2015.7.12
 * @author Big Martin
 * @comment 
 */
package org.smvc.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author Big Martin
 *
 */
public class AnnotationUtil {
    
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        Assert.notNull(clazz, "Class must not be null");
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }
    
    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        Assert.notNull(method, "Method must not be null");
        
        A annotation = method.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        
        return null;
    }
    
    public static <A extends Annotation> A findAnnotation(Field field, Class<A> annotationType) {
        Assert.notNull(field, "Method must not be null");
        
        A annotation = field.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        
        return null;
    }
}
