/**
 * @filename AutoPackBeanFactory.java
 * @createtime 2015年8月2日
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.ioc.factory;

import java.lang.reflect.Field;

import org.apache.log4j.LogManager;

import com.smvc.framework.exception.BeanInstantiationException;
import com.smvc.framework.ioc.bean.BeanDefinition;
import com.smvc.framework.ioc.bean.BeanReference;
import com.smvc.framework.ioc.bean.PropertyValue;
import com.smvc.framework.ioc.bean.PropertyValues;
import com.smvc.framework.util.BeanUtil;

/**
 * @author Big Martin
 *
 */
public final class AutoPackBeanFactory extends AbstractBeanFactory {
    /**
     * singleton
     */
    private static final AutoPackBeanFactory instance = new AutoPackBeanFactory();
    
    private AutoPackBeanFactory(){}
    
    @Override
    public Object createBean(BeanDefinition beanDefinition) throws BeanInstantiationException {
        Object instance = instanceBean(beanDefinition);
        setProperty(instance, beanDefinition);
        
        return instance;
    }

    private void setProperty(Object instance, BeanDefinition beanDefinition) {
        PropertyValues properties = beanDefinition.getPropertyValues();
        
        if (properties == null)
        {
            return;
        }
        
        
        Class<?> clazz = instance.getClass();

        // set fields' value
        for (PropertyValue property : properties.getPropertyValues()) {
            try {
                Field field = clazz.getDeclaredField(property.getName());
                
                //access forcibly
                field.setAccessible(true);
                
                Object value = property.getValue();
                
                //if reference to other bean, then call getbean recursively 
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getName());
                }
                
                field.set(instance, value);
                
            } catch (Exception e) {
                LogManager.getLogger(AutoPackBeanFactory.class)
                        .warn("Failed to set field [" + property.getName() + "], please check it.", e);
            }

        }
    }

    private Object instanceBean(BeanDefinition beanDefinition) throws BeanInstantiationException {
        return BeanUtil.instantiate(beanDefinition.getBeanClass());
    }
    
    public static AutoPackBeanFactory getFactory()
    {
        return instance;
    }

}
