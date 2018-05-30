
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'restart' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="reloadBundles",
    usage="reloadBundles",
    description="Reload the bundles' library."
)
class ReloadBundlesCommand extends Command
{
    private final BundleLoader bundleHandler;

    public ReloadBundlesCommand(BundleLoader bundleHandler)
    {
        this.bundleHandler = bundleHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        bundleHandler.reload();
        OutputManager.println(Launcher.OUT_COMMON, "Bundle library reloaded.");
    }
}
