
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandBuffer
{
    /**
     * Gets the source from which this buffer was generated
     * @return The source descriptor.
     */
    String getSource();
    
    /**
     * Gets the number of added commands.
     * @return the number as int.
     */
    int count();

    /**
     * Gets the description of the active mark.
     * @return The description as String, or null if it has not been specified.
     */
    String getMarkDescription();

    /**
     * Gets the max. reset of the active mark.
     * @return the max. as integer, or -1 if the buffer is not marked.
     */
    int getMarkResetLimit();
    
    /**
     * Gets the reset count of the active mark.
     * @return the count as integer, or -1 if the buffer is not marked.
     */
    int getMarkResetCount();

    /**
     * Checks if the buffer is empty.
     * @return true if is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if the buffer's current position is at end.
     * @return true if is finished, false otherwise.
     */
    boolean isFinished();

    /**
     * Checks if there is an active mark on the buffer.
     * @return true if a mark exists, false otherwise.
     */
    boolean isMarked();

    /**
     * Gets the current command execution and advances a position within the
     * buffer.
     * @return The current command execution, or null if the position is at the
     * buffer's end.
     */
    CommandRunner next();

    /**
     * Pops the latter mark added into de buffer.
     * @return true if the mark was successful poped, false if there are no
     * marks added.
     */
    boolean popMark();

    /**
     * Push a new command execution into the buffer.
     * @param cmd The info bean.
     */
    void pushCommand(CommandRunner cmd);

    /**
     * Push a new mark into the buffer with no reset limit and no description.
     * @return true if the mark was successful pushed, false if the buffer is
     * empty.
     */
    boolean pushMark();

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
    boolean pushMark(int resetLimit, String desc);

    /**
     * Reset the buffer's current position to the last mark added if its max.
     * reset count has not been reached, or to the first position if no mark has
     * been added.
     * @return true if the reposition was successful, or false if there is a
     *  current mark and the reset limit has been already reached.
     */
    boolean reset();
    
    /**
     * Gets the variable collection which is used in this buffer
     * @return the variable collection instance.
     */
    VariableHandler getVariables();
}
