
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class RepeatUntilBlock extends IterationBlock
{
    public RepeatUntilBlock(Expression<Boolean> condition)
    {
        super(condition);
    }

    @Override
    public void run() throws AlgorithmException
    {
        start();
        
        do
        {
            getIterationBlock().run();
        }
        while(isRunning() && !getCondition().get());
        
        stop();
    }
}
