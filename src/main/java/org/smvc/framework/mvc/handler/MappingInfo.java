package org.smvc.framework.mvc.handler;

import java.util.List;

import org.smvc.framework.util.Assert;
import org.smvc.framework.util.PathUtil;

/**
 * Holder for request mapping metadata.
 * @author Big Martin
 *
 */
public class MappingInfo {
    
    /**
     * the type of mapping , class or method
     */
    private MappingType type;
    
    /**
     * uri, like 'test.do'
     */
    private String[] patterns;
    
    /**
     * uri parameters, like 'param1=1' of 'test.do?param1=1'
     */
    private String[] params;
    
    public MappingInfo(MappingType type, String[] patterns, String[] params) {
        super();
        this.type = type == null ? MappingType.CLASS : MappingType.METHOD;
        this.patterns = patterns == null ? new String[0] : patterns;
        this.params = params == null ? new String[0] : params;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public String[] getParams() {
        return params;
    } 
    
    public boolean hasPatterns()
    {
        return patterns.length != 0;
    } 
    
    /**
     * Determine whether this mappinginfo can match the request.
     * @param realPath real uri without params string and web context path.
     * @param paramsStr parameter string
     * @return
     */
    public boolean isMatch(String realPath, List<String> paramStrs)
    {
        Assert.notNull(paramStrs);
        Assert.notNull(realPath);
        
        boolean patternMatched = false;
        
        if (patterns.length == 0)
        {
            patternMatched = true;
        }
        
        //match url : Just need get one match
        for (String pattern : patterns)
        {
            if (realPath.startsWith(pattern)
                || PathUtil.getInstance().match(pattern, realPath))
            {
                patternMatched = true;
                
                break;
            }
        }
        
        //match params : Must match all params
        if (patternMatched)
        {
            if (params.length == 0)
            {
                return true;
            }
            for (String param : params)
            {
                //if any one param not match, then return false
                if (paramStrs.contains(param))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
}
