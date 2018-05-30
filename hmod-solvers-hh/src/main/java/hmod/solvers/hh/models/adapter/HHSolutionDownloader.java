
package hmod.solvers.hh.models.adapter;

import hmod.core.AlgorithmException;

/**
 *
 * @author Enrique Urra C.
 */
public interface HHSolutionDownloader<T>
{
    void download(T llSolution) throws AlgorithmException;
}
