
package hmod.launcher;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public final class LauncherControl
{
    private boolean running;
    private boolean debug;
    private boolean threading;
    private String batchFolder;
    private String outputFolder;

    LauncherControl(boolean debugEnabled, boolean threadingEnabled, String defBatchFolder, String defOutputFolder)
    {
        this.running = true;
        this.debug = debugEnabled;
        this.threading = threadingEnabled;
        this.batchFolder = defBatchFolder;
        this.outputFolder = defOutputFolder;
    }

    public void stop()
    {
        running = false;
    }

    @Operator
    public boolean isRunning()
    {
        return running;
    }

    @Operator
    public void enableDebugging()
    {
        debug = true;
    }

    public void disableDebugging()
    {
        debug = false;
    }

    public boolean isDebugEnabled()
    {
        return debug;
    }

    public void setScriptBaseFolder(String folder)
    {
        batchFolder = folder;
    }

    public String getScriptBaseFolder()
    {
        return batchFolder;
    }

    public void setOutputBaseFolder(String folder)
    {
        outputFolder = folder;
    }

    public String getOutputBaseFolder()
    {
        return outputFolder;
    }

    public void enableThreading()
    {
        threading = true;
    }

    public void disableThreading()
    {
        threading = false;
    }

    public boolean isThreadingEnabled()
    {
        return threading;
    }
}
