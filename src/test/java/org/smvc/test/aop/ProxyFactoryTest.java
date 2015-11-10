package org.smvc.test.aop;

import org.junit.Test;
import org.smvc.framework.aop.ProxyFactory;
import org.smvc.framework.aop.interceptor.Interceptor;

public class ProxyFactoryTest {
    
    @Test
    public void doTest() {  
        //haveAuth(); 
        haveNoAuth();
    }  
    public void doMethod(TableDao dao){  
        dao.create();  
        dao.query();  
//        dao.update();  
//        dao.delete();  
    }  
    //模拟有权限
    public void haveAuth(){  
        TableDao tDao = ProxyFactory.getInstance(TableDao.class, new Interceptor[]{new AuthInterceptor("张三"), new LogInterceptor()});  
        doMethod(tDao);  
    }  
    //模拟无权限
    public void haveNoAuth(){  
        TableDao tDao = ProxyFactory.getInstance(TableDao.class, new Interceptor[]{new AuthInterceptor("李四"), new LogInterceptor()});  
        doMethod(tDao);  
    }
}