package org.smvc.framework.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.smvc.framework.exception.BeanInstantiationException;
import org.smvc.framework.mvc.context.ApplicationContext;
import org.smvc.framework.mvc.context.ContextFactory;
import org.smvc.framework.mvc.handler.ClassMappingInfo;
import org.smvc.framework.mvc.handler.HandlerManager;
import org.smvc.framework.mvc.handler.HandlerMethodManager;
import org.smvc.framework.util.BeanUtil;
import org.smvc.framework.util.ClassUtil;
import org.smvc.framework.util.RequestUtil;

/**
 * Servlet implementation class DispatchServlet
 */
public class DispatchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * view preffix
     */
    private static String preffix;
    
    /**
     * view suffix
     */
    private static String suffix;
    
    /**
     * smvc application context
     */
    private static ApplicationContext context;

    /**
     * logger
     */
    public static final Logger logger = Logger.getLogger(DispatchServlet.class);
    
    public DispatchServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            //init context 
            context = ContextFactory.getApplicationContext(getServletContext());
            if (context == null)
            {
                logger.error("Failed to load Application context.Failed to startup.");
                return;
            }
            
            //init view config
            preffix = context.getViewPreffix();
            suffix = context.getViewSuffix();
            
            //init all user controller
            //get all controller class
            Set<Class<?>> classes = ClassUtil.getClasses(context.getScanPackage());
            
            //check whether config scan path
            if (classes == null 
                    || classes.isEmpty())
            {
                logger.warn("There is no scan path!");
            }
            
            for (Class clazz : classes)
            {
                HandlerManager.getInstance().registHandler(clazz);
            
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestUrl = RequestUtil.getRealRequestURI(request);

        // get request bean，total match
        // regex match later
        ClassMappingInfo classMappingInfo = HandlerManager.getInstance().getHandler(request);

        //404 no handler mapped
        if (classMappingInfo == null) {
            Logger.getLogger(this.getClass()).error("request uri [" + requestUrl + "] did not match any controller.");
            response.sendError(404);
            return;
        }
        
        Map<String, String[]> params = RequestUtil.getRequestParamMapping(request);
        Method targetMethod = HandlerMethodManager.getInstance().getHandlerMethod(classMappingInfo, request);
        
//        //iterate all declared methods
//        for (Method method : methods)
//        {
//            //find method key 
//            if (params.containsKey(METHOD_REQUEST_MAPPING_KEY))
//            {
//                String[] paramValues = params.get(METHOD_REQUEST_MAPPING_KEY);
//                //check whether value is right
//                for (String value : paramValues)
//                {
//                    //if value equal, find it
//                    if (METHOD_REQUEST_MAPPING_VALUE.equals(value))
//                    {
//                        targetMethod = method;
//                    }
//                }
//            }
//        }
        
        if (targetMethod == null)
        {
            Logger.getLogger(this.getClass()).error("request uri [" + requestUrl + "] did not match any controller.");
            response.sendError(404);
            
            return;
        }
        
        ///TODO do pre intercept
        
        //if find target method, then do invoke.
        try {
            ParamModel paramModel = BeanUtil.getMethodParams(targetMethod, request, response);
            Object[] args = paramModel.getParams();
            Object instance = BeanUtil.instantiate(classMappingInfo.getClazz());
            Object result = targetMethod.invoke(instance, args);

            //set user controller's result
            if (paramModel.getResultMapIndex() != ParamModel.NO_RESULT_MAP)
            {
                try {
                    Map<Object, Object> resultMap = (Map<Object, Object>) args[paramModel.getResultMapIndex()];
                    Set<Object> resultIterator = resultMap.keySet();
                    for (Object key : resultIterator)
                    {
                        request.setAttribute(key.toString(), resultMap.get(key));
                    }
                } catch (ClassCastException ex) {
                    logger.warn("Can't cast user resultmap to Map<Object, Object>.Please Check method["
                            + targetMethod.toGenericString() + "].");
                }
            }
            
            //dispatch to page
            if (result instanceof String) {
                String resultStr = (String) result;
                String resultPage = preffix + resultStr + suffix;

                // dispatch request
                request.getRequestDispatcher(resultPage).forward(request, response);
            }

        } catch (BeanInstantiationException e) {
            e.printStackTrace();
        }  catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    
    private void pageNotFound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(request, response);
    }

}
