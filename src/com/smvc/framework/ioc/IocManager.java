/**
 * @filename IocManager.java
 * @createtime 2015年8月5日 下午11:46:46
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.ioc;

import java.lang.reflect.Field;

import com.smvc.framework.ioc.annotation.Bean;
import com.smvc.framework.ioc.annotation.Inject;
import com.smvc.framework.ioc.bean.BeanDefinition;
import com.smvc.framework.ioc.bean.BeanReference;
import com.smvc.framework.ioc.bean.PropertyValue;
import com.smvc.framework.ioc.bean.PropertyValues;
import com.smvc.framework.ioc.factory.AutoPackBeanFactory;
import com.smvc.framework.ioc.factory.IBeanFactory;
import com.smvc.framework.util.AnnotationUtil;
import com.smvc.framework.util.StringUtil;

/**
 * A IocManager can handle annotations IOC related to load IOC configuration.
 * @author Big Martin
 * 
 */
public class IocManager {
    private static IocManager instance = new IocManager();
    
    private IocManager(){}
    
    private static IBeanFactory factory = AutoPackBeanFactory.getFactory();
    /**
     * Register a bean to ioc container
     * @param clazz
     */
    public void registBean(Class<?> clazz)
    {   
        //if have been registered
        if (getBean(clazz) != null)
        {
            return;
        }
        
        //find @controller annotation
        Bean bean = AnnotationUtil.findAnnotation(clazz, Bean.class);
        
        //if a bean
        if (bean != null)
        {           
            //get properties
            //iterate all fields
            Field[] fields = clazz.getDeclaredFields();
            Inject inject;
            PropertyValues properties = new PropertyValues();
            
            for (Field field : fields)
            {
                inject = AnnotationUtil.findAnnotation(field, Inject.class);
                
                if (inject != null)
                {
                    String injectValue = inject.value();
                    Class<?> injectClazz = inject.clazz();
                    if (!StringUtil.isEmpty(injectValue)) {
                        properties.addPropertyValue(new PropertyValue(field.getName(), injectValue));
                    } else if (injectClazz != Inject.class) {
                        registBean(injectClazz);
                        properties.addPropertyValue(
                                new PropertyValue(field.getName(), new BeanReference(injectClazz.getName())));
                    }
                }
            }
            
            BeanDefinition definition = new BeanDefinition(clazz, properties);
            
            factory.registBean(clazz.getName(), definition);
        }
    }
    
    /**
     * Get a bean with class
     * @param clazz
     * @return
     */
    public <T> T getBean(Class<T> clazz)
    {
        Object object = factory.getBean(clazz.getName());
        if (object == null)
        {
            return null;
        }
        
        return (T) object;
    }
    
    public static IocManager getManager()
    {
        return instance;
    }
}
