
package hmod.launcher;

/**
 * Defines a component through which launcher text variables can be processed
 * according to different formats and logic.
 * @author Enrique Urra C.
 */
public interface TextVariableProcessor
{
    /**
     * Process an input variable, returning its related replacement.
     * @param input The variable's input string.
     * @return The replacement string, or null if no replacement was found.
     * @throws LauncherException if an error occurs in the process.
     */
    String process(String input) throws LauncherException;
}
