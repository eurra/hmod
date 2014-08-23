
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.CommandRunner;
import hmod.domains.launcher.core.LauncherException;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandParseHandler extends DataHandler
{
    CommandRunner parseNextCommand(String input) throws LauncherException;
    CommandRunner getCommandCurrentlyParsed() throws DataHandlingException;
}
