
package hmod.domains.launcher.core.ac;

import hmod.domains.launcher.core.DefaultVariableCollection;
import hmod.domains.launcher.core.VariableCollection;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultVariableHandler implements VariableHandler
{
    /**
     * The table that store the custom variables.
     */
    private final VariableCollection mainCollection;
    private CommandBufferHandler commandBufferHandler;

    public DefaultVariableHandler()
    {
        mainCollection = new DefaultVariableCollection();
    }

    public void setCommandBufferHandler(CommandBufferHandler commandBufferHandler)
    {
        this.commandBufferHandler = commandBufferHandler;
    }

    @Override
    public VariableCollection getMainVariables()
    {
        return mainCollection;
    }

    @Override
    public VariableCollection getActiveVariables()
    {
        if(commandBufferHandler.existsCurrentBuffer())
            return commandBufferHandler.getVariableCollectionFromBuffer();
        
        return mainCollection;
    }
}
