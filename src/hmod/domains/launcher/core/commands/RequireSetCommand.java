
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.VariableCollection;
import hmod.domains.launcher.core.ac.VariableHandler;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="require_set",
    usage="require_set <var1> [var2, var3, ..., varN]",
    description="Checks if a set ov variables have been set, throwing and error otherwise.\n"
    + "<var1> [var2, var3, ..., varN]: The variables to check."
)
public class RequireSetCommand extends Command
{
    private VariableHandler variableHandler;

    public void setVariableHandler(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        VariableCollection activeCollection = variableHandler.getActiveVariables();
        
        for(int i = 0; i < args.getCount(); i++)
        {
            if(!activeCollection.isSet(args.getString(i)))
                throw new LauncherException("Required variable non-set: " + args.getString(i));
        }
    }
}