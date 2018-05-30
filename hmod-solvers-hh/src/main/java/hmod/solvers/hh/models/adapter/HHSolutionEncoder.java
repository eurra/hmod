
package hmod.solvers.hh.models.adapter;

import hmod.core.AlgorithmException;
import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
public interface HHSolutionEncoder<T extends HHSolution, K>
{
    T encode(K llSolution) throws AlgorithmException;
}
