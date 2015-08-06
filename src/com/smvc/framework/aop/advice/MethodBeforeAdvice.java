/**
 * @filename MethodBeforeAdvice.java
 * @createtime 2015年8月2日
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.aop.advice;

import java.lang.reflect.Method;

/**
 * @author Big Martin
 *
 */
public abstract class MethodBeforeAdvice extends BaseAdvice{
    /**
     * Do something after method invoked.
     * Note: if method return nothing, the resultVal will be null.
     * @param method
     * @param args
     * @param resutlVal 
     */
    protected void doAfter(Method method, Object[] args, Object resutlVal)
    {
        return;
    }
    
    /**
     * Do something when throw exceptions.
     * @param method
     * @param args
     * @param resutlVal
     */
    protected void doWhenThrowExcp(Method method, Object[] args)
    {
        return;
    }
}
