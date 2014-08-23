
package hmod.domains.launcher.core.ac;

import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.FileBuffer;
import hmod.domains.launcher.core.VariableCollection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultCommandBufferHandler implements CommandBufferHandler
{
    /**
    * The stack of additional buffers
    */
   private final List<CommandBuffer> bufferStack;
   private VariableHandler variableHandler;

    public DefaultCommandBufferHandler()
    {
        this.bufferStack = new ArrayList<>();
    }

    public void setVariableHandler(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }

    /**
     * Checks if there is a buffer added to the stack.
     * @return true if exists a buffer, false otherwise.
     */
    @Override
    public boolean existsCurrentBuffer()
    {
        return !bufferStack.isEmpty();
    }

    /**
     * Gets the current buffer from the stack, without removing it.
     * @return The buffer object, or null if the stack is empty.
     */
    @Override
    public CommandBuffer getCurrentBuffer()
    {
        if(!existsCurrentBuffer())
            return null;

        return bufferStack.get(bufferStack.size() - 1);
    }

    /**
     * Push a new additional buffer into de stack.
     * @return The added buffer object.
     */
    @Override
    public CommandBuffer pushBuffer(String source)
    {
        VariableCollection varColToUse;

        if(!bufferStack.isEmpty())
            varColToUse = bufferStack.get(bufferStack.size() - 1).getVariables();
        else
            varColToUse = variableHandler.getMainVariables();

        CommandBuffer buffer = new FileBuffer(source, varColToUse);
        bufferStack.add(buffer);

        return buffer;
    }

    /**
     * Pops the current buffer from the stack.
     * @return true if the buffer was poped, false if no buffers have been 
     * added previously.
     */
    @Override
    public boolean popBuffer()
    {
        if(!existsCurrentBuffer())
            return false;

        bufferStack.remove(bufferStack.size() - 1);
        return true;
    }

    @Override
    public VariableCollection getVariableCollectionFromBuffer()
    {
        if(!existsCurrentBuffer())
            return null;
        
        return bufferStack.get(bufferStack.size() - 1).getVariables();
    }    
}
