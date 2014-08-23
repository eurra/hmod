
package hmod.domains.launcher.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public final class FileBuffer implements CommandBuffer
{
    private static class MarkInfo
    {
        int pos;
        int resetLimit;
        int resetCount;
        String desc;

        public MarkInfo(int pos, int resetLimit, String desc)
        {
            this.pos = pos;
            this.resetLimit = resetLimit;
            this.resetCount = 1;
            this.desc = desc;
        }
    }
    
    /**
     * The buffer source
     */
    private final String source;
    /**
     * The stack of executable commands.
     */
    private final List<CommandRunner> cmdStack;
    /**
     * The stack of marks.
     */
    private final List<MarkInfo> markStack;
    /**
     * The buffer current position.
     */
    private int currIndex;
    /**
     * The variable collection.
     */
    private final VariableCollection vars; 

    
    public FileBuffer(String source, VariableCollection vars)
    {
        if(source == null)
            throw new NullPointerException("Null source");

        if(vars == null)
            throw new NullPointerException("Null variables");

        this.source = source;
        this.cmdStack = new ArrayList<>();
        this.markStack = new ArrayList<>();
        this.currIndex = 0;
        this.vars = vars;
    }

    @Override
    public String getSource()
    {
        return source;
    }
    
    /**
     * Checks if the buffer is empty.
     * @return true if is empty, false otherwise.
     */
    @Override
    public boolean isEmpty()
    {
        return cmdStack.isEmpty();
    }

    /**
     * Push a new command execution into the buffer.
     * @param cmd The info bean.
     */
    @Override
    public void pushCommand(CommandRunner cmd)
    {
        if(cmd == null)
            return;

        cmdStack.add(cmd);
    }

    /**
     * Checks if the buffer's current position is at end.
     * @return true if is finished, false otherwise.
     */
    @Override
    public boolean isFinished()
    {
        return currIndex == cmdStack.size();
    }

    /**
     * Gets the current command execution and advances a position within the
     * buffer.
     * @return The current command execution, or null if the position is at the
     * buffer's end.
     */
    @Override
    public CommandRunner next()
    {
        if(isFinished())
            return null;

        CommandRunner cmdRunner = cmdStack.get(currIndex++);
        return cmdRunner;
    }

    /**
     * Push a new mark into the buffer with no reset limit and no description.
     * @return true if the mark was successful pushed, false if the buffer is
     * empty.
     */
    @Override
    public boolean pushMark()
    {
        return pushMark(0, null);
    }

    /**
     * Push a new mark into the buffer. The mark stores the current position, 
     * then the latter can be restores by using the reset() method.
     * A number of max. resets can be specified for the mark so when the reset 
     * number is reached, the last reset does not have effect. If the limit is
     * not specified (value zero) then the reset will always work.
     * @param resetLimit The max. resets available for the mark. Use 0 for
     *  specify no limit on this value.
     * @return true if the mark was successful pushed, false if the buffer is
     * empty.
     */
    @Override
    public boolean pushMark(int resetLimit, String desc)
    {
        if(isEmpty())
            return false;

        if(resetLimit < 0)
            throw new IllegalArgumentException("The reset limit must be greater or equal to zero.");

        markStack.add(new MarkInfo(currIndex, resetLimit, desc));
        return true;
    }

    /**
     * Gets the description of the active mark.
     * @return The description as String, or null if it has not been specified.
     */
    @Override
    public String getMarkDescription()
    {
        if(!isMarked())
            return null;

        return markStack.get(markStack.size() - 1).desc;
    }

    /**
     * Gets the max. reset of the active mark.
     * @return the max. as integer, or -1 if the buffer is not marked.
     */
    @Override
    public int getMarkResetLimit()
    {
        if(!isMarked())
            return -1;

        return markStack.get(markStack.size() - 1).resetLimit;
    }

    /**
     * Gets the reset count of the active mark.
     * @return the count as integer, or -1 if the buffer is not marked.
     */
    @Override
    public int getMarkResetCount()
    {
        if(!isMarked())
            return -1;

        return markStack.get(markStack.size() - 1).resetCount;
    }

    /**
     * Pops the latter mark added into de buffer.
     * @return true if the mark was successful poped, false if there are no 
     * marks added.
     */
    @Override
    public boolean popMark()
    {
        if(!isMarked())
            return false;

        markStack.remove(markStack.size() - 1);
        return true;
    }

    /**
     * Reset the buffer's current position to the last mark added if its max. 
     * reset count has not been reached, or to the first position if no mark has
     * been added.
     * @return true if the reposition was successful, or false if there is a
     *  current mark and the reset limit has been already reached.
     */
    @Override
    public boolean reset()
    {
        if(!isMarked())
        {
            currIndex = 0;
            return true;
        }

        MarkInfo info = markStack.get(markStack.size() - 1);

        if(info.resetLimit > 0)
        {            
            if(info.resetCount >= info.resetLimit)
                return false;
            else
                info.resetCount++;
        }

        currIndex = info.pos;
        return true;
    }

    /**
     * Checks if there is an active mark on the buffer.
     * @return true if a mark exists, false otherwise.
     */
    @Override
    public boolean isMarked()
    {
        return !markStack.isEmpty();
    }

    /**
     * Gets the number of added commands.
     * @return the number as int.
     */
    @Override
    public int count()
    {
        return cmdStack.size();
    }

    @Override
    public VariableCollection getVariables()
    {
        return vars;
    }
}
