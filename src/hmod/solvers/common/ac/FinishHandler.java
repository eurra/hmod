
package hmod.solvers.common.ac;

import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface FinishHandler extends DataHandler
{
    void finish();
    boolean isFinished();
}
