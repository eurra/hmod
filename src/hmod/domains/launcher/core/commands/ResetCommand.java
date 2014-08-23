
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.AlgorithmLoaderHandler;
import optefx.util.output.OutputManager;

/**
 * Implements the 'restart' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="reset",
    usage="reset",
    description="Resets the currently loaded algorithm definitions."
)
public class ResetCommand extends Command
{
    private AlgorithmLoaderHandler algorithmParserHandler;

    public void setAlgorithmParserHandler(AlgorithmLoaderHandler algorithmParserHandler)
    {
        this.algorithmParserHandler = algorithmParserHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        algorithmParserHandler.getCurrentLoader().restart();
        OutputManager.println(Launcher.OUT_COMMON, "Algorithm load restarted.");
    }
}
