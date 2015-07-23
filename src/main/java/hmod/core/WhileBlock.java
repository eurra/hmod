
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class WhileBlock extends IterationBlock
{
    public WhileBlock(Expression<Boolean> condition)
    {
        super(condition);
    }

    @Override
    public void run() throws AlgorithmException
    {
        start();
        
        while(isRunning() && getCondition().get())
            getIterationBlock().run();
        
        stop();
    }
}
