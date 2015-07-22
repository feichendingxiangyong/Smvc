/**
 * @filename ApplicationContext.java
 * @createtime 2015.7.21
 * @author Big Martin
 * @comment 
 */
package com.smvc.context;

/**
 * Store framework configuration and other basic information.
 * @author Big Martin
 *
 */
public class ApplicationContext {
    /**
     * user's controller package that need to scan.
     */
    private String scanPackage;

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    @Override
    public String toString() {
        return "ApplicationContext [scanPackage=" + scanPackage + "]";
    }
    
    
    
}
