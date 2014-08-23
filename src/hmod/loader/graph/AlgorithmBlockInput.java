
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Condition;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmBlockInput extends Delegate
{
    AlgorithmBlockInput run(NestedBuilder<Operator> opBuilder);
    AlgorithmBlockInput call(NestedBuilder<Step> subProcess);
    AlgorithmBlockInput If(NestedBuilder<Condition> evalBuilder);
    AlgorithmBlockInput elseIf(NestedBuilder<Condition> evalBuilder);
    AlgorithmBlockInput Else();
    AlgorithmBlockInput repeat();
    AlgorithmBlockInput until(NestedBuilder<Condition> evalBuilder);
    AlgorithmBlockInput While(NestedBuilder<Condition> evalBuilder);    
    AlgorithmBlockInput end();
}
