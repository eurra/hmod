
package hmod.solvers.hh.models.adapter;

import hmod.core.AlgorithmException;

/**
 *
 * @author Enrique Urra C.
 */
public interface HHSolutionUploader<T>
{
    T upload() throws AlgorithmException;
}
