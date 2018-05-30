
package hmod.solvers.hh.models.adapter;

import hmod.core.AlgorithmException;
import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
public interface HHSolutionDecoder<T extends HHSolution, K>
{
    K decode(T solution) throws AlgorithmException;
}
