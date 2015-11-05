
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public class DeciderDelegateImpl implements DeciderInput, DeciderOutput
{
    private Buildable decider;

    @Override
    public DeciderInput setDecider(Buildable<? extends BooleanOperator> decider) throws BuildException
    {
        this.decider = decider;
        return this;
    }

    @Override
    public Buildable<BooleanOperator> getDecider()
    {
        return decider;
    }
}
