/**
 * @filename Before.java
 * @createtime 2015年8月9日 下午9:35:23
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.aop.annotation;

/**
 * @author Big Martin
 *
 */
public @interface Before {
    Class<?>[] clazz();
}
