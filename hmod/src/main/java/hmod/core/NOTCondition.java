
package hmod.core;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class NOTCondition implements Condition
{
    private final Expression<Boolean> condition;

    public NOTCondition(Expression<Boolean> condition)
    {
        this.condition = Objects.requireNonNull(condition, "null condition");
    }

    @Override
    public Boolean get()
    {
        return !condition.get();
    }
}
