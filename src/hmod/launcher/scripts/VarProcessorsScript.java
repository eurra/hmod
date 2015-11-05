
package hmod.launcher.scripts;

import flexbuilders.basic.ArrayBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;

/**
 * Script that configures the custom variable processors used by the <i>hMod</i>
 * launcher.<p>
 * 
 * You may write or provide your own version of this class according to your 
 * implementation. Read the instructions below to learn how the file must be 
 * specified. This class, or any modified version of it, must be configured in 
 * the <i>'launcher.variable.processors.script'</i> entry of the 
 * <i>'hmod.properties'</i> file, in the application folder.
 * 
 * @author Enrique Urra C.
 */
public class VarProcessorsScript extends BuildScript
{
    public VarProcessorsScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {
        /*
         * The custom processors' array. Any processor added must extend the 
         * 'hMod.launcher.VariableProcessor' class. Check for examples in the 
         * 'hMod.launcher.varProcessors' package. Also, the array length must 
         * be updated if necessary.
         * 
         * The provided processors may require the core data object used by the
         * launcher. Such data is implemented in the object referenced by the
         * 'consoleLauncher_mainData' id. Check the 'hMod.launcher.scripts.Data'
         * script for such object.
         * 
         * Each new processor can be defined through any framework-based builder. 
         * The default processor (check the variable processor section of the 
         * 'hMod.launcher.scripts.MainDataScript' file) are builded as follows:
         * 
         * buildElem(
         *     obj(new YourProcessorInstance()).
         *     set(createBuilder(cbs.ijSetter).inject(SomeInjectorSetter1.class), ref("someDataObjectId")).
         *     set(createBuilder(cbs.ijSetter).inject(SomeInjectorSetter2.class), ref("someDataObjectId")).
         *     ...
         * ).
         */
        
        branch("consoleLauncher_variableProcessors").getBuildableAs(ArrayBuilder.class)
        //  .buildElem(
        //      <building logic of your first factory>
        //   )           
        //  .buildElem(
        //      <building logic of your second factory>
        //   )
        //  ...
        ;
    }
}
