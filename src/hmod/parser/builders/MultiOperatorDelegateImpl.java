
package hmod.parser.builders;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
class MultiOperatorDelegateImpl implements MultiOperatorInput, MultiOperatorOutput
{
    private ArrayBuilder<Operator> arrayBuilder;

    public MultiOperatorDelegateImpl() throws BuildException
    {
        this.arrayBuilder = array(Operator.class);
    }

    @Override
    public MultiOperatorInput addOperator(Buildable<? extends Operator> operator) throws BuildException
    {
        arrayBuilder.elem(operator);
        return this;
    }

    @Override
    public Operator[] getOperators() throws BuildException
    {
        return arrayBuilder.asArray().build();
    }
}
