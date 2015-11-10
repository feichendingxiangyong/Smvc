/**
 * @filename TestIocManager.java
 * @createtime 2015年8月6日 上午00:48:59
 * @author dingxiangyong
 * @comment 
 */
package org.smvc.test;

import org.junit.Assert;
import org.junit.Test;
import org.smvc.framework.ioc.IocManager;

/**
 * @author Big Martin
 *
 */
public class IocManagerTest {

    @Test
    public void testNotRegistBean()
    {
        OutputService outputService = IocManager.getManager().getBean(OutputServiceImpl.class);
        Assert.assertNull(outputService);
    }
    
    @Test
    public void testRegisterBean()
    {
        IocManager.getManager().registBean(TestService.class);
        TestService service = IocManager.getManager().getBean(TestService.class);
        Assert.assertEquals("Hello IOC", service.printHello());
    }
}
