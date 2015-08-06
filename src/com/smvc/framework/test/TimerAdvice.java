package com.smvc.framework.test;

import org.aopalliance.intercept.MethodInvocation;

import com.smvc.framework.aop.advice.MethodArroundAdvice;

public class TimerAdvice extends MethodArroundAdvice{

    @Override
    protected Object doArround(MethodInvocation methodInvocation) throws Throwable{
        long time = System.nanoTime();
        System.out.println("Invocation of Method " + methodInvocation.getMethod().getName() + " start!");
        Object proceed = methodInvocation.proceed();
        System.out.println("Invocation of Method " + methodInvocation.getMethod().getName() + " end! takes " + (System.nanoTime() - time)
                + " nanoseconds.");
        return proceed;
    }

}
