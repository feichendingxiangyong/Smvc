package com.smvc.framework.aop.proxy;
import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
import java.lang.reflect.Proxy;  
public class JdkDynamicProxy implements InvocationHandler {  
    /**
     * 被代理的对象
     */
    private Object delegate;  
    
    /** 
     * 绑定委托对象并返回一个代理类 
     * @param target 
     * @return 
     */  
    public Object bind(Object target) {  
        this.delegate = target;  
        //取得代理对象  
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),  
                target.getClass().getInterfaces(), this);   //要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)  
    }  
  
    @Override  
    /** 
     * 调用方法 
     */  
    public Object invoke(Object proxy, Method method, Object[] args)  
            throws Throwable {  
        Object result=null;  
        TestInterceptor.before();
        //执行方法  
        result=method.invoke(delegate, args);  
        TestInterceptor.after(); 
        return result;  
    }  
  
}  
