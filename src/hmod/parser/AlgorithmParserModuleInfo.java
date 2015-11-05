
package hmod.parser;

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
public @interface AlgorithmParserModuleInfo
{
    Class<? extends AlgorithmParserFactory> factoryType();
    String name() default "no name";
    String description() default "no description";
    String version() default "no version";
}
