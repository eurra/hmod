
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface LauncherHandler extends DataHandler
{
    void stop();
    boolean isRunning(); 
    void enableDebugging();
    void disableDebugging();
    boolean isDebugEnabled();
    void setBatchBaseFolder(String folder);
    String getBatchBaseFolder();
    void setOutputBaseFolder(String folder);
    String getOutputBaseFolder();
    void enableThreading();
    void disableThreading();
    boolean isThreadingEnabled();
}