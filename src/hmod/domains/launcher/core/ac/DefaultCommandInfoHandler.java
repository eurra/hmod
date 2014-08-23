
package hmod.domains.launcher.core.ac;

import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.UndefinedCommandException;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultCommandInfoHandler implements CommandInfoHandler
{
    private CommandHandler commandHandler;

    public void setCommandHandler(CommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandInfo getInfoForCommand(String word) throws UndefinedCommandException
    {
        Command cmd = commandHandler.getCommand(word);
        return cmd.getInfo();
    }

    @Override
    public CommandInfo[] getAllCommandInfos()
    {
        String[] allCmds = commandHandler.getAllCommandsWords();
        CommandInfo[] resInfos = new CommandInfo[allCmds.length];
        int i = 0;

        for(String word : allCmds)
        {
            resInfos[i] = commandHandler.getCommand(word).getInfo();
            i++;
        }

        return resInfos;
    }
}
