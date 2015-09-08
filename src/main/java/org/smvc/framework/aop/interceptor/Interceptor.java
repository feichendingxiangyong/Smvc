/**
 * @author Big Martin
 *
 */
package org.smvc.framework.aop.interceptor;

import org.smvc.framework.aop.Invocation;

/**
 * Interceptor.
 */
public interface Interceptor {
    
    void intercept(Invocation inv);
}

