/**
 * Controller.java
 * @author Big Martin
 */
package org.smvc.framework.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller annotation, it tells Smvc that this is a controller class.
 * @author Big Martin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Controller {
    
}
