
package hmod.launcher;

import java.lang.annotation.Annotation;

/**
 * Defines an executable command that can be performed over a launcher and 
 * its components.
 * @author Enrique Urra C.
 */
public abstract class Command
{
    /**
     * The internal info configured.
     */
    private final CommandInfo info;
    
    /**
     * Default constructor.
     */
    public Command()
    {
        CommandInfo checkInfo = this.getClass().getAnnotation(CommandInfo.class);
        
        if(checkInfo == null)
        {
            checkInfo = new CommandInfo()
            {
                @Override
                public String word()
                {
                    return null;
                }

                @Override
                public String usage()
                {
                    return "(none)";
                }

                @Override
                public String description()
                {
                    return "(none)";
                }

                @Override
                public Class<? extends Annotation> annotationType()
                {
                    return CommandInfo.class;
                }
            };
        }
        
        info = checkInfo;
    }
    
    /**
     * Gets the command's info.
     * @return The word as String.
     */
    public final CommandInfo getInfo()
    {
        return info;
    }
    
    /**
     * Checks if the command is enabled.
     * @return true if is enabled, false otherwise.
     */
    public boolean isEnabled()
    {
        return true;
    }
    
    /**
     * Runs the command using the provided arguments
     * @param args The arguments to use.
     * @throws LauncherException if an error occurs during the execution.
     */
    public abstract void executeCommand(CommandArgs args) throws LauncherException;
}
