
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.RunManager;
import hmod.launcher.components.RunData;
import hmod.launcher.running.AlgorithmInterfaceFactory;
import optefx.util.output.OutputManager;

/**
 * Implements the 'set_interface' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="set_interface",
    usage="set_interface <id>",
    description="Configures the interface used during an algorithm execution.\n"
        + "<id> The id of the interface to set. Run this command without this"
        + " argument to see all the available ids."
)
public class SetInterfaceCommand extends Command
{
    private RunData runHandler;

    public void setRunData(RunData handler)
    {
        this.runHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        RunManager manager = runHandler.getRunManager();
        String[] factoriesIds = manager.getInterfaceIds();
        
        if(args.length < 1)
        {
            for(int i = 0; i < factoriesIds.length; i++)
            {
                AlgorithmInterfaceFactory factory = manager.getInterfaceFactory(factoriesIds[i]);
                OutputManager.println(Launcher.OUT_COMMON, "'" + factory.getId() + "': " + factory.getDescription());
            }
        }
        else
        {
            String id = args[0];
            
            if(manager.setCurrentInterfaceFactory(id))
                OutputManager.println(Launcher.OUT_COMMON, "Interface '" + id + "' selected.");
            else
                OutputManager.println(Launcher.OUT_COMMON, "The provided interface id (" + id + ") is not registered.");
        }
    }
}