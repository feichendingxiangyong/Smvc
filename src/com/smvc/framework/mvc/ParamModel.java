/**
 * It is used to maintain parameter info when invoking user's controller.
 */
package com.smvc.framework.mvc;

/**
 * @author Big Martin
 *
 */
public class ParamModel {
    /**
     * no result to return.
     */
    public static final int NO_RESULT_MAP = -1;
    
    /**
     * parameters
     */
    private Object[] params;
    
    /**
     * the result key-value result map index.(Smvc use a map to represent user's controller invoking results.)
     */
    private int resultMapIndex = NO_RESULT_MAP;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public int getResultMapIndex() {
        return resultMapIndex;
    }

    public void setResultMapIndex(int resultMapIndex) {
        this.resultMapIndex = resultMapIndex;
    }
    
    
}
