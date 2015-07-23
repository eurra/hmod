
package hmod.core;

import static hmod.core.FlowchartFactory.expandBlocks;
import java.util.Arrays;

/**
 *
 * @author Enrique Urra C.
 */
public final class ArrayBlock extends SequentialBlock
{
    private final Statement[] finalBlocks;
    
    public ArrayBlock(Statement... blocks)
    {
        this.finalBlocks = expandBlocks(blocks);
    }

    @Override
    public Statement getChildAt(int pos) throws IndexOutOfBoundsException
    {
        return finalBlocks[pos];
    }

    @Override
    public int getChildsCount()
    {
        return finalBlocks.length;
    }

    @Override
    public Statement[] getChilds()
    {
        return Arrays.copyOf(finalBlocks, finalBlocks.length);
    }
}
