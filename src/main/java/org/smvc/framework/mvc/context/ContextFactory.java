/**
 * @filename ContextFactory.java
 * @createtime 2015年7月22日
 * @author dingxiangyong
 * @comment 
 */
package org.smvc.framework.mvc.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.smvc.framework.mvc.DispatchServlet;
import org.smvc.framework.util.StringUtil;
import org.smvc.framework.util.XMLParser;

/**
 * @author Big Martin
 *
 */
public class ContextFactory {
    private static final String DEFAULT_CONTEXT_PATH = "/WEB-INF/smvc.xml";
    
    /**
     * application context
     */
    private static ApplicationContext context;
    
    private static Map<String, String> nodeName2FieldMap = new HashMap<String, String>();
    
    /**
     * logger
     */
    public static final Logger logger = Logger.getLogger(DispatchServlet.class);
    
    static{
        nodeName2FieldMap.put("smvc-config", "ApplicationContext");
        nodeName2FieldMap.put("controller-package", "scanPackage");
        nodeName2FieldMap.put("view-preffix", "viewPreffix");
        nodeName2FieldMap.put("view-suffix", "viewSuffix");
    }
    
    /**
     * load configuration 
     * @return
     */
    public static ApplicationContext getApplicationContext(ServletContext servletContext)
    {
        return getApplicationContext(servletContext, null, true);
    }
    
    /**
     * load configuration 
     * @param contextFilePath
     * @return
     */
    public static ApplicationContext getApplicationContext(ServletContext servletContext, String contextFilePath, boolean isNeedReload)
    {
        //check whether cached 
        if (context != null
                && !isNeedReload)
        {
            return context;
        }
        
        if (StringUtil.isEmpty(contextFilePath))
        {
            contextFilePath = servletContext.getRealPath(DEFAULT_CONTEXT_PATH);
        }
        
        context = XMLParser.parseXml2Object(contextFilePath, ApplicationContext.class, nodeName2FieldMap);
        
        if (context == null)
        {
            logger.error("Failed to load context, context path is : " + contextFilePath);
        }
        
        return context;
    }
}
