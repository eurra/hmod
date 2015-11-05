
package hmod.core.components;

import hmod.core.DataInterface;

/**
 *
 * @author Enrique Urra C.
 */
public interface TimeElapsedData extends DataInterface
{
    void setInitTime(double time);
    double getInitTime();
    
    void setMaxSeconds(double seconds);
    double getMaxSeconds();
    
    void setElapsedTime(double seconds);
    double getElapsedTime();
    
    void setTimeFinished(boolean finished);
    boolean getTimeFinished();
}
