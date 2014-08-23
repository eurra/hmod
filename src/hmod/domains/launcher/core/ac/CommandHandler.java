
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.UndefinedCommandException;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandHandler extends DataHandler
{
    boolean commandExists(String word);
    String[] getAllCommandsWords();
    Command getCommand(String word) throws UndefinedCommandException;
}
