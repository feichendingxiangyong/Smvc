package com.smvc.framework.aop;

import com.smvc.framework.aop.advice.BaseAdvice;

/**
 * 代理相关的元数据
 * @author Big Martin
 */
public class AdvisedSupport {
    /**
     * 被代理对象
     */
	private TargetSource targetSource;

	/**
	 * 通知
	 */
    private BaseAdvice advice;

    /**
     * 方法匹配器
     */
    private MethodMatcher methodMatcher;

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public BaseAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(BaseAdvice advice) {
        this.advice = advice;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
