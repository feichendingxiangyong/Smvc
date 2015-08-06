/**
 * @filename PathUtil.java
 * @createtime 2015.7.19
 * @author Big Martin
 * @comment referenced from spring AntPathMatcher
 */
package com.smvc.framework.util;

import java.util.Map;


/**
 * @author Big Martin
 * @note referenced from spring.AntPathMatcher
 *
 */
public class PathUtil {
    
    /** Default path separator: "/" */
    public static final String DEFAULT_PATH_SEPARATOR = "/";
    
    private static final PathUtil INSTANCE = new PathUtil();
    
    public static PathUtil getInstance()
    {
        return INSTANCE;
    }
    
    private String pathSeparator = DEFAULT_PATH_SEPARATOR;
    
    public boolean match(String pattern, String path) {
        return doMatch(pattern, path, true, null);
    }

    public boolean matchStart(String pattern, String path) {
        return doMatch(pattern, path, false, null);
    }


    /**
     * Actually match the given <code>path</code> against the given <code>pattern</code>.
     * @param pattern the pattern to match against
     * @param path the path String to test
     * @param fullMatch whether a full pattern match is required (else a pattern match
     * as far as the given base path goes is sufficient)
     * @return <code>true</code> if the supplied <code>path</code> matched, <code>false</code> if it didn't
     */
    protected boolean doMatch(String pattern, String path, boolean fullMatch,
            Map<String, String> uriTemplateVariables) {

        if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
            return false;
        }

        String[] pattDirs = StringUtil.tokenizeToStringArray(pattern, this.pathSeparator);
        String[] pathDirs = StringUtil.tokenizeToStringArray(path, this.pathSeparator);

        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;

        // Match all elements up to the first **
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String patDir = pattDirs[pattIdxStart];
            if ("**".equals(patDir)) {
                break;
            }
            if (!matchStrings(patDir, pathDirs[pathIdxStart])) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }

        if (pathIdxStart > pathIdxEnd) {
            // Path is exhausted, only match if rest of pattern is * or **'s
            if (pattIdxStart > pattIdxEnd) {
                return (pattern.endsWith(this.pathSeparator) ? path.endsWith(this.pathSeparator) :
                        !path.endsWith(this.pathSeparator));
            }
            if (!fullMatch) {
                return true;
            }
            if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
                return true;
            }
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        else if (pattIdxStart > pattIdxEnd) {
            // String not exhausted, but pattern is. Failure.
            return false;
        }
        else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
            // Path start definitely matches due to "**" part in pattern.
            return true;
        }

        // up to last '**'
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String patDir = pattDirs[pattIdxEnd];
            if (patDir.equals("**")) {
                break;
            }
            if (!matchStrings(patDir, pathDirs[pathIdxEnd])) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        if (pathIdxStart > pathIdxEnd) {
            // String is exhausted
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }

        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (pattDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == pattIdxStart + 1) {
                // '**/**' situation, so skip one
                pattIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - pattIdxStart - 1);
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            int foundIdx = -1;

            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = pattDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (!matchStrings(subPat, subStr)) {
                        continue strLoop;
                    }
                }
                foundIdx = pathIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            pattIdxStart = patIdxTmp;
            pathIdxStart = foundIdx + patLength;
        }

        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!pattDirs[i].equals("**")) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Combines two patterns into a new pattern that is returned.
     * <p>This implementation simply concatenates the two patterns, unless the first pattern
     * contains a file extension match (such as {@code *.html}. In that case, the second pattern
     * should be included in the first, or an {@code IllegalArgumentException} is thrown.
     * <p>For example: <table>
     * <tr><th>Pattern 1</th><th>Pattern 2</th><th>Result</th></tr> <tr><td>/hotels</td><td>{@code
     * null}</td><td>/hotels</td></tr> <tr><td>{@code null}</td><td>/hotels</td><td>/hotels</td></tr>
     * <tr><td>/hotels</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels</td><td>bookings</td><td>/hotels/bookings</td></tr>
     * <tr><td>/hotels/*</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels/&#42;&#42;</td><td>/bookings</td><td>/hotels/&#42;&#42;/bookings</td></tr>
     * <tr><td>/hotels</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr> <tr><td>/hotels/*</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr>
     * <tr><td>/hotels/&#42;&#42;</td><td>{hotel}</td><td>/hotels/&#42;&#42;/{hotel}</td></tr>
     * <tr><td>/*.html</td><td>/hotels.html</td><td>/hotels.html</td></tr> <tr><td>/*.html</td><td>/hotels</td><td>/hotels.html</td></tr>
     * <tr><td>/*.html</td><td>/*.txt</td><td>IllegalArgumentException</td></tr> </table>
     * @param pattern1 the first pattern
     * @param pattern2 the second pattern
     * @return the combination of the two patterns
     * @throws IllegalArgumentException when the two patterns cannot be combined
     */
    public String combine(String pattern1, String pattern2) {
        if (StringUtil.isEmpty(pattern1) && StringUtil.isEmpty(pattern2)) {
            return "";
        }
        else if (StringUtil.isEmpty(pattern1)) {
            return pattern2;
        }
        else if (StringUtil.isEmpty(pattern2)) {
            return pattern1;
        }
        else if (!pattern1.contains("{") && match(pattern1, pattern2)) {
            return pattern2;
        }
        else if (pattern1.endsWith("/*")) {
            if (pattern2.startsWith("/")) {
                // /hotels/* + /booking -> /hotels/booking
                return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
            }
            else {
                // /hotels/* + booking -> /hotels/booking
                return pattern1.substring(0, pattern1.length() - 1) + pattern2;
            }
        }
        else if (pattern1.endsWith("/**")) {
            if (pattern2.startsWith("/")) {
                // /hotels/** + /booking -> /hotels/**/booking
                return pattern1 + pattern2;
            }
            else {
                // /hotels/** + booking -> /hotels/**/booking
                return pattern1 + "/" + pattern2;
            }
        }
        else {
            int dotPos1 = pattern1.indexOf('.');
            if (dotPos1 == -1) {
                // simply concatenate the two patterns
                if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
                    return pattern1 + pattern2;
                }
                else {
                    return pattern1 + "/" + pattern2;
                }
            }
            String fileName1 = pattern1.substring(0, dotPos1);
            String extension1 = pattern1.substring(dotPos1);
            String fileName2;
            String extension2;
            int dotPos2 = pattern2.indexOf('.');
            if (dotPos2 != -1) {
                fileName2 = pattern2.substring(0, dotPos2);
                extension2 = pattern2.substring(dotPos2);
            }
            else {
                fileName2 = pattern2;
                extension2 = "";
            }
            String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
            String extension = extension1.startsWith("*") ? extension2 : extension1;

            return fileName + extension;
        }
    }
    
    public String combine2(String pattern1, String pattern2) {
        if (StringUtil.isEmpty(pattern1) && StringUtil.isEmpty(pattern2)) {
            return "";
        }
        else if (StringUtil.isEmpty(pattern1)) {
            return pattern2;
        }
        else if (StringUtil.isEmpty(pattern2)) {
            return pattern1;
        }
        else if (!pattern1.contains("{") && match(pattern1, pattern2)) {
            return pattern2;
        }
        else if (pattern1.endsWith("/*")) {
            if (pattern2.startsWith("/")) {
                // /hotels/* + /booking -> /hotels/booking
                return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
            }
            else {
                // /hotels/* + booking -> /hotels/booking
                return pattern1.substring(0, pattern1.length() - 1) + pattern2;
            }
        }
        else {
            if (pattern2.startsWith("/")) {
                // /hotels/ + booking -> /hotels/*booking
                return pattern1 + pattern2.substring(0, pattern2.length() - 1);
            }
            else {
                // /hotels/ + booking -> /hotels/*booking
                return pattern1 + pattern2;
            }
        }
    }
    
    public boolean matchStrings(String pattern, String str) {
        if (pattern.contains("*"))
        {
            pattern = pattern.replace("*", ".*");
        }
        return str.matches(pattern);
    }
}
