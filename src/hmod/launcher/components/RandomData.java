
package hmod.launcher.components;

import hmod.core.DataInterface;

/**
 *
 * @author Enrique Urra C.
 */
public interface RandomData extends DataInterface
{    
    void setSeed(long seed);
    long getSeed();
}
