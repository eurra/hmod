
package hmod.launcher;

/**
 * Defines a component through which launcher variables can be processed
 * according to different formats and logic.
 * @author Enrique Urra C.
 */
public interface VariableProcessor
{
    /**
     * Process an input variable, replacing it with the generated result.
     * @param input The variable's input string.
     * @return The string that results from the process.
     * @throws LauncherException if an error occurs in the process.
     */
    String process(String input) throws LauncherException;
}
