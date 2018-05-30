
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="set",
    usage="set <var names...> = <var values...>",
    description="Sets values for different variables, replacing their current assignment. A single value may be applied to many names, or "
    + "N values may be applied to N names. Any other assignation is considered invalid. If a batch environment is active, the variables "
    + "will be declared only on such an environment, and they will not be available when the batch process is finished.\n"
    + "<var values...>: A set of values (1 or N) to assign.\n"
    + "<var names...>: The names in which the single (N) value(s) will be(en) set."
)
class SetCommand extends Command
{
    private final VariableHandler variableHandler;

    public SetCommand(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 3)
            throw new CommandUsageException(this);
        
        int assignerPos = -1;
        int argsCount = args.getCount();
        
        for(int i = 0; i < argsCount && assignerPos == -1; i++)
        {
            Object currArg = args.getObject(i);
            
            if(currArg instanceof String && ((String)currArg).equals("="))
                assignerPos = i;
        }
        
        if(assignerPos == -1 || (assignerPos != 1 && assignerPos != argsCount / 2))
            throw new CommandUsageException(this);
        
        boolean singleValue = assignerPos == 1;
        String varsNames = "";
        
        for(int i = assignerPos + 1; i < argsCount; i++)
        {
            Object variableValue = args.getObject(i);
            String variableName = singleValue ? args.getString(0) : args.getString(i - (argsCount / 2) - 1);
            
            variableHandler.setVariable(variableName, variableValue);
            varsNames += variableName + (i == argsCount - 1 ? "" : ", ");
        }
        
        OutputManager.println(Launcher.OUT_COMMON, "Variable(s) set (" + varsNames + ")");
    }
}