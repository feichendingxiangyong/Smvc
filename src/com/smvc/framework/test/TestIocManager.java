/**
 * @filename TestIocManager.java
 * @createtime 2015年8月6日 上午00:48:59
 * @author dingxiangyong
 * @comment 
 */
package com.smvc.framework.test;

import org.junit.Assert;
import org.junit.Test;

import com.smvc.framework.ioc.IocManager;

/**
 * @author Big Martin
 *
 */
public class TestIocManager {
    @Test
    public void testRegisterBean()
    {
        IocManager.getManager().registBean(TestService.class);
        TestService service = IocManager.getManager().getBean(TestService.class);
        Assert.assertEquals("Hello IOC", service.printHello());
    }
}
