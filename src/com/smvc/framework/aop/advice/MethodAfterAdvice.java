/**
 * @filename MethodAfterAdvice.java
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
public abstract class MethodAfterAdvice extends BaseAdvice{
    /**
     * Do something before method invoking.
     * @param method
     * @param args
     */
    protected void doBefore(Method method, Object[] args)
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
