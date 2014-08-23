
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.CommandRunner;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.VariableCollection;
import hmod.domains.launcher.core.ac.CommandBufferHandler;
import hmod.domains.launcher.core.ac.CommandParseHandler;
import hmod.domains.launcher.core.ac.LauncherHandler;
import optefx.util.output.OutputManager;
import java.io.BufferedReader;
import java.io.File;
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
    usage="batch <input> [args...]",
    description="Runs a set of scripted commands from a file in packages or "
        + "the filesystem.\n"
    + "<input>: the input from which the commands will be extracted. The "
        + "filesystem will be searched first, packages after.\n"
    + "[args...] a set of N input arguments that are accesible through"
    + " ordered variables ({$0},{$1},...,{$N-1}) within the batch execution."
)
public class BatchCommand extends Command
{
    private LauncherHandler launcherHandler;
    private CommandBufferHandler commandBufferHandler;
    private CommandParseHandler commandParseHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    public void setCommandBufferHandler(CommandBufferHandler commandBufferHandler)
    {
        this.commandBufferHandler = commandBufferHandler;
    }

    public void setCommandParseHandler(CommandParseHandler commandParseHandler)
    {
        this.commandParseHandler = commandParseHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        int argsCount = args.getCount();
        
        if(argsCount < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        String input = args.getString(0);        
        BufferedReader reader = null;
        String finalInput = null;
        
        try
        {
            reader = new BufferedReader(new FileReader(input));
            finalInput = input;
        }
        catch(FileNotFoundException ex)
        {
            String batchPath = launcherHandler.getBatchBaseFolder();
            File inputPath = new File(batchPath, input);
            
            try
            {
                reader = new BufferedReader(new FileReader(inputPath));
                finalInput = inputPath.getPath();
            }
            catch(FileNotFoundException ex2)
            {
                InputStream is = BatchCommand.class.getResourceAsStream(input);
        
                if(is != null)
                {
                    reader = new BufferedReader(new InputStreamReader(is));
                    finalInput = input;
                }
                else if(is == null && commandBufferHandler.existsCurrentBuffer())
                {
                    String bufferSource = commandBufferHandler.getCurrentBuffer().getSource();
                    File bufferFile = new File(bufferSource);
                    String bufferPath = bufferFile.getParentFile().getPath();
                    File finalPath = new File(bufferPath, input);

                    try
                    {
                        reader = new BufferedReader(new FileReader(finalPath));
                        finalInput = finalPath.getPath();
                    }
                    catch(FileNotFoundException ex3)
                    {
                    }
                }
            }
        }
        
        if(reader == null)
            throw new LauncherException("The provided input (" + input + ") was not found");
        
        CommandBuffer buffer = commandBufferHandler.pushBuffer(finalInput);
        
        if(argsCount > 1)
        {
            VariableCollection varCol = buffer.getVariables();
            
            for(int i = 1; i < argsCount; i++)
                varCol.setVariable("$" + (i - 1), args.getObject(i));
        }
        
        String line;

        try
        {
            while((line = reader.readLine()) != null)
            {
                CommandRunner cmdRunner = commandParseHandler.parseNextCommand(line);
                
                if(cmdRunner == null)
                    continue;
                
                buffer.pushCommand(cmdRunner);
            }
            
            CommandRunner currCmdRunner;
            OutputManager.println(Launcher.OUT_COMMON, "*** Running batch (" + input + " - " + buffer.count() + " commands)...");
            
            while((currCmdRunner = buffer.next()) != null)
                currCmdRunner.runCommand();
        }
        catch(IOException ex)
        {
            throw new LauncherException(ex);
        }
        finally
        {          
            commandBufferHandler.popBuffer();
            
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