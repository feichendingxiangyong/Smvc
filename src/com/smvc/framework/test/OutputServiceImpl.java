package com.smvc.framework.test;

import com.smvc.framework.ioc.annotation.Bean;

/**
 * @author Big Martin
 */
@Bean
public class OutputServiceImpl implements OutputService {

    @Override
    public void output(String text){
        System.out.println(text);
    }

}
