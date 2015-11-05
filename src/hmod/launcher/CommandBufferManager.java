
package hmod.launcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a manager for command buffers. Multiple buffers can be stored in
 * a stack structure, therefore a single buffer can be defined even if another
 * buffer is currently active.
 * @author Enrique Urra C.
 */
public class CommandBufferManager
{
    /**
     * The stack of additional buffers
     */
    private List<CommandBuffer> bufferStack;

    /**
     * Default constructor.
     */
    public CommandBufferManager()
    {
        bufferStack = new ArrayList<>();
    }
    
    /**
     * Checks if there is a buffer added to the stack.
     * @return true if exists a buffer, false otherwise.
     */
    public boolean existsBuffer()
    {
        return !bufferStack.isEmpty();
    }
    
    /**
     * Gets the current buffer from the stack, without removing it.
     * @return The buffer object, or null if the stack is empty.
     */
    public CommandBuffer getBuffer()
    {
        if(!existsBuffer())
            return null;
        
        return bufferStack.get(bufferStack.size() - 1);
    }
    
    /**
     * Push a new additional buffer into de stack.
     * @return The added buffer object.
     */
    public CommandBuffer pushBuffer()
    {
        CommandBuffer buffer = new CommandBuffer();
        bufferStack.add(buffer);
        
        return buffer;
    }
    
    /**
     * Pops the current buffer from the stack.
     * @return true if the buffer was poped, false if no buffers have been 
     * added previously.
     */
    public boolean popBuffer()
    {
        if(!existsBuffer())
            return false;
        
        bufferStack.remove(bufferStack.size() - 1);
        return true;
    }
}
