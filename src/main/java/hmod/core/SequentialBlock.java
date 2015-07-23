
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class SequentialBlock extends StoppableStatement implements Block
{    
    @Override
    public final void run() throws AlgorithmException
    {
        int count = getChildsCount();
        start();
        
        for(int i = 0; isRunning() && i < count; i++)
            getChildAt(i).run();
        
        stop();
    }

    @Override
    public void stop()
    {
        int count = getChildsCount();
        
        for(int i = 0; isRunning() && i < count; i++)
            getChildAt(i).stop();
        
        super.stop();
    }
    
    public abstract Statement getChildAt(int pos) throws IndexOutOfBoundsException;
}
