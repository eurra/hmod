
package hmod.parser.builders;

import static flexbuilders.basic.BasicBuilders.*;
import flexbuilders.basic.Setter;
import static flexbuilders.basic.SetterBuilders.*;
import flexbuilders.basic.SetterInvokerBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.StackBuildable;
import hmod.core.DataInterface;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
class OperatorBuilderImpl<T extends Operator> extends StackBuildable<T> implements OperatorBuilder<T>
{
    private T op;
    private Class opType;
    private SetterInvokerBuilder<T> setterInvokerBuilder;

    public OperatorBuilderImpl(Class<T> opType) throws BuildException
    {
        this.setterInvokerBuilder = setterInvoker(object(opType));
        this.opType = opType;
    }

    @Override
    public OperatorBuilder injectData(Buildable<? extends DataInterface> data) throws BuildException
    {
        return injectData(beanSetter(), data);
    }

    @Override
    public OperatorBuilder injectData(Buildable<? extends Setter> setter, Buildable<? extends DataInterface> dataImpl) throws BuildException
    {
        setterInvokerBuilder.set(setter, dataImpl);
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        op = setterInvokerBuilder.build();
    }

    @Override
    public T getInstance() throws BuildException
    {
        return op;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Operator").
            addStateData("type", opType.getSimpleName()
        );
    }
}
