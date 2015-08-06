package com.smvc.framework.test;

import org.junit.Test;

import com.smvc.framework.aop.AdvisedSupport;
import com.smvc.framework.aop.AspectJExpressionPointcut;
import com.smvc.framework.aop.TargetSource;
import com.smvc.framework.aop.proxy.JdkDynamicAopProxy;

/**
 * @author yihua.huang@dianping.com
 */
public class JdkDynamicAopProxyTest {
    @Test
	public void testInterceptor() throws Exception {
		// --------- helloWorldService without AOP
        HelloWorldServiceImpl helloWorldService = new HelloWorldServiceImpl();
	    helloWorldService.setText("hello world");
	    helloWorldService.setOutputService(new OutputServiceImpl());
		// --------- helloWorldService with AOP
		// 1. 设置被代理对象(Joinpoint)
		AdvisedSupport advisedSupport = new AdvisedSupport();
		TargetSource targetSource = new TargetSource(helloWorldService, HelloWorldServiceImpl.class,
				new Class<?>[] {HelloWorldService.class});
		advisedSupport.setTargetSource(targetSource);

		// 2. 设置拦截器(Advice)
		//TimerInterceptor timerInterceptor = new TimerInterceptor();
		TimerAdvice timerAdvice = new TimerAdvice();
		advisedSupport.setAdvice(timerAdvice);
		AspectJExpressionPointcut matcher = new AspectJExpressionPointcut();
		matcher.setExpression("execution(* com.smvc.test.*.*(..))");
		advisedSupport.setMethodMatcher(matcher);

		// 3. 创建代理(Proxy)
		JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
		HelloWorldService helloWorldServiceProxy = (HelloWorldService) jdkDynamicAopProxy.getProxy();

		// 4. 基于AOP的调用
		helloWorldServiceProxy.helloWorld();

	}
}
