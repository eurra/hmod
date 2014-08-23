
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Condition;
import hmod.core.Operator;
import hmod.core.Step;
import static hmod.loader.graph.AlgorithmBuilders.condition;
import static hmod.loader.graph.AlgorithmBuilders.operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmBlockBuilder extends NestedBuilder<Step>, AlgorithmBlockInput, DirectStepInput
{
    default AlgorithmBlockBuilder run(OperatorFactory opFactory, NestedBuilder... args)
    {
        return run(operator(opFactory, args));
    }
    
    default AlgorithmBlockBuilder If(ConditionFactory condFactory, NestedBuilder... args)
    {
        return If(condition(condFactory, args));
    }
    
    default AlgorithmBlockBuilder elseIf(ConditionFactory condFactory, NestedBuilder... args)
    {
        return elseIf(condition(condFactory, args));
    }
    
    default AlgorithmBlockBuilder until(ConditionFactory condFactory, NestedBuilder... args)
    {
        return until(condition(condFactory, args));
    }
    
    default AlgorithmBlockBuilder While(ConditionFactory condFactory, NestedBuilder... args)
    {
        return While(condition(condFactory, args));
    }
    
    @Override AlgorithmBlockBuilder run(NestedBuilder<Operator> opBuilder);
    @Override AlgorithmBlockBuilder call(NestedBuilder<Step> subProcess);
    @Override AlgorithmBlockBuilder If(NestedBuilder<Condition> evalBuilder);
    @Override AlgorithmBlockBuilder elseIf(NestedBuilder<Condition> evalBuilder);
    @Override AlgorithmBlockBuilder Else();
    @Override AlgorithmBlockBuilder repeat();
    @Override AlgorithmBlockBuilder until(NestedBuilder<Condition> evalBuilder);
    @Override AlgorithmBlockBuilder While(NestedBuilder<Condition> evalBuilder);    
    @Override AlgorithmBlockBuilder end();
    @Override AlgorithmBlockBuilder setNextStep(NestedBuilder<Step> next);
}
