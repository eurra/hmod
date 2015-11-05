
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.CommandBuffer;
import hmod.launcher.CommandBufferManager;
import hmod.launcher.CommandExecInfo;
import hmod.launcher.CommandParser;
import hmod.launcher.Launcher;
import hmod.launcher.components.CommandData;
import optefx.util.output.OutputManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Implements the 'batch' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="batch",
    usage="batch <input>",
    description="Runs a set of scripted commands from a file in packages or "
        + "the filesystem.\n"
    + "<input>: the input from which the commands will be extracted. The "
        + "filesystem will be searched first, packages after."
)
public class BatchCommand extends Command
{
    private CommandData commandHandler;

    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1)
            throw new LauncherException("Usage: " + getUsage());
        
        String input = args[0];        
        BufferedReader reader;
        
        try
        {
            reader = new BufferedReader(new FileReader(input));
        }
        catch(FileNotFoundException ex)
        {
            InputStream is = BatchCommand.class.getResourceAsStream(input);
            
            if(is == null)
                throw new LauncherException("The provided input (" + input + ") was not found", ex);
                
            reader = new BufferedReader(new InputStreamReader(is));
        }
        
        CommandParser parser = commandHandler.getCommandParser();
        CommandBufferManager bufferManager = commandHandler.getCommandBufferManager();
        CommandBuffer buffer = bufferManager.pushBuffer();
        String line;

        try
        {
            while((line = reader.readLine()) != null)
            {
                CommandExecInfo cmdInfo = parser.parseInput(line);
                
                if(cmdInfo == null)
                    continue;
                
                buffer.pushCommand(cmdInfo);
            }
            
            CommandExecInfo currCmdInfo;
            OutputManager.println(Launcher.OUT_COMMON, "*** Running batch (" + buffer.count() + " commands)...");
            
            while((currCmdInfo = buffer.next()) != null)
            {
                Command cmd = currCmdInfo.getCmd();
                String cmdArgs = currCmdInfo.getArgs();                

                cmd.executeCommand(commandHandler.getCommandParser().parseArguments(cmdArgs));
            }
        }
        catch(IOException ex)
        {
            throw new LauncherException(ex);
        }
        finally
        {          
            bufferManager.popBuffer();
            
            try
            {
                reader.close();
            }
            catch(IOException ex)
            {
            }
        }
        
        OutputManager.println(Launcher.OUT_COMMON, "*** Batch complete.");
    }
}