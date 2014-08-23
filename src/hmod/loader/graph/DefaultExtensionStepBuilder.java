
package hmod.loader.graph;

import flexbuilders.core.BuildSession;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.core.Step;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultExtensionStepBuilder extends DirectStepBuilder implements ExtensionStepInput
{
    private static class ExtensionStepOperator implements Operator
    {
        private final Algorithm[] subProcedures;

        public ExtensionStepOperator(Algorithm[] subProcedures)
        {
            this.subProcedures = subProcedures;
        }
        
        @Override
        public void execute() throws AlgorithmException
        {
            for(int i = 0; i < subProcedures.length; i++)
                subProcedures[i].start();
        }
    }
    
    private List<NestedBuilder> extensions;

    public DefaultExtensionStepBuilder()
    {
        extensions = new ArrayList<>();
    }

    @Override
    public int getExtensionsCount()
    {
        return extensions.size();
    }

    @Override
    public ExtensionStepInput addFirst(NestedBuilder<Step> extension)
    {
        if(extension != null)
            extensions.add(0, extension);
        
        return this;
    }

    @Override
    public ExtensionStepInput addLast(NestedBuilder<Step> extension)
    {
        if(extension != null)
            extensions.add(extensions.size(), extension);
        
        return this;
    }

    @Override
    public ExtensionStepInput addAt(NestedBuilder<Step> extension, int pos)
    {
        if(extension != null)
        {
            try
            {
                extensions.add(pos, extension);
            }
            catch(IndexOutOfBoundsException ex)
            {
                throw new BuilderInputException("The specified position (" + pos + ") is not valid", ex);
            }
        }
        
        return this;
    }
    
    @Override
    public Operator createOperator(BuildSession session)
    {
        Algorithm[] subProcedures = new Algorithm[extensions.size()];
        int i = 0;
        
        for(NestedBuilder<Step> buildableStep : extensions)
            subProcedures[i++] = Algorithm.create(buildableStep.build(session));
        
        return new ExtensionStepOperator(subProcedures);
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ExtensionStep");
    }
}
