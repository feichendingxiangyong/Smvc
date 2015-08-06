/**
 * User model for test only
 */
package com.smvc.framework.test;

/**
 * @author Big Martin
 *
 */
public class UserModel {
    private String userName;
    
    private String psw;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
