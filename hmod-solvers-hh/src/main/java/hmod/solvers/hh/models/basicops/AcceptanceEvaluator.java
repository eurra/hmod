
package hmod.solvers.hh.models.basicops;

import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
public interface AcceptanceEvaluator<T extends HHSolution>
{
    T evaluateAcceptance(T currAccepted, T candidate);
}
