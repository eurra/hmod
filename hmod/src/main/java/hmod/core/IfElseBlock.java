
package hmod.core;

import static hmod.core.Statement.EMPTY_STATEMENT;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class IfElseBlock extends StoppableStatement implements Block
{
    private final Expression<Boolean> condition;
    private Statement trueBlock;
    private Statement falseBlock;

    public IfElseBlock(Expression<Boolean> condition)
    {
        this.condition = Objects.requireNonNull(condition, "Null condition");
    }

    public void setTrueBlock(Statement trueBlock)
    {
        this.trueBlock = trueBlock;
    }

    public void setFalseBlock(Statement falseBlock)
    {
        this.falseBlock = falseBlock;
    }
    
    public Statement getTrueBlock()
    {
        return trueBlock == null ? EMPTY_STATEMENT : trueBlock;
    }
    
    public Statement getFalseBlock()
    {
        return falseBlock == null ? EMPTY_STATEMENT : falseBlock;
    }

    @Override
    public int getChildsCount()
    {
        return 2;
    }

    @Override
    public Statement[] getChilds()
    {
        return new Statement[] {
            getTrueBlock(),
            getFalseBlock()
        };
    }

    @Override
    public void run() throws AlgorithmException
    {
        start();
        
        if(condition.get())
            getTrueBlock().run();
        else
            getFalseBlock().run();
        
        stop();
    }

    @Override
    public void stop()
    {
        getTrueBlock().stop();
        getFalseBlock().stop();
        
        super.stop();
    }
}
