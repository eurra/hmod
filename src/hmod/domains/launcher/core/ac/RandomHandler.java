
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface RandomHandler extends DataHandler
{    
    void setCurrentSeed(long seed);
    void setRandomSeed();
    long getCurrentSeed();
}
