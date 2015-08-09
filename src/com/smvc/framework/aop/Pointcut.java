package com.smvc.framework.aop;

/**
 * @author Big Martin
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
