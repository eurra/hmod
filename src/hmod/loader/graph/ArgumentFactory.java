
package hmod.loader.graph;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Enrique Urra C.
 */
@Target(ElementType.METHOD)
@Documented
public @interface ArgumentFactory
{
    ArgumentInfo[] value() default {};
}
