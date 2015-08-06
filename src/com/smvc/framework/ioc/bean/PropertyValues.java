package com.smvc.framework.ioc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * To save a bean's properties.
 */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

	public PropertyValues() {
	}

	public void addPropertyValue(PropertyValue pv) {
		this.propertyValueList.add(pv);
	}

	public List<PropertyValue> getPropertyValues() {
		return this.propertyValueList;
	}

}
