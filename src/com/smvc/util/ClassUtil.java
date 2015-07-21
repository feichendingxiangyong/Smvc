/**
 * ClassUtil.java
 */
package com.smvc.util;

/**
 * @author Big Martin
 *
 */
public class ClassUtil {
    private static final String PACKAGE_SEPARATOR = ".";
    private static final String CLASS_FILE_SUFFIX = ".class";
    
    /**
      * Determine the name of the class file, relative to the containing
      * package: e.g. "String.class"
      * @param clazz the class
      * @return the file name of the ".class" file
      */
     public static String getClassFileName(Class<?> clazz) {
         String className = clazz.getName();
         int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
         return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
     }
}
