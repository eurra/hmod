
package hmod.launcher;

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
    private CommandInfo info;
    /**
     * Indicates if the command is enabled/available.
     */
    private boolean enabled;
    
    /**
     * Default constructor.
     */
    public Command()
    {
        info = this.getClass().getAnnotation(CommandInfo.class);
        enabled = true;
    }
    
    /**
     * Gets the command's word.
     * @return The word as String.
     */
    public String getWord()
    {
        if(info == null)
            return "(none)";
        
        return info.word();
    }
    
    /**
     * Gets the command's usage.
     * @return The usage as String.
     */
    public String getUsage()
    {
        if(info == null)
            return "(none)";
        
        return info.usage();
    }
    
    /**
     * Gets the command's description.
     * @return The description as String.
     */
    public String getDescription()
    {
        if(info == null)
            return "(none)";
        
        return info.description();
    }
    
    /**
     * Enables the command.
     */
    public void enable()
    {
        this.enabled = true;
    }
    
    /**
     * Disables de command.
     */
    public void disable()
    {
        this.enabled = false;
    }
    
    /**
     * Checks if the command is enabled.
     * @return true if is enabled, false otherwise.
     */
    public boolean isEnabled()
    {
        return enabled;
    }
    
    /**
     * Runs the command using the provided arguments
     * @param args The arguments to use.
     * @throws LauncherException if an error occurs during the execution.
     */
    public abstract void executeCommand(String[] args) throws LauncherException;
}
