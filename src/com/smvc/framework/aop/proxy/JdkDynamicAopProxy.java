package com.smvc.framework.aop.proxy;

import org.aopalliance.intercept.MethodInterceptor;

import com.smvc.framework.aop.AbstractAopProxy;
import com.smvc.framework.aop.AdvisedSupport;
import com.smvc.framework.aop.ReflectiveMethodInvocation;
import com.smvc.framework.aop.advice.BaseAdvice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于jdk的动态代理
 * 
 * @author yihua.huang@dianping.com
 */
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        super(advised);
    }

	@Override
	public Object getProxy() {
		return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getInterfaces(), this);
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		BaseAdvice methodInterceptor = advised.getAdvice();
		if (advised.getMethodMatcher() != null
				&& advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
			return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(),
					method, args));
		} else {
			return method.invoke(advised.getTargetSource().getTarget(), args);
		}
	}

}
