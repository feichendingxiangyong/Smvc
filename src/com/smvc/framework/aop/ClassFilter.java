package com.smvc.framework.aop;

/**
 * @author Big Martin
 */
public interface ClassFilter {

    boolean matches(Class targetClass);
}
