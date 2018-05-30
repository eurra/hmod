
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'set_interface' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="setInterface",
    usage="setInterface <id>",
    description="Configures the interface used during an algorithm execution.\n"
        + "<id> The id of the interface to set. Run this command without this"
        + " argument to see all the available ids."
)
class SetInterfaceCommand extends Command
{
    private final InterfaceRegistry runInterfaceHandler;

    public SetInterfaceCommand(InterfaceRegistry runInterfaceHandler)
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
                catch(IllegalArgumentException ex)
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
            catch(IllegalArgumentException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
            }  
        }
    }
}