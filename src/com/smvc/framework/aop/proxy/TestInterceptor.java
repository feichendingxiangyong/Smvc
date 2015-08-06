package com.smvc.framework.aop.proxy;

public class TestInterceptor {
    public static void before()
    {
        System.out.println("---------before-----------");
    }
    
    public static void after()
    {
        System.out.println("---------after-----------");
    }
}
