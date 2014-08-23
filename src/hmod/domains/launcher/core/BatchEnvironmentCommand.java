
package hmod.domains.launcher.core;

import hmod.domains.launcher.core.ac.CommandBufferHandler;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class BatchEnvironmentCommand extends Command
{
    private CommandBufferHandler bufferHandler;

    public void setBufferStack(CommandBufferHandler bufferStack)
    {
        this.bufferHandler = bufferStack;
    }

    public CommandBufferHandler getBufferHandler()
    {
        if(bufferHandler == null)
            throw new NullPointerException("Null buffer stack");
        
        return bufferHandler;
    }

    @Override
    public boolean isEnabled()
    {
        if(bufferHandler == null)
            throw new NullPointerException("Null buffer stack");
        
        return bufferHandler.existsCurrentBuffer();
    }
}
