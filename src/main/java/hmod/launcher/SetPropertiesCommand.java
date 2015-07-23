
package hmod.launcher;

import java.util.Properties;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="setProps",
    usage="setProps <props_name> <key> <value>",
    description="Sets a new value into the provided properties variable.\n"
    + "<props_name>: the variable associated with the properties object.\n"
    + "<key>: the string key.\n"
    + "<value>: the string value."
)
public class SetPropertiesCommand extends Command
{
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 3)
            throw new CommandUsageException(this);
        
        Properties props = args.getArgAs(0, Properties.class);
        String key = args.getString(1);
        String value = args.getString(2);
        
        props.setProperty(key, value);        
        OutputManager.println(Launcher.OUT_COMMON, "Property set (" + key + " = " + value + ")");
    }    
}