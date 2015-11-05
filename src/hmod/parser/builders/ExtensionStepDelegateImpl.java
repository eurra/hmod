
package hmod.parser.builders;

import static hmod.parser.builders.AlgorithmBuilders.*;
import flexbuilders.core.BuildException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
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
public class ExtensionStepDelegateImpl extends DirectStepBuildable implements ExtensionStepInput
{
    private static class ExtensionStepOperator implements Operator
    {
        private Algorithm[] subProcedures;

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
    
    private List<Buildable> extensions;

    public ExtensionStepDelegateImpl()
    {
        extensions = new ArrayList<>();
    }

    @Override
    public int getExtensionsCount()
    {
        return extensions.size();
    }

    @Override
    public ExtensionStepInput addFirst(Buildable<Step> extension) throws BuildException
    {
        if(extension != null)
            extensions.add(0, extension);
        
        return this;
    }

    @Override
    public ExtensionStepInput addLast(Buildable<Step> extension) throws BuildException
    {
        if(extension != null)
            extensions.add(extensions.size(), extension);
        
        return this;
    }

    @Override
    public ExtensionStepInput addAt(Buildable<Step> extension, int pos) throws BuildException
    {
        if(extension != null)
        {
            try
            {
                extensions.add(pos, extension);
            }
            catch(IndexOutOfBoundsException ex)
            {
                throw new BuildException("The specified position (" + pos + ") is not valid", ex);
            }
        }
        
        return this;
    }

    @Override
    public Operator getDirectOperator() throws BuildException
    {
        Algorithm[] subProcedures = new Algorithm[extensions.size()];
        int i = 0;
        
        for(Buildable<Step> buildableStep : extensions)
            subProcedures[i++] = algorithm().startWith(buildableStep).build();
        
        return new ExtensionStepOperator(subProcedures);
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ExtensionStep");
    }
}
