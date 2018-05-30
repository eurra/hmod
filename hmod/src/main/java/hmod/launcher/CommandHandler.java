
package hmod.launcher;

import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
final class CommandHandler implements CommandRegistry
{
    private final HashMap<String, Command> cmds = new HashMap<>();
    
    @Override
    public CommandHandler addCommands(Command... cmds)
    {
        for(Command cmd : cmds)
            addCommand(cmd);
        
        return this;
    }
    
    @Override
    public CommandHandler addCommand(Command cmd)
    {
        if(cmd == null)
            throw new NullPointerException("The provided command cannot be null");

        CommandInfo help = cmd.getInfo();

        if(help == null)
            throw new IllegalArgumentException("The command '" + cmd.getClass() + "' does not specifies its required information");

        String word = help.word();

        if(word == null || word.isEmpty())
            throw new IllegalArgumentException("The word of the command '" + cmd.getClass() + "' has not been specified");

        if(cmds.containsKey(word))
            throw new IllegalArgumentException("The word of the command '" + cmd.getClass() + "' is already registered");
        
        cmds.put(word, cmd);
        return this;
    }
    
    @Override
    public boolean commandExists(String word)
    {
        return cmds.containsKey(word);
    }
    
    public boolean commandIsEnabled(String word)
    {
        return commandExists(word) && cmds.get(word).isEnabled();
    }
    
    private Command getExistentCommand(String word) throws UndefinedCommandException
    {
        if(!commandExists(word))
            throw new UndefinedCommandException(word);
        
        return cmds.get(word);
    }

    public Command getCommand(String word) throws UndefinedCommandException, UnavailableCommandException
    {
        Command cmd = getExistentCommand(word);
        
        if(!cmd.isEnabled())
            throw new UnavailableCommandException(word);
        
        return cmd;
    }
    
    public String[] getAllCommandsWords()
    {
        return cmds.keySet().toArray(new String[0]);
    }
    
    public CommandInfo getInfoForCommand(String word) throws UndefinedCommandException
    {
        Command cmd = getExistentCommand(word);
        return cmd.getInfo();
    }

    public CommandInfo[] getAllCommandInfos()
    {
        String[] allCmds = getAllCommandsWords();
        CommandInfo[] resInfos = new CommandInfo[allCmds.length];
        int i = 0;

        for(String word : allCmds)
        {
            resInfos[i] = getExistentCommand(word).getInfo();
            i++;
        }

        return resInfos;
    }
}
