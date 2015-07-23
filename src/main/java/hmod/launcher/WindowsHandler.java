
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public final class WindowsHandler
{
    private boolean autoClose;

    WindowsHandler(boolean autoClose)
    {
        this.autoClose = autoClose;
    }

    public void enableAutoClose()
    {
        autoClose = true;
    }

    public void disableAutoClose()
    {
        autoClose = false;
    }

    public boolean isAutoCloseEnabled()
    {
        return autoClose;
    }
}
