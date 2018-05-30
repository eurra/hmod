
package hmod.core;

import java.util.function.UnaryOperator;

/**
 *
 * @author Enrique Urra C.
 */
public interface Routine extends Statement
{
    Routine append(Statement block);
    Routine appendAfter(Statement block);
    Routine appendBefore(Statement block);
    Routine prepend(Statement block);
    Routine prependAfter(Statement block);
    Routine prependBefore(Statement block);
    Routine apply(UnaryOperator<Statement> handler);
}