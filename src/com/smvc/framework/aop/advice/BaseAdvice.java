/**
 * @filename BaseAdvice.java
 * @createtime 2015.8.2
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.aop.advice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * @author Big Martin
 *
 */
public abstract class BaseAdvice implements MethodInterceptor {
    static Logger logger = Logger.getLogger(BaseAdvice.class);
    
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //get all useful message
        Method method = methodInvocation.getMethod();
        Object[] args = methodInvocation.getArguments();
        Object result = null;
        try{
            doBefore(method, args);
            result = doArround(methodInvocation);
        }catch(Throwable ex)
        {
            logger.warn("Base advice got an exception.", ex);
            doWhenThrowExcp(method, args);
        }finally {
            doAfter(method, args, result);
        }
        
        return null;
    }

    protected Object doArround(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        result = methodInvocation.proceed();
        return result;
    }
    
    /**
     * Do something before method invoking.
     * @param method
     * @param args
     */
    protected abstract void doBefore(Method method, Object[] args) throws Throwable;
    
    /**
     * Do something after method invoked.
     * Note: if method return nothing, the resultVal will be null.
     * @param method
     * @param args
     * @param resutlVal 
     */
    protected abstract void doAfter(Method method, Object[] args, Object resutlVal);
    
    /**
     * Do something when throw exceptions.
     * @param method
     * @param args
     * @param resutlVal
     */
    protected abstract void doWhenThrowExcp(Method method, Object[] args);
    

}
