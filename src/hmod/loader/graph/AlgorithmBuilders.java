
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.ProxyBuilder;
import hmod.core.Condition;
import hmod.core.DataHandler;
import hmod.core.Operator;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 */
public final class AlgorithmBuilders
{    
    private AlgorithmBuilders()
    {
    }
    
    public static <T> ArgumentFactoryBuilder<T> argFactory(Function<ArgumentList, T> factory)
    {
        return new DefaultArgumentFactoryBuilder<>(factory);
    }
    
    public static <T> ArgumentFactoryBuilder<T> argFactory(Function<ArgumentList, T> factory, NestedBuilder... args)
    {
        ArgumentFactoryBuilder<T> builder = argFactory(factory);
        
        for(NestedBuilder arg : args)
            builder.nextArg(arg);
        
        return builder;
    }
    
    public static AlgorithmBlockBuilder algorithmBlock()
    {
        return new DefaultAlgorithmBlockBuilder();
    }
    
    public static NestedBuilder<Operator> operator(OperatorFactory opFactory, NestedBuilder... args)
    {
        return argFactory(opFactory, args);
    }
    
    public static NestedBuilder<Condition> condition(ConditionFactory condFactory, NestedBuilder... args)
    {
        return argFactory(condFactory, args);
    }
    
    public static <T extends DataHandler> DataHandlerAssembler<T> dataHandlerAssembler(Class<T> handlerType)
    {
        return dataHandlerAssembler(handlerType, null);
    }
    
    public static <T extends DataHandler> DataHandlerAssembler<T> dataHandlerAssembler(Class<T> handlerType, NestedBuilder<? extends T> defaultImpl)
    {
        return new DefaultDataHandlerAssembler<>(handlerType, defaultImpl);
    }
    
    public static <T> DataHandlerSetterBuilder<T> dataHandlerSetter(NestedBuilder<T> target)
    {
        return new DefaultDataHandlerSetterBuilder<>(target);
    }
    
    public static <T extends DirectStepInput & DirectStepOutput> T directStep()
    {
        return (T) new DirectStepDelegate();
    }
    
    public static SequentialStepBuilder sequentialStep()
    {
        return ProxyBuilder.createProxyFor(SequentialStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(new DefaultSequentialStepBuilder(), MultiOperatorInput.class, NestedBuilder.class).
            build();
    }
    
    public static SubProcessStepBuilder subProcessStep()
    {
        return ProxyBuilder.createProxyFor(SubProcessStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(new DefaultSubProcessStepBuilder()).
            build();
    }
    
    public static ExtensionStepBuilder extensionStep()
    {
        return ProxyBuilder.createProxyFor(ExtensionStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(new DefaultExtensionStepBuilder()).
            build();
    }
    
    public static ConditionStepBuilder decisionStep()
    {
        return new DefaultConditionStepBuilder();
    }
    
    public static NestedBuilder<Condition> AND(NestedBuilder<Condition>... targets)
    {
        return new ANDConditionBuilder(targets);
    }
    
    public static NestedBuilder<Condition> OR(NestedBuilder<Condition>... targets)
    {
        return new ORConditionBuilder(targets);
    }
    
    public static NestedBuilder<Condition> NOT(NestedBuilder<Condition> target)
    {
        return new NOTConditionBuilder(target);
    }
    
    public static NestedBuilder<Condition> NOT(ConditionFactory condFactory, NestedBuilder... args)
    {
        return new NOTConditionBuilder(condition(condFactory, args));
    }
}
