
package hmod.core.components;

import hmod.core.DataInterface;

/**
 *
 * @author Enrique Urra C.
 */
public interface IterationData extends DataInterface
{
    void setMaxIteration(int maxIteration);
    int getMaxIteration();
    
    void setCurrentIteration(int iteration);
    int getCurrentIteration();
    
    void setIterationFinished(boolean finished);
    boolean getIterationFinished();
}
