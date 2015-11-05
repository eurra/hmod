
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.ProxyBuilder;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public class AlgorithmBuilders
{    
    public static AlgorithmBuilder algorithm() throws BuildException
    {
        return new AlgorithmBuilderImpl();
    }
    
    public static <T extends Operator> OperatorBuilder<T> operator(Class<T> opType) throws BuildException
    {
        return new OperatorBuilderImpl<>(opType);
    }
    
    public static <T extends DeciderInput & DeciderOutput> T deciderStep() throws BuildException
    {
        return (T) new DeciderDelegateImpl();
    }
    
    public static <T extends MultiDeciderInput & MultiDeciderOutput> T multiDeciderStep() throws BuildException
    {
        return (T) new MultiDeciderDelegateImpl();
    }
    
    public static <T extends DirectStepInput & DirectStepOutput> T directStep() throws BuildException
    {
        return (T) new DirectStepDelegateImpl();
    }
    
    public static <T extends DecisionStepInput & DecisionStepOutput> T decisionStep() throws BuildException
    {
        return (T) new DecisionStepDelegateImpl();
    }
    
    public static <T extends SingleOperatorInput & SingleOperatorOutput> T singleOperatorStep() throws BuildException
    {
        return (T) new SingleOperatorDelegateImpl();
    }
    
    public static <T extends MultiOperatorInput & MultiOperatorOutput> T multiOperatorStep() throws BuildException
    {
        return (T) new MultiOperatorDelegateImpl();
    }
    
    public static SequentialStepBuilder sequentialStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(SequentialStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(multiOperatorStep(), MultiOperatorInput.class, MultiOperatorOutput.class).
            addDelegate(new SequentialStepBuildableImpl()).
            build();
    }
    
    public static SubProcessStepBuilder subProcessStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(SubProcessStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(new SubProcessStepDelegateImpl()).
            build();
    }
    
    public static ExtensionStepBuilder extensionStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(ExtensionStepBuilder.class).
            addDelegate(directStep(), DirectStepInput.class, DirectStepOutput.class).
            addDelegate(new ExtensionStepDelegateImpl()).
            build();
    }
    
    public static BooleanStepBuilder booleanStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(BooleanStepBuilder.class).
            addDelegate(decisionStep(), DecisionStepInput.class, DecisionStepOutput.class).
            addDelegate(deciderStep(), DeciderInput.class, DeciderOutput.class).
            addDelegate(multiOperatorStep(), MultiOperatorInput.class, MultiOperatorOutput.class).
            addDelegate(new BooleanStepBuildableImpl()).
            build();
    }
    
    public static ANDStepBuilder ANDStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(ANDStepBuilder.class).
            addDelegate(decisionStep(), DecisionStepInput.class, DecisionStepOutput.class).
            addDelegate(multiDeciderStep(), MultiDeciderInput.class, MultiDeciderOutput.class).
            addDelegate(new ANDStepBuildableImpl()).
            build();
    }
    
    public static ORStepBuilder ORStep() throws BuildException
    {
        return ProxyBuilder.createProxyFor(ORStepBuilder.class).
            addDelegate(decisionStep(), DecisionStepInput.class, DecisionStepOutput.class).
            addDelegate(multiDeciderStep(), MultiDeciderInput.class, MultiDeciderOutput.class).
            addDelegate(new ORStepBuildableImpl()).
            build();
    }
}
