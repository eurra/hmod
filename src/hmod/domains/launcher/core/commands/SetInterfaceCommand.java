
package hmod.domains.launcher.core.commands;

import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.RunInterfaceHandler;
import hmod.domains.launcher.core.running.InterfaceInfo;
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
    private RunInterfaceHandler runInterfaceHandler;

    public void setRunInterfaceHandler(RunInterfaceHandler runInterfaceHandler)
    {
        this.runInterfaceHandler = runInterfaceHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        String[] factoriesIds = runInterfaceHandler.getSupportedInterfacesIds();
        
        if(args.getCount() < 1)
        {
            for(int i = 0; i < factoriesIds.length; i++)
            {
                InterfaceInfo info;
                
                try
                {
                    info = runInterfaceHandler.getInterfaceInfoFor(factoriesIds[i]);
                }
                catch(DataHandlingException ex)
                {
                    throw new LauncherException(ex);
                }
                
                OutputManager.println(Launcher.OUT_COMMON, "'" + info.id() + "': " + info.description());
            }
        }
        else
        {
            String id = args.getString(0);
            
            try
            {
                runInterfaceHandler.setCurrentInterface(id);
                OutputManager.println(Launcher.OUT_COMMON, "Interface '" + id + "' selected.");
            }
            catch(DataHandlingException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
            }  
        }
    }
}