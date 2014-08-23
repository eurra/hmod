
package hmod.domains.launcher.core.ac;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultLauncherHandler implements LauncherHandler
{
    private boolean running;
    private boolean debug;
    private boolean threading;
    private String batchFolder;
    private String outputFolder;

    public DefaultLauncherHandler(boolean debugEnabled, boolean threadingEnabled, String defBatchFolder, String defOutputFolder)
    {
        this.running = true;
        this.debug = debugEnabled;
        this.threading = threadingEnabled;
        this.batchFolder = defBatchFolder;
        this.outputFolder = defOutputFolder;
    }

    @Override
    public void stop()
    {
        running = false;
    }

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @Override
    public void enableDebugging()
    {
        debug = true;
    }

    @Override
    public void disableDebugging()
    {
        debug = false;
    }

    @Override
    public boolean isDebugEnabled()
    {
        return debug;
    }

    @Override
    public void setBatchBaseFolder(String folder)
    {
        batchFolder = folder;
    }

    @Override
    public String getBatchBaseFolder()
    {
        return batchFolder;
    }

    @Override
    public void setOutputBaseFolder(String folder)
    {
        outputFolder = folder;
    }

    @Override
    public String getOutputBaseFolder()
    {
        return outputFolder;
    }

    @Override
    public void enableThreading()
    {
        threading = true;
    }

    @Override
    public void disableThreading()
    {
        threading = false;
    }

    @Override
    public boolean isThreadingEnabled()
    {
        return threading;
    }
}
