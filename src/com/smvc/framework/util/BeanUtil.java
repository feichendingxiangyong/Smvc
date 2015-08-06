package com.smvc.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.smvc.framework.exception.BeanInstantiationException;
import com.smvc.framework.mvc.ParamModel;

public class BeanUtil {
    /**
     * all java declared object types
     */
    private static final List<String> OTHER_OBJECT_TYPE_LIST = new ArrayList<String>();
    
    /**
     * user's data to add to request for return
     */
    private static final String RESULT_TYPE = "java.util.Map";
    
    /**
     * http servlet request
     */
    private static final String REQUEST_TYPE = "javax.servlet.http.HttpServletRequest";
    
    /**
     * http servlet response
     */
    private static final String RESPONSE_TYPE = "javax.servlet.http.HttpServletResponse";
    
    private static Logger logger = LogManager.getLogger(BeanUtil.class);
    
    private static Map<Class<?>, Object> cachedInstance = new HashMap<Class<?>, Object>();
    
    //init java inner object type
    static
    {
        OTHER_OBJECT_TYPE_LIST.add("java.lang.String");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Boolean");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Byte");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Character");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Double");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Integer");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Float");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Long");
        OTHER_OBJECT_TYPE_LIST.add("java.lang.Byte");
    }
    
	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
		// if cached , return
	    if (cachedInstance.containsKey(clazz))
		{
		    return (T) cachedInstance.get(clazz);
		}
	    
	    // if not cached, then new it
	    if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
		    T instance = clazz.newInstance();
		    cachedInstance.put(clazz, instance);
			return instance;
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
		}
	}

	/**
	 * get all defined fields' of a class
	 * @param clazz
	 * @return
	 * @throws BeanInstantiationException
	 */
    public static <T> Field[] getClassFieldNames(Class<T> clazz) throws BeanInstantiationException {
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "Specified class is an interface");
        }

        return clazz.getDeclaredFields();
    }
	
    /**
     * get method params for invoking
     * @param method method
     * @param request servlet request
     * @return ParamModel
     */
	public static ParamModel getMethodParams(Method method, HttpServletRequest request, HttpServletResponse response)
	{
	    ParamModel paramModel = new ParamModel();
	    
	    // get all param types
	    Parameter[] params = method.getParameters();
	    
	    String[] paramNames = MethodParamNameVisitor.getMethodParamNames(method);
	    
	    Object[] objs = new Object[params.length];
	    int resultMapIndex = ParamModel.NO_RESULT_MAP;
	    
	    //iterate all types
	    for (int i = 0; i < params.length; i ++)
	    {
	        // if String or something basic object type
	        if (OTHER_OBJECT_TYPE_LIST.contains(params[i].getType().getName()))
	        {
	            objs[i] = request.getParameter(paramNames[i]);
	        }
	        
	        // if map type , means results
	        else if (RESULT_TYPE.equals(params[i].getType().getName()))
	        {
	            objs[i] = new HashMap<>();
	            resultMapIndex = i;
	        }
	        
	        // if user wanna get http servlet request
	        else if (REQUEST_TYPE.equals(params[i].getType().getName()))
	        {
	            objs[i] = request;
	        }
	        
	        // if user wanna get http servlet response
	        else if (RESPONSE_TYPE.equals(params[i].getType().getName()))
	        {
	            objs[i] = response;
	        }
	        
	        // other user defined type
	        else
	        {
	            objs[i] = setUserModel(params[i], request);
	        }
	    }
	    
	    //set paramModel
	    paramModel.setParams(objs);
	    paramModel.setResultMapIndex(resultMapIndex);
	    return paramModel;
	}

    private static Object setUserModel(Parameter param, HttpServletRequest request) {
        try
        {
            Object userModel = instantiate(param.getType());
            
            //instant object
            Field[] fields = getClassFieldNames(param.getType());
            
            String fieldName, setMethodName;
            Object fieldValue;
            Method setMethod;
            //iterate fields
            for (Field field : fields)
            {
                fieldName = field.getName();
                fieldValue = request.getParameter(fieldName);
                setMethodName = "set" + StringUtil.upperFirst(fieldName);
                
                try {
                    // get setmethod
                    setMethod = param.getType().getMethod(setMethodName, field.getType());
                } catch (NoSuchMethodException ex) {
                    // can't get setmethod then continue.
                    continue;
                }
                
                try
                {
                    //invoke set method
                    setMethod.invoke(userModel, fieldValue);
                }
                catch (Exception ex)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Can't invoke method [").append(setMethodName)
                      .append("] of class[").append(param.getType().getName()).append("].");
                    logger.warn(sb.toString(), ex);
                }
            }
            
            return userModel;
        }
        catch(Throwable e)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Error happened when setting fields of class [" + param.getType().getName() + "].", e);
            }
        }
        
        return null;
    }
}
