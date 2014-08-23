
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.domains.launcher.core.VariableCollection;

/**
 *
 * @author Enrique Urra C.
 */
public interface VariableHandler extends DataHandler
{
    VariableCollection getMainVariables();
    VariableCollection getActiveVariables();
}
