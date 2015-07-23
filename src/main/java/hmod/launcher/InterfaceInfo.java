
package hmod.launcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for providing required and optional information about algorithm
 * interfaces in the hMod launcher.
 * @author Enrique Urra C.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceInfo
{
    String id();
    String description() default "not specified";
}
