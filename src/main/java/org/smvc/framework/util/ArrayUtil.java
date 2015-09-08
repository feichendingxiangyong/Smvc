package org.smvc.framework.util;

import java.util.Collection;

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
    
    /**
     * determine whether array is empty : null or size of array is 0.
     * @param array
     * @return true:empty
     */
    public static <T> boolean isContrainerEmpty(Collection<T> collection)
    {
        if (collection == null)
        {
            return false;
        }
        if (collection.size() == 0)
        {
            return false;
        }
        return true;
    }
}
