/**
 * @filename HandlerMethodManager.java
 * @createtime 2015.7.12
 * @author Big Martin
 * @comment 
 */
package org.smvc.framework.mvc.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.smvc.framework.mvc.annotation.RequestMapping;
import org.smvc.framework.util.AnnotationUtil;
import org.smvc.framework.util.Assert;
import org.smvc.framework.util.PathUtil;
import org.smvc.framework.util.RequestUtil;

/**
 * @author Big Martin
 *
 */
public class HandlerMethodManager {
    // singleton instance
    private static final HandlerMethodManager INSTANCE = new HandlerMethodManager();
    
    /**
     * Handler's method map
     */
    private static Map<Class<?>, Map<Method, MappingInfo>> handlerMethodMapping = new HashMap<Class<?>, Map<Method, MappingInfo>>();
    
    private HandlerMethodManager()
    {
        
    }
    
    /**
     * get instance
     * @return
     */
    public static HandlerMethodManager getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Regist a handler to Smvc
     * @param clazz
     */
    public void registHandlerMethod(Class<?> clazz)
    {
        Assert.notNull(clazz, "Class cannot be null.");
        
        // result
        Map<Method, MappingInfo> results = new HashMap<Method, MappingInfo>();
        
        // scan all methods
        Method[] methods = clazz.getMethods();
        for (Method method : methods)
        {
            //find @requestmaping
            RequestMapping requestMapping = AnnotationUtil.findAnnotation(method, RequestMapping.class);
            
            MappingInfo mappingInfo;
            if (requestMapping != null)
            {
                //create MappingInfo
                mappingInfo = new MappingInfo(MappingType.METHOD, requestMapping.value(), requestMapping.params());
                results.put(method, mappingInfo);
            }
            
        }
        
        // add result to handlerMethodMapping
        if (results.size() != 0)
        {
            handlerMethodMapping.put(clazz, results);
        }
    }
    
    /**
     * get right method of class with looking path
     * @param classMappingInfo
     * @return
     */
    public Method getHandlerMethod(ClassMappingInfo classMappingInfo, HttpServletRequest request)
    {
        Assert.notNull(classMappingInfo);
        
        Class<?> clazz = classMappingInfo.getClazz();
        MappingInfo mappingInfo = classMappingInfo.getMappingInfo();
        String lookPath = RequestUtil.getRealRequestURIWithoutPrefix(request);
        
        //iterate all class patterns
        if (mappingInfo != null)
        {
            Map<Method, MappingInfo> methodMappingInfos = handlerMethodMapping.get(clazz);
            Set<Method> methods = methodMappingInfos.keySet();

            MappingInfo methodMappingInfo = null;
            //iterate all method of class 
            for (Method method : methods)
            {
                methodMappingInfo = methodMappingInfos.get(method);
                if (methodMappingInfo != null)
                {
                    //iterate all class patterns
                    for (String classPattern : mappingInfo.getPatterns())
                    {
                        if (methodMappingInfo.getPatterns().length == 0)
                        {
                            return method;
                        }
                        for (String methodPattern : methodMappingInfo.getPatterns())
                        {
                            String combinedPath = PathUtil.getInstance().combine2(classPattern, methodPattern);
                            if (PathUtil.getInstance().matchStrings(combinedPath, lookPath))
                            {
                                return method;
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
}
