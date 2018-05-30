
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public interface FactorMap
{
    FactorValue getFactorValue(Factor factor);
    Factor[] getFactors();
    boolean existsFactor(Factor factor);
    int getFactorsCount();
    FactorMap clone();
}
