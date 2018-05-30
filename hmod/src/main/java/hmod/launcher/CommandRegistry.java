
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandRegistry
{
    CommandRegistry addCommand(Command cmd);
    CommandRegistry addCommands(Command... cmds);
    boolean commandExists(String word);
}
