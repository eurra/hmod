
package hmod.launcher.components;

import hmod.core.DataInterface;
import hmod.launcher.VariableProcessor;
import hmod.launcher.VariableManager;

/**
 *
 * @author Enrique Urra C.
 */
public interface VariableData extends DataInterface
{
    void setVariableProcessors(VariableProcessor[] processors);
    VariableProcessor[] getVariableProcessors();
    
    void setVariableDelimiter(String delimiter);
    String getVariableDelimiter();
    
    void setVariableManager(VariableManager manager);
    VariableManager getVariableManager();
}
