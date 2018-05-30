
package hmod.core;

import static hmod.core.FlowchartFactory.expandBlocks;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import optefx.loader.ModuleLoadException;

/**
 *
 * @author Enrique Urra C.
 */
public final class ValidableRoutine implements Routine
{
    private Statement[] finalBlocks;
    private List<Statement> before = new ArrayList<>();
    private List<Statement> after = new ArrayList<>();
    private Statement main;
    private final List<UnaryOperator<Statement>> mainMutators = new ArrayList<>();
    private final String id;
    private final boolean requireNonEmpty;

    public ValidableRoutine(String id)
    {
        this(id, true);
    }

    public ValidableRoutine(String id, boolean requireNonEmpty)
    {
        this.requireNonEmpty = requireNonEmpty;
        this.id = id;
    }
    
    private boolean addAsNew(Statement block)
    {
        Objects.requireNonNull(block, "null block");

        if(main == null)
        {
            main = block;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private boolean isReady()
    {
        return finalBlocks != null;
    }
    
    @Override
    public Routine appendAfter(Statement block)
    {
        if(isReady()) return this;
        
        after.add(Objects.requireNonNull(block, "null block"));
        return this;
    }

    @Override
    public Routine appendBefore(Statement block)
    {
        if(isReady()) return this;
        
        before.add(Objects.requireNonNull(block, "null block"));
        return this;
    }

    @Override
    public final Routine append(Statement block)
    {
        if(isReady()) return this;
        
        if(!addAsNew(block))
            apply(FlowchartFactory.append(block));
        
        return this;
    }

    @Override
    public Routine prepend(Statement block)
    {
        if(isReady()) return this;
        
        if(!addAsNew(block))
            apply(FlowchartFactory.prepend(block));
        
        return this;
    }

    @Override
    public Routine prependAfter(Statement block)
    {
        if(isReady()) return this;
        
        after.add(0, Objects.requireNonNull(block, "null block"));
        return this;
    }

    @Override
    public Routine prependBefore(Statement block)
    {
        if(isReady()) return this;
        
        before.add(0, Objects.requireNonNull(block, "null block"));
        return this;
    }

    @Override
    public final Routine apply(UnaryOperator<Statement> mutator)
    {
        if(isReady()) return this;        
        mainMutators.add(Objects.requireNonNull(mutator, "null mutator"));        
        return this;
    }
    
    public final void validate() throws ModuleLoadException
    {
        if(isReady())
            return;
        
        Statement toUseImpl = main;
        
        if(toUseImpl != null)
        {
            for(UnaryOperator<Statement> mutator : mainMutators)
            {
                toUseImpl = mutator.apply(toUseImpl);

                if(toUseImpl == null)
                    throw new ModuleLoadException("Null block result in routine '" + this + "' (" + mutator + ")");
            }
        }
        
        List<Statement> finalList = new ArrayList<>(before.size() + after.size() + 1);
        finalList.addAll(before);
        
        if(toUseImpl != null)
            finalList.add(toUseImpl);
        
        finalList.addAll(after);
        
        try
        {
            finalBlocks = expandBlocks(finalList.toArray(new Statement[0]), requireNonEmpty);
        }
        catch(AlgorithmException ex)
        {
            throw new ModuleLoadException("Cannot expand the routine '" + this + "' (maybe it's incomplete?)", ex);
        }
                
        main = null;
        before = null;
        after = null;
    }

    @Override
    public String toString()
    {
        return id == null ? super.toString() : id;
    }

    @Override
    public void run() throws AlgorithmException
    {
        if(!isReady())
            throw new AlgorithmException("The block '" + this + "' has not been validated");
        
        for(Statement block : finalBlocks)
            block.run();
    }
    
    @Override
    public void stop() throws AlgorithmException
    {
        if(!isReady())
            throw new AlgorithmException("The block '" + this + "' has not been validated");
        
        for(Statement block : finalBlocks)
            block.stop();
    }
}
