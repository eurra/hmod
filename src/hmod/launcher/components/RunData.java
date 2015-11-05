
package hmod.launcher.components;

import hmod.core.DataInterface;
import hmod.launcher.RunManager;
import hmod.launcher.running.AlgorithmInterfaceFactory;
import hmod.launcher.running.AlgorithmRunnerFactory;

/**
 *
 * @author Enrique Urra C.
 */
public interface RunData extends DataInterface
{
    void setThreading(boolean threading);
    boolean getThreading();
    
    void setInterfaceFactories(AlgorithmInterfaceFactory[] factories);
    AlgorithmInterfaceFactory[] getInterfaceFactories();
    
    void setRunnerFactories(AlgorithmRunnerFactory[] factories);
    AlgorithmRunnerFactory[] getRunnerFactories();
    
    void setDefaultRunner(String id);
    String getDefaultRunner();
    
    void setDefaultInterface(String id);
    String getDefaultInterface();
    
    void setRunManager(RunManager manager);
    RunManager getRunManager();
}
