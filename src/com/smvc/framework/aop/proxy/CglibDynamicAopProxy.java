package com.smvc.framework.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import com.smvc.framework.aop.AbstractAopProxy;
import com.smvc.framework.aop.AdvisedSupport;
import com.smvc.framework.aop.ReflectiveMethodInvocation;

/**
 * @author yihua.huang@dianping.com
 */
public class CglibDynamicAopProxy extends AbstractAopProxy {

	public CglibDynamicAopProxy(AdvisedSupport advised) {
		super(advised);
	}

	@Override
    public Object getProxy() {
        Object enhanced = Enhancer.create(advised.getTargetSource().getTargetClass(),
                advised.getTargetSource().getInterfaces(), new DynamicAdvisedInterceptor(advised));

        return enhanced;
    }

	private static class DynamicAdvisedInterceptor implements MethodInterceptor {

		private AdvisedSupport advised;

		private org.aopalliance.intercept.MethodInterceptor delegateMethodInterceptor;

		private DynamicAdvisedInterceptor(AdvisedSupport advised) {
			this.advised = advised;
			this.delegateMethodInterceptor = advised.getAdvice();
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (advised.getMethodMatcher() == null
					|| advised.getMethodMatcher().matches(method, advised.getTargetSource().getTargetClass())) {
				return delegateMethodInterceptor.invoke(new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy));
			}
			return new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy).proceed();
		}
	}

	private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

		private final MethodProxy methodProxy;

		public CglibMethodInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy) {
			super(target, method, args);
			this.methodProxy = methodProxy;
		}

		@Override
		public Object proceed() throws Throwable {
			return this.methodProxy.invoke(this.target, this.arguments);
		}
	}

}
