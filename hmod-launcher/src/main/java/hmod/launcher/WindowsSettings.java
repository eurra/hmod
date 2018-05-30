
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public final class WindowsSettings
{
    private boolean autoClose;

    WindowsSettings(boolean autoClose)
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
