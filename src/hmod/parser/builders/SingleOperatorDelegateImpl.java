
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
class SingleOperatorDelegateImpl implements SingleOperatorInput, SingleOperatorOutput
{
    private Buildable operatorBuildable;

    @Override
    public SingleOperatorInput setOperator(Buildable<? extends Operator> operator) throws BuildException
    {
        operatorBuildable = operator;
        return this;
    }

    @Override
    public Buildable<Operator> getOperator()
    {
        return operatorBuildable;
    }
}
