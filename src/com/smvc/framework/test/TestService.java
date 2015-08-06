package com.smvc.framework.test;

import com.smvc.framework.ioc.annotation.Bean;
import com.smvc.framework.ioc.annotation.Inject;

@Bean
public class TestService {
    @Inject(value = "Hello IOC")
    private String text;
    @Inject(clazz = OutputServiceImpl.class)
    private OutputService service;
    
    public String printHello()
    {
        service.output(text);
        return text;
    }
}
