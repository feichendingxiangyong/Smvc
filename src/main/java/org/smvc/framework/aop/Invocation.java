/**
 * @author Big Martin
 *
 */
package org.smvc.framework.aop;

import java.lang.reflect.Method;

import org.smvc.framework.aop.interceptor.Interceptor;

import net.sf.cglib.proxy.MethodProxy;

public class Invocation {
    
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private Interceptor[] inters;
    private Object returnValue = null;
    private int invokeIndex = 0;
    
    public Invocation(Object target, Method method, Object[] args, MethodProxy methodProxy, Interceptor[] inters) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.inters = inters;
    }
    
    /**
     * This method will be called recursively by intercepters. 
     * @return
     */
    public void invoke()
    {
        if (invokeIndex < inters.length)
        {
            inters[invokeIndex++].intercept(this);
        }
        else
        {
            try {
                returnValue = methodProxy.invokeSuper(target, args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public void setMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    public Interceptor[] getInters() {
        return inters;
    }

    public void setInters(Interceptor[] inters) {
        this.inters = inters;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
    
}
