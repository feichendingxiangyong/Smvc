package com.smvc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * request related tools
 * @author dingxiangyong
 *
 */
public class RequestUtil {
	
    /**
     * get real request url without context path and '/'.
     * @param request httpservletrequest
     * @return real request url
     */
    public static String getRealRequestURIWithoutPrefix(HttpServletRequest request)
    {
        String realUri = getRealRequestURI(request);
        return realUri.replaceFirst("/", "");
    }
    
    /**
     * get real request url without context path.
     * @param request httpservletrequest
     * @return real request url
     */
	public static String getRealRequestURI(HttpServletRequest request)
	{
	    String requestURI = request.getRequestURI();
	    String contextPath = request.getContextPath();
	    
		//check contextPath
		//if empty or equals with /, then return it
		if (StringUtil.isEmpty(contextPath)
		        || "/".equals(contextPath))
		{
		    return requestURI;
		}
		
		//check requestURI
		if (StringUtil.isEmpty(requestURI))
		{
		    return requestURI;
		}
		
		//remove contextPath
		if (requestURI.contains(contextPath))
		{
		    return requestURI.replaceFirst(contextPath, "");
		}
		
		
		return requestURI;
	}
	
	/**
	 * get all parameter from http request
	 * @param request
	 * @return
	 */
	public static Map<String, String[]> getRequestParamMapping(HttpServletRequest request)
	{
	    return request.getParameterMap();
	}
	
	/**
	 * Get parameter list of request, for example : 'test.do?test1=1&test2=2' --> 'test1=1&test2=2'
	 * @param request
	 * @return if not parameter , return empty list not null.
	 */
	public static List<String> getRequestParamStrList(HttpServletRequest request)
	{
	    Assert.notNull(request);
	    
	    //get parameter string
        String paramsStr = request.getQueryString();
        String[] params = paramsStr.split("&");
        
        if (ArrayUtil.isArrayEmpty(params))
        {
            return Arrays.asList(params);
        }
        
        return new ArrayList<String>();
	}
}
