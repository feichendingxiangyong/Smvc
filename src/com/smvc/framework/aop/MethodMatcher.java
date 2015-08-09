package com.smvc.framework.aop;

import java.lang.reflect.Method;

/**
 * @author Big Martin
 */
public interface MethodMatcher {

    boolean matches(Method method, Class targetClass);
}
