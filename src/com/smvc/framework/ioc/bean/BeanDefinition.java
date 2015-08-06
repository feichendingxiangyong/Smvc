package com.smvc.framework.ioc.bean;

/**
 * To save bean ioc config , and bean instance created.
 * 
 * @author Big Martin
 */
public class BeanDefinition {

	private Object bean;

	private Class<?> beanClass;

	private String beanClassName;

	private PropertyValues propertyValues = new PropertyValues();

	public BeanDefinition() {
	}
	
	public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        super();
        this.beanClass = beanClass;
        this.beanClassName = beanClass.getSimpleName();
        this.propertyValues = propertyValues;
    }


    public void setBean(Object bean) {
		this.bean = bean;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object getBean() {
		return bean;
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
}
