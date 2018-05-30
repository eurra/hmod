
package hmod.launcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for provide required and optional information about commands in 
 * the hMod launcher.
 * @author Enrique Urra C.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo
{
    String word();
    String usage() default "not specified";
    String description() default "not specified";
}
