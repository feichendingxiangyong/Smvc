package org.smvc.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    public static final String EMPTY_STRING = "";
	
	/**
	 * empty or not. 
	 * @return true : empty or space only or null, otherwise false.
	 */
	public static boolean isEmpty(String str)
	{
	    //check conditions
		if (str == null
		    || str.isEmpty()
		    || str.trim().isEmpty())
		{
		    return true;
		}
		
		return false;
	}
	
	/**
	 * upper first character of a String.
	 * For example, userName ---> UserName.
	 * @param str 
	 * @return
	 */
	public static String upperFirst(String str)
	{
	    StringBuilder sb = new StringBuilder();
	    String firstChar;
	    if (!isEmpty(str))
	    {
	        firstChar = str.substring(0, 1);
	        sb.append(firstChar.toUpperCase()).append(str.substring(1, str.length()));
	        
	        return sb.toString();
	    }
	    
	    return EMPTY_STRING;
	}
	
	/**
	 * standard url, e.g '/test.do' -->  'test.do' 
	 * @return
	 */
	public static String[] standardUrlPattern(String[] urls)
	{
	    Assert.notNull(urls);
	    
	    String[] results = new String[urls.length];
	    for (int i =0; i < urls.length; i ++)
	    {
	        if (urls[i].startsWith("\\"))
	        {
	            results[i] = urls[i].substring(1, urls[i].length());
	        }
	        else 
	        {
	            results[i] = urls[i];
            }
	    }
	    
	    return results;
	}
	
	   /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter)
     * @param trimTokens trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array
     * (only applies to tokens that are empty after trimming; StringTokenizer
     * will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens (<code>null</code> if the input String
     * was <code>null</code>)
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }
    
    /**
     * Copy the given Collection into a String array.
     * The Collection must contain String elements only.
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the passed-in
     * Collection was <code>null</code>)
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }
}
