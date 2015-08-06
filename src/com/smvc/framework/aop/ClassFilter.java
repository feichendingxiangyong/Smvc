package com.smvc.framework.aop;

/**
 * @author yihua.huang@dianping.com
 */
public interface ClassFilter {

    boolean matches(Class targetClass);
}
