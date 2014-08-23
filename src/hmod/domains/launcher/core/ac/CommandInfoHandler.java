
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.UndefinedCommandException;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandInfoHandler extends DataHandler
{
    CommandInfo getInfoForCommand(String word) throws UndefinedCommandException;
    CommandInfo[] getAllCommandInfos();
}