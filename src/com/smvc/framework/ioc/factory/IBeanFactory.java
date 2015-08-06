package com.smvc.framework.ioc.factory;

import com.smvc.framework.ioc.bean.BeanDefinition;

/**
 * bean的容器
 * @author dingxiangyong
 */
public interface IBeanFactory {

    /**
     * get bean from ICO container.This function use lazy loading.
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name);

    /**
     * regist a bean to factory
     * @param beanDefinition
     */
    void registBean(String name, BeanDefinition beanDefinition);
}
