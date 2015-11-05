
package hmod.launcher.scripts;

import flexbuilders.basic.ArrayBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;

/**
 * Script that configures the custom plugins used by the <i>hMod</i> launcher.<p>
 * 
 * Any plugin added in this file must implement the interface 'hMod.launcher.Plugin'.
 * Check for examples in the 'hMod.launcher.plugins' package. Different plugins 
 * can be added for different events within the launcher's execution. Each array
 * defined in this script store the plugins related to a particular event.<p>
 * 
 * The provided plugins may require the core data object used by the launcher. 
 * Such data is implemented in the object referenced by the 'consoleLauncher_mainData' 
 * id. Check the 'hMod.launcher.scripts.Data' script for such object.<p>
 * 
 * Each new plugin can be defined through any framework-based builder. The 
 * default plugins (check the plugins section of the 
 * 'hMod.launcher.scripts.MainDataScript' file) are builded as follows:
 * 
 * buildElem(
 *     obj(new YourPluginInstance()).
 *     set(createBuilder(cbs.ijSetter).inject(SomeInjectorSetter1.class), ref("someDataObjectId")).
 *     set(createBuilder(cbs.ijSetter).inject(SomeInjectorSetter2.class), ref("someDataObjectId")).
 *     ...
 * ).
 * 
 * You may write or provide your own version of this class according to your 
 * implementation. Read the instructions below to learn how the file must be 
 * specified. This class, or any modified version of it, must be configured in 
 * the <i>'launcher.plugins.script'</i> entry of the <i>'hmod.properties'</i> 
 * file, in the application folder.
 * 
 * @author Enrique Urra C.
 */
public class PluginsScript extends BuildScript
{
    public PluginsScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {
        branch("consoleLauncher_startPlugins").getBuildableAs(ArrayBuilder.class)
        //  .elem(
        //      <building logic of your first plugin>
        //   )           
        //  .elem(
        //      <building logic of your second plugin>
        //   )
        //  ...
        ;
        
        branch("consoleLauncher_onCmdLoadPlugins").getBuildableAs(ArrayBuilder.class)
        //  .elem(
        //      <building logic of your first plugin>
        //   )           
        //  .elem(
        //      <building logic of your second plugin>
        //   )
        //  ...
        ;
        
        branch("consoleLauncher_onCmdExecPlugins").getBuildableAs(ArrayBuilder.class)
        //  .elem(
        //      <building logic of your first plugin>
        //   )           
        //  .elem(
        //      <building logic of your second plugin>
        //   )
        //  ...
        ;
        
        branch("consoleLauncher_endPlugins").getBuildableAs(ArrayBuilder.class)
        //  .elem(
        //      <building logic of your first plugin>
        //   )           
        //  .elem(
        //      <building logic of your second plugin>
        //   )
        //  ...
        ;
    }
}
