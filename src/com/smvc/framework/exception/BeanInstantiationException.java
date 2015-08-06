package com.smvc.framework.exception;

public class 
BeanInstantiationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9213782382919562088L;
	
	/**
	 * class
	 */
	private Class beanClass;
	
	public BeanInstantiationException(Class beanClass, String msg)
	{
		super("Cannot instant [" + beanClass.getName() + "], here is error msg : " + msg);
		this.beanClass = beanClass;
	}

	public BeanInstantiationException(Class beanClass, String msg, Throwable ex) {
		super("Cannot instant [" + beanClass.getName() + "], here is error msg : " + msg, ex);
		this.beanClass = beanClass;
	}

	@Override
	public String toString() {
		return "BeanInstantiationException [beanClass=" + beanClass + "], " + this.getMessage();
	}
	
	
}
