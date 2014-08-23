
package hmod.domains.launcher.core.ac;

import flexbuilders.core.BuilderInputException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.UndefinedCommandException;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultCommandHandler implements CommandHandler
{
    private HashMap<String, Command> cmds;
    
    public void setCommands(Command[] commands)
    {
        if(cmds != null)
            throw new BuilderInputException("The commands are already setted!");
        
        if(commands == null)
            throw new NullPointerException("Null commands");

        if(commands.length == 0)
            throw new NullPointerException("No commands were provided");

        HashMap<String, Command> checkCmds = new HashMap<>(commands.length);
        
        for(int i = 0; i < commands.length; i++)
        {
            Command cmd = commands[i];

            if(cmd == null)
                throw new NullPointerException("The provided command cannot be null");

            CommandInfo help = cmd.getInfo();

            if(help == null)
                throw new IllegalArgumentException("The command '" + cmd.getClass() + "' does not specifies its required information");

            String word = help.word();

            if(word == null || word.isEmpty())
                throw new IllegalArgumentException("The word of the command '" + cmd.getClass() + "' has not been specified");

            checkCmds.put(word.toLowerCase(), cmd);
        }
        
        this.cmds = checkCmds;
    }
    
    @Override
    public boolean commandExists(String word)
    {
        return cmds.containsKey(word);
    }

    @Override
    public Command getCommand(String word) throws UndefinedCommandException
    {
        if(!cmds.containsKey(word))
            throw new UndefinedCommandException(word);
        
        return cmds.get(word.toLowerCase());
    }
    
    @Override
    public String[] getAllCommandsWords()
    {
        return cmds.keySet().toArray(new String[0]);
    }
}
