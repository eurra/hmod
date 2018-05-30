
package hmod.launcher;

import optefx.util.output.OutputManager;


/**
 * Implements the 'add_console_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="addOutput",
    usage="addOutput <id>",
    description="Adds an algorithm system output handler.\n"
    + "<id>: the id to be associated with the handler."
)
class AddOutputCommand extends Command
{
    private final OutputSettings outputConfigHandler;

    public AddOutputCommand(OutputSettings outputConfigHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        String output = args.getString(0);
        outputConfigHandler.addSystemOutput(output);
        OutputManager.println(Launcher.OUT_COMMON, "System output '" + output + "' enabled.");
    }
}