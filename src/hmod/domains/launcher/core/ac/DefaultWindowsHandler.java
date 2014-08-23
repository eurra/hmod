
package hmod.domains.launcher.core.ac;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultWindowsHandler implements WindowsHandler
{
    private boolean autoClose;

    public DefaultWindowsHandler(boolean autoClose)
    {
        this.autoClose = autoClose;
    }

    @Override
    public void enableAutoClose()
    {
        autoClose = true;
    }

    @Override
    public void disableAutoClose()
    {
        autoClose = false;
    }

    @Override
    public boolean isAutoCloseEnabled()
    {
        return autoClose;
    }
    
}
