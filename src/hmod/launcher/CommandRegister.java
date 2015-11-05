
package hmod.launcher;

import java.util.HashMap;

/**
 * Implements a register of launcher commands.
 * @author Enrique Urra C.
 */
public class CommandRegister
{
    /**
     * A table with the parseable commands and their related steps.
     */
    private HashMap<String, Command> cmds;
    
    /**
     * Constructor.
     */
    public CommandRegister()
    {
        cmds = new HashMap<>();
    }
    
    /**
     * Registers a single command.
     * @param cmd The provided command object.
     * @throws LauncherException if the provided command is invalid.
     */
    public void registerCommand(Command cmd) throws LauncherException
    {
        if(cmd == null)
            throw new LauncherException("The provided command cannot be null");

        CommandInfo help = cmd.getClass().getAnnotation(CommandInfo.class);
        
        if(help == null)
            throw new LauncherException("The command does not specifies its required information");
        
        String word = cmd.getWord();
        
        if(word == null)
            throw new LauncherException("The word of the command has not been specified");
        
        cmds.put(word.toLowerCase(), cmd);
    }
    
    /**
     * Gets a command by using its word.
     * @param word The word related to the command.
     * @return The command object, or null if the factory does not exists.
     */
    public Command checkCommand(String word) throws UndefinedCommandException
    {
        Command cmd = cmds.get(word.toLowerCase());
        
        if(cmd == null)
            throw new UndefinedCommandException(word);
        
        return cmd;
    }
    
    /**
     * Gets all registered commands.
     * @return An array of commands objects.
     */
    public Command[] getAllCommands()
    {
        Command[] resCmds = new Command[cmds.size()];
        int i = 0;
        
        for(String word : cmds.keySet())
        {
            resCmds[i] = cmds.get(word);
            i++;
        }
        
        return resCmds;
    }
}
