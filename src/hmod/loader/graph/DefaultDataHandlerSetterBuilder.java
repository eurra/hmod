
package hmod.loader.graph;

import static flexbuilders.basic.BasicBuilders.setFor;
import flexbuilders.basic.SetterInvokeBuilder;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultDataHandlerSetterBuilder<T> extends AbstractBuilder<T> implements DataHandlerSetterBuilder<T>
{
    private SetterInvokeBuilder<T> target;

    public DefaultDataHandlerSetterBuilder(NestedBuilder<T> target)
    {
        if(target == null)
            throw new NullPointerException();
        
        this.target = setFor(target);
    }
    
    @Override
    public DataHandlerSetterBuilder<T> setData(NestedBuilder<? extends DataHandler>... handlers)
    {
        target.values(handlers);
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        session.registerResult(this, target.build(session));
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("DataHandlerSetter");
    }
}
