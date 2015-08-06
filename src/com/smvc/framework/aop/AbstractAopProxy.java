package com.smvc.framework.aop;

/**
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractAopProxy implements AopProxy {

    protected AdvisedSupport advised;

    public AbstractAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
}
