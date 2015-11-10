package org.smvc.test;

import org.smvc.framework.ioc.annotation.Bean;

/**
 * @author Big Martin
 */
@Bean
public class OutputServiceImpl implements OutputService {

    public void output(String text){
        System.out.println(text);
    }

}
