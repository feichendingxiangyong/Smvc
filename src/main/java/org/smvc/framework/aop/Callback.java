/**
 * @author Big Martin
 *
 */
package org.smvc.framework.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.smvc.framework.aop.annotation.Before;
import org.smvc.framework.aop.annotation.Clear;
import org.smvc.framework.aop.interceptor.Interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 主拦截器，所有的生成AOP代理时均使用该拦截器
 */
class Callback implements MethodInterceptor {
	
	private Object injectTarget = null;
	private final Interceptor[] injectInters;
	
	public static final Interceptor[] NULL_INTERS = new Interceptor[0];
	
	private static final Set<String> excludedMethodName = buildExcludedMethodName();
	
	public Callback() {
		this.injectInters = NULL_INTERS;
	}
	
	public Callback(Interceptor... injectInters) {
		if (injectInters == null)
			throw new IllegalArgumentException("injectInters can not be null.");
		this.injectInters = injectInters;
	}
	
	public Callback(Object injectTarget, Interceptor... injectInters) {
		if (injectTarget == null)
			throw new IllegalArgumentException("injectTarget can not be null.");
		if (injectInters == null)
			throw new IllegalArgumentException("injectInters can not be null.");
		this.injectTarget = injectTarget;
		this.injectInters = injectInters;
	}
	
	public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (excludedMethodName.contains(method.getName())) {
			if (method.getName().equals("finalize"))
				return methodProxy.invokeSuper(target, args);
			return this.injectTarget != null ? methodProxy.invoke(this.injectTarget, args) : methodProxy.invokeSuper(target, args);
		}
        // 这里是关键，这里并没有直接利用cglib的methodProxy.invokeSuper，
		// 而是利用了intercept函数的织入功能，在织入后，如何调用使用自己封装的interceptor
        
		//这里根据class、method设定的拦截器来决定拦截栈
		Set<Class<? extends Interceptor>> interceptorStack = new HashSet<Class<? extends Interceptor>>();
		Before classBefore = target.getClass().getAnnotation(Before.class);
		//加载类级别拦截器配置
		loadBeforeInterceptors(interceptorStack, classBefore);
		
		//加载方法级别拦截器配置
		Before methodBefore = method.getAnnotation(Before.class);
		loadBeforeInterceptors(interceptorStack, methodBefore);
		
		Clear methodClear = method.getAnnotation(Clear.class);
        if (methodClear != null)
        {
            Class<? extends Interceptor>[] clearIntercrptors = methodClear.value();
            
            if (clearIntercrptors != null)
            {
                for (Class<? extends Interceptor> item : clearIntercrptors)
                {
                    interceptorStack.remove(item);
                }
            }
        }
        
        Invocation invocation = new Invocation(target, method, args, methodProxy, getInterceptorInstance(interceptorStack));
        
        invocation.invoke();
        
        return invocation.getReturnValue();
	}

    private void loadBeforeInterceptors(Set<Class<? extends Interceptor>> interceptorStack, Before classBefore) {
        if (classBefore != null)
		{
		    Class<? extends Interceptor>[] classIntercrptors = classBefore.value();
		    
		    if (classIntercrptors != null)
		    {
		        Collections.addAll(interceptorStack, classIntercrptors);
		    }
		}
    }
	
	private final static Set<String> buildExcludedMethodName() {
		Set<String> excludedMethodName = new HashSet<String>();
		Method[] methods = Object.class.getDeclaredMethods();
		for (Method m : methods)
			excludedMethodName.add(m.getName());
		
		// getClass() registerNatives() can not be enhanced
		// excludedMethodName.remove("getClass");	
		// excludedMethodName.remove("registerNatives");
		return excludedMethodName;
	}
	
    public Interceptor[] getInterceptorInstance(Set<Class<? extends Interceptor>> interceptorStack)
    {
        Interceptor[] array = new Interceptor[interceptorStack.size()];
        
        Interceptor interceptor = null;
        int index = 0;
        
        //加载配置完成后，进行拦截器调用：如果传入的拦截器在拦截器栈中，则进行拦截
        for (Class<? extends Interceptor> interClazz : interceptorStack)
        {
            //判断用户是否提供了该拦截器实例
            interceptor = getInterceptorByClazz(interClazz);
            
            //否则使用默认实例
            if (interceptor == null)
            {
                try {
                    interceptor = interClazz.newInstance();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            if (interceptor != null)
            {
                array[index++] = interceptor;
            }
        }
        
        if (index < interceptorStack.size())
        {
            Interceptor[] arrayNew = Arrays.copyOf(array, index - 1);
            return arrayNew;
        }
        
        return array;
    }
    
    private Interceptor getInterceptorByClazz(Class<? extends Interceptor> clazz)
    {
        for (Interceptor inter : injectInters)
        {
            if (inter.getClass().equals(clazz))
            {
                return inter;
            }
        }
        
        return null;
    }
}


