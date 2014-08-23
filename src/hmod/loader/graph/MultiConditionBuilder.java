
package hmod.loader.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.NestedBuilder;
import hmod.core.Condition;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class MultiConditionBuilder extends AbstractBuilder<Condition>
{
    private ArrayBuilder<Condition> conditions;

    public MultiConditionBuilder(NestedBuilder<? extends Condition>... conditions)
    {
        if(conditions == null)
            throw new BuilderInputException("Null conditions");
        
        this.conditions = array(Condition.class);
        
        for(int i = 0; i < conditions.length; i++)
        {
            if(conditions[i] == null)
                throw new BuilderInputException("Null condition at position " + i);
            
            this.conditions.elem(conditions[i]);
        }
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        Condition[] baseDeciders = conditions.asArray().build(session);
        session.registerResult(this, getMainCondition(baseDeciders, session));
    }
    
    public abstract Condition getMainCondition(Condition[] baseConditions, BuildSession session);
}
