
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.running.AlgorithmInterface;
import hmod.domains.launcher.core.running.InterfaceInfo;

/**
 *
 * @author Enrique Urra C.
 */
public interface RunInterfaceHandler extends DataHandler
{
    void setCurrentInterface(String id) throws DataHandlingException;
    InterfaceInfo getCurrentInterfaceInfo() throws DataHandlingException;
    AlgorithmInterface createNewInterfaceFromCurrent(String algName) throws DataHandlingException;
    InterfaceInfo getInterfaceInfoFor(String id) throws DataHandlingException;
    String[] getSupportedInterfacesIds();
}
