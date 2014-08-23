
package hmod.loader.graph;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 *
 * @author Enrique Urra C.
 */
@Target(ElementType.METHOD)
@Documented
@Repeatable(ArgumentFactory.class)
public @interface ArgumentInfo
{
    Class type();
    boolean optional() default false;
}
