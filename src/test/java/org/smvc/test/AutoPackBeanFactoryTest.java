package org.smvc.test;

import org.junit.Assert;
import org.junit.Test;
import org.smvc.framework.ioc.bean.BeanDefinition;
import org.smvc.framework.ioc.bean.BeanReference;
import org.smvc.framework.ioc.bean.PropertyValue;
import org.smvc.framework.ioc.bean.PropertyValues;
import org.smvc.framework.ioc.factory.AutoPackBeanFactory;
import org.smvc.framework.ioc.factory.IBeanFactory;

public class AutoPackBeanFactoryTest {
    @Test
    public void testSetCommonVal()
    {
        IBeanFactory factory = AutoPackBeanFactory.getFactory();
        PropertyValues properties = new PropertyValues();
        properties.addPropertyValue(new PropertyValue("text", "hello ioc!"));
        BeanDefinition definition = new BeanDefinition(HelloWorldServiceImpl.class, properties);
        
        factory.registBean(HelloWorldServiceImpl.class.getSimpleName(), definition);
        
        HelloWorldServiceImpl helloWorldService = (HelloWorldServiceImpl) factory.getBean(HelloWorldServiceImpl.class.getSimpleName());
        Assert.assertNotNull(helloWorldService);
        Assert.assertSame("hello ioc!", helloWorldService.getText());
        
    }
    
    @Test
    public void testSetRefVal()
    {
        IBeanFactory factory = AutoPackBeanFactory.getFactory();
        BeanDefinition definition = new BeanDefinition(OutputServiceImpl.class, null);
        
        factory.registBean(OutputServiceImpl.class.getSimpleName(), definition);
        
        PropertyValues properties2 = new PropertyValues();
        properties2.addPropertyValue(new PropertyValue("text", "hello ioc!"));
        properties2.addPropertyValue(new PropertyValue("outputService", new BeanReference(OutputServiceImpl.class.getSimpleName())));
        BeanDefinition definition2 = new BeanDefinition(HelloWorldServiceImpl.class, properties2);
        
        factory.registBean(HelloWorldServiceImpl.class.getSimpleName(), definition2);
        
        HelloWorldServiceImpl helloWorldService = (HelloWorldServiceImpl) factory.getBean(HelloWorldServiceImpl.class.getSimpleName());
        Assert.assertNotNull(helloWorldService);
        Assert.assertSame("hello ioc!", helloWorldService.getText());
        helloWorldService.helloWorld();
        
    }  
}
