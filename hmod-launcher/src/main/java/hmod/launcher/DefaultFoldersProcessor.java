
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultFoldersProcessor implements TextVariableProcessor
{
    private final LauncherControl launcherHandler;

    public DefaultFoldersProcessor(LauncherControl launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {
        if(input.equals("SCRIPT_LOC"))
            return launcherHandler.getScriptBaseFolder();
        
        if(input.equals("OUTPUT_LOC"))
            return launcherHandler.getOutputBaseFolder();
        
        return null;
    }
}
