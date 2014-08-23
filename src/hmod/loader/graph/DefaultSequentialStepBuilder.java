
package hmod.loader.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.Operator;
import hmod.core.SequentialOperator;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultSequentialStepBuilder extends DirectStepBuilder implements MultiOperatorInput
{    
    private final ArrayBuilder<Operator> arrayBuilder;

    public DefaultSequentialStepBuilder()
    {
        this.arrayBuilder = array(Operator.class);
    }

    @Override
    public MultiOperatorInput addOperator(NestedBuilder<? extends Operator> operator)
    {
        arrayBuilder.elem(operator);
        return this;
    }

    @Override
    public Operator createOperator(BuildSession session)
    {
        SequentialOperator op = new SequentialOperator();
        op.setOperators(arrayBuilder.asArray().build(session));
        
        return op;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("SequentialStep");
    }
}
