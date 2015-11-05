
package hmod.parser.builders;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public class MultiDeciderDelegateImpl implements MultiDeciderInput, MultiDeciderOutput
{
    private ArrayBuilder<BooleanOperator> arrayBuilder;

    public MultiDeciderDelegateImpl() throws BuildException
    {
        this.arrayBuilder = array(BooleanOperator.class);
    }

    @Override
    public MultiDeciderInput addDecider(Buildable<? extends BooleanOperator> decider) throws BuildException
    {
        arrayBuilder.elem(decider);
        return this;
    }

    @Override
    public BooleanOperator[] getDeciders() throws BuildException
    {
        return arrayBuilder.asArray().build();
    }
}
