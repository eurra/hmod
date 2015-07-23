
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="requireSet",
    usage="requireSet <var1> [var2, var3, ..., varN]",
    description="Checks if a set ov variables have been set, throwing and error otherwise.\n"
    + "<var1> [var2, var3, ..., varN]: The variables to check."
)
class RequireSetCommand extends Command
{
    private final VariableHandler variableHandler;

    public RequireSetCommand(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        for(int i = 0; i < args.getCount(); i++)
        {
            if(!variableHandler.isSet(args.getString(i)))
                throw new LauncherException("Required variable non-set: " + args.getString(i));
        }
    }
}