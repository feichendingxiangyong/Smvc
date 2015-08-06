package com.smvc.framework.util;

public final class ArrayUtil {
    
    /**
     * determine whether array is empty : null or size of array is 0.
     * @param array
     * @return true:empty
     */
    public static <T> boolean isArrayEmpty(T[] array)
    {
        if (array == null)
        {
            return false;
        }
        if (array.length == 0)
        {
            return false;
        }
        return true;
    }
}
