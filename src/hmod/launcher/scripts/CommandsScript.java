
package hmod.launcher.scripts;

import flexbuilders.basic.ArrayBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;

/**
 * Script that configures the custom commands used by the <i>hMod</i> launcher.<p>
 * 
 * You may write or provide your own version of this class according to your 
 * implementation. Read the instructions below to learn how the file must be 
 * specified. This class, or any modified version of it, must be configured in 
 * the <i>'launcher.commands.script'</i> entry of the <i>'hmod.properties'</i> 
 * file, in the application folder.
 * 
 * @author Enrique Urra C.
 */
public class CommandsScript extends BuildScript
{
    public CommandsScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {
        /*
         * The custom commands' array. Any command added here must extend the 
         * class 'hMod.launcher.Command' and must be annotated with the 
         * 'hMod.launcher.CommandInfo' annotation. Check for examples in the 
         * 'hMod.launcher.commands' package.
         * 
         * The provided commands may require the core data object used by the
         * launcher. Such data is implemented in the object referenced by the
         * 'consoleLauncher_mainData' id. Check the 'hMod.launcher.scripts.Data'
         * script for such object.
         * 
         * Each new command can be defined through any framework-based builder. 
         * The default commands (check the commands section of the 
         * 'hMod.launcher.scripts.MainDataScript' file) are builded as follows:
         * 
         * buildElem(
         *     object(new YourCommandInstance()).
         *     set(createBuilder(cbs.injectorSetter).inject(SomeInjectorSetter1.class), ref("someDataObjectId")).
         *     set(createBuilder(cbs.injectorSetter).inject(SomeInjectorSetter2.class), ref("someDataObjectId")).
         *     ...
         * ).
         */
       
        branch("consoleLauncher_commands").getBuildableAs(ArrayBuilder.class)
        //  .elem(
        //      <building logic of your first command>
        //   )           
        //  .elem(
        //      <building logic of your second command>
        //   )
        //  ...
        ;
    }
}
