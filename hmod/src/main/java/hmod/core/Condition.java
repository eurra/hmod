
package hmod.core;

import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public interface Condition extends Expression<Boolean>
{
    public static final Condition TRUE = () -> true;
    public static final Condition FALSE = () -> false;
    
    public static Condition from(Supplier<Boolean> sup) 
    {
        Objects.requireNonNull(sup, "null supplier");
        return () -> sup.get();
    }
}
