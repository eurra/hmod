
package hmod.core;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class IterationBlock extends StoppableStatement implements Block
{
    private final Expression<Boolean> condition;
    private Statement iterationBlock;

    public IterationBlock(Expression<Boolean> condition)
    {
        this.condition = Objects.requireNonNull(condition, "null condition");
    }

    protected final Expression<Boolean> getCondition()
    {
        return condition;
    }

    public final void setIterationBlock(Statement iterationBlock)
    {
        this.iterationBlock = iterationBlock;
    }

    protected final Statement getIterationBlock()
    {
        if(iterationBlock == null)
            throw new AlgorithmException("The iteration block has not been set");
        
        return iterationBlock;
    }

    @Override
    public void stop()
    {
        if(iterationBlock != null)
            iterationBlock.stop();
        
        super.stop();
    }

    @Override
    public Statement[] getChilds()
    {
        return new Statement[] { getIterationBlock() };
    }

    @Override
    public final int getChildsCount()
    {
        return 1;
    }
}
