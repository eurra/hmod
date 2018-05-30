
package hmod.launcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Enrique Urra
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.PACKAGE)
public @interface LauncherPlugin
{
    String name();
    String description() default "no description";
    String version() default "no version";
}
