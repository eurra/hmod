
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.core.Evaluator;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataHandlerAssembler<T extends DataHandler> extends NestedBuilder<T>
{
    DataHandlerAssembler<T> addOperatorDelegate(String methodName, NestedBuilder<? extends Operator> delegate) throws DataHandlingException;
    DataHandlerAssembler<T> addEvaluatorDelegate(String methodName, NestedBuilder<? extends Evaluator<Boolean>> delegate) throws DataHandlingException;
}
