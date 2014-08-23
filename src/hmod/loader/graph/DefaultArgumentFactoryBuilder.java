
package hmod.loader.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuilderInputException;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultArgumentFactoryBuilder<T> extends AbstractBuilder<T> implements ArgumentFactoryBuilder<T>
{
    private final Function<ArgumentList, T> factory;
    private final ArrayBuilder<Object> args;

    public DefaultArgumentFactoryBuilder(Function<ArgumentList, T> factory)
    {
        if(factory == null)
            throw new NullPointerException("Null factory");
        
        this.factory = factory;
        this.args = array(Object.class);
    }

    @Override
    public ArgumentFactoryBuilder<T> nextArg(NestedBuilder arg)
    {
        if(arg == null)
            throw new BuilderInputException("null argument");
        
        args.elem(arg);
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        Object[] finalArgs = args.asArray().build(session);
        DefaultArgumentsList argList = new DefaultArgumentsList(finalArgs);
        session.registerResult(this, factory.apply(argList));
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ArgumentFactory");
    }
}
