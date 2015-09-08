/**
 * @filename HandlerManager.java
 * @createtime 2015年7月12日
 * @author dingxiangyong
 * @comment 
 */
package org.smvc.framework.mvc.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.smvc.framework.mvc.annotation.Controller;
import org.smvc.framework.mvc.annotation.RequestMapping;
import org.smvc.framework.util.AnnotationUtil;
import org.smvc.framework.util.RequestUtil;
import org.smvc.framework.util.StringUtil;


/**
 * @author Big Martin
 *
 */
public class HandlerManager {
    
    // singleton instance
    private static final HandlerManager INSTANCE = new HandlerManager();
    
    /**
     * Handler map
     */
    private static Map<MappingInfo, Class<?>> handlerMapping = new HashMap<MappingInfo, Class<?>>();
    
    private HandlerManager()
    {
        
    }
    
    /**
     * get instance
     * @return
     */
    public static HandlerManager getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Regist a handler to Smvc
     * @param clazz
     */
    public void registHandler(Class<?> clazz)
    {   
        //find @controller annotation
        Controller controller = AnnotationUtil.findAnnotation(clazz, Controller.class);
        
        //if it's a controller
        if (controller != null)
        {
            //find @requestmaping
            RequestMapping requestMapping = AnnotationUtil.findAnnotation(clazz, RequestMapping.class);
            
            MappingInfo mappingInfo;
            if (requestMapping != null)
            {
                //create MappingInfo
                mappingInfo = new MappingInfo(MappingType.CLASS, StringUtil.standardUrlPattern(requestMapping.value()), requestMapping.params());
                
            }
            else
            {
                mappingInfo = new MappingInfo(MappingType.CLASS, null, null);
            }
            handlerMapping.put(mappingInfo, clazz);
            
            //init method mapping
            HandlerMethodManager.getInstance().registHandlerMethod(clazz);
        }
    }
    
    /**
     * Find right handler with request uri and parameters.
     * @param request
     * @return If got nothing, then return null.
     */
    public ClassMappingInfo getHandler(HttpServletRequest request)
    {
        //get request info
        String realPath = RequestUtil.getRealRequestURIWithoutPrefix(request);
        List<String> params = RequestUtil.getRequestParamStrList(request);
        
        Set<MappingInfo> keys = handlerMapping.keySet();
        for (MappingInfo mappingInfo : keys)
        {
            //find right handler 
            if (mappingInfo.isMatch(realPath, params))
            {
                return new ClassMappingInfo(handlerMapping.get(mappingInfo), mappingInfo);
            }
        }
        
        return null;
    }
    
}
