
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class FactorValue
{
    public static FactorValue create(double value)
    {
        return new FactorValue(value);
    }
    
    private final double value;

    private FactorValue(double value)
    {
        this.value = value;
    }

    public double getValue()
    {
        return value;
    }
}
