
package hmod.loader.graph;

import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.AlgorithmException;
import hmod.core.Condition;
import hmod.core.Operator;
import hmod.core.Step;
import static hmod.loader.graph.AlgorithmBuilders.decisionStep;
import static hmod.loader.graph.AlgorithmBuilders.sequentialStep;
import static hmod.loader.graph.AlgorithmBuilders.subProcessStep;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmBlockBuilder extends AbstractBuilder<Step> implements AlgorithmBlockBuilder
{    
    private static class InnerStep implements Step
    {
        private Step step;

        public void setStep(Step step)
        {
            this.step = step;
        }
        
        @Override
        public Step resolveNext() throws AlgorithmException
        {
            if(step == null)
                throw new AlgorithmException("Block step not resolved");
            
            return step.resolveNext();
        }
    }
    
    private static abstract class AlgorithmBlockPosition
    {
        private AlgorithmBlockPosition nextPosition;

        public void setNextPosition(AlgorithmBlockPosition nextPosition)
        {
            this.nextPosition = nextPosition;
        }

        public AlgorithmBlockPosition getNextPosition()
        {
            return nextPosition;
        }
        
        public abstract NestedBuilder<Step> getBuilder();
        public abstract void linkBuilderTo(NestedBuilder<Step> nextBuildable);
    }
    
    private static class ProcessPosition extends AlgorithmBlockPosition implements MultiOperatorInput
    {
        private final SequentialStepBuilder builder;
        
        public ProcessPosition()
        {
            builder = sequentialStep();
        }
        
        @Override
        public SequentialStepBuilder getBuilder()
        {
            return builder;
        }
        
        @Override
        public MultiOperatorInput addOperator(NestedBuilder<? extends Operator> operator)
        {
            builder.addOperator(operator);
            return this;
        }

        @Override
        public void linkBuilderTo(NestedBuilder<Step> nextBuildable)
        {
            builder.setNextStep(nextBuildable);
        }
    }
    
    private static class SubProcessPosition extends AlgorithmBlockPosition implements SubProcessStepInput
    {
        private final SubProcessStepBuilder builder;
        
        public SubProcessPosition()
        {
            builder = subProcessStep();
        }
        
        @Override
        public SubProcessStepBuilder getBuilder()
        {
            return builder;
        }

        @Override
        public SubProcessStepInput setSubStep(NestedBuilder<Step> step)
        {
            builder.setSubStep(step);
            return this;
        }

        @Override
        public void linkBuilderTo(NestedBuilder<Step> nextBuildable)
        {
            builder.setNextStep(nextBuildable);
        }
    }
    
    private static class DecisionPosition extends AlgorithmBlockPosition implements SingleConditionInput
    {
        private final ConditionStepBuilder deciderBuilder;
        private final DefaultAlgorithmBlockBuilder trueBranch;
        private final DefaultAlgorithmBlockBuilder falseBranch;

        public DecisionPosition(DefaultAlgorithmBlockBuilder parent)
        {
            deciderBuilder = decisionStep();
            trueBranch = new DefaultAlgorithmBlockBuilder(parent);
            falseBranch = new DefaultAlgorithmBlockBuilder(parent);
            
            deciderBuilder.setTrueStep(trueBranch).setFalseStep(falseBranch);
        }

        @Override
        public ConditionStepBuilder getBuilder()
        {
            return deciderBuilder;
        }

        public DefaultAlgorithmBlockBuilder getTrueBranch()
        {
            return trueBranch;
        }

        public DefaultAlgorithmBlockBuilder getFalseBranch()
        {
            return falseBranch;
        }

        @Override
        public SingleConditionInput setCondition(NestedBuilder<? extends Condition> decider)
        {
            deciderBuilder.setCondition(decider);
            return this;
        }

        @Override
        public void linkBuilderTo(NestedBuilder<Step> nextBuildable)
        {
            if(trueBranch.isEmpty())
                deciderBuilder.setTrueStep(nextBuildable);
            else          
                trueBranch.setNextStep(nextBuildable);
            
            if(falseBranch.isEmpty())
                deciderBuilder.setFalseStep(nextBuildable);
            else
                falseBranch.setNextStep(nextBuildable);
        }
    }
    
    private static abstract class IterationPosition extends AlgorithmBlockPosition
    {
        protected final ConditionStepBuilder conditionBuilder;
        protected final DefaultAlgorithmBlockBuilder iterationBranch;

        public IterationPosition(DefaultAlgorithmBlockBuilder parent)
        {
            this.conditionBuilder = decisionStep();
            this.iterationBranch = new DefaultAlgorithmBlockBuilder(parent);
        }

        public DefaultAlgorithmBlockBuilder getIterationBranch()
        {
            return iterationBranch;
        }
    }
    
    private static class RepeatUntilPosition extends IterationPosition implements SingleConditionInput
    {
        public RepeatUntilPosition(DefaultAlgorithmBlockBuilder parent)
        {
            super(parent);         
            
            iterationBranch.setNextStep(conditionBuilder);
            conditionBuilder.setFalseStep(iterationBranch);
        }

        @Override
        public SingleConditionInput setCondition(NestedBuilder<? extends Condition> decider)
        {
            conditionBuilder.setCondition(decider);
            return this;
        }

        @Override
        public NestedBuilder<Step> getBuilder()
        {
            return iterationBranch;
        }

        @Override
        public void linkBuilderTo(NestedBuilder<Step> nextBuildable)
        {
            conditionBuilder.setTrueStep(nextBuildable);
        }
    }
    
    private static class WhilePosition extends IterationPosition
    {
        public WhilePosition(DefaultAlgorithmBlockBuilder parent, NestedBuilder<? extends Condition> decider)
        {
            super(parent);
            
            conditionBuilder.setCondition(decider).setTrueStep(iterationBranch);
            iterationBranch.setNextStep(conditionBuilder);
        }

        @Override
        public NestedBuilder<Step> getBuilder()
        {
            return conditionBuilder;
        }

        @Override
        public void linkBuilderTo(NestedBuilder<Step> nextBuildable)
        {
            conditionBuilder.setFalseStep(nextBuildable);
        }
    }
    
    private final DefaultAlgorithmBlockBuilder parent;
    private AlgorithmBlockPosition currPosition;
    private AlgorithmBlockPosition firstPosition;
    private NestedBuilder<Step> next;

    public DefaultAlgorithmBlockBuilder()
    {
        this(null);
    }
    
    private boolean isEmpty()
    {
        return firstPosition == null;
    }
    
    private DefaultAlgorithmBlockBuilder(DefaultAlgorithmBlockBuilder parent)
    {
        this.parent = parent;
    }
    
    private <T extends AlgorithmBlockPosition> T getCurrentPositionAs(Class<T> type)
    {        
        if(currPosition != null && type.isAssignableFrom(currPosition.getClass()))
            return type.cast(currPosition);
        
        return null;
    }
    
    private void nextPosition(AlgorithmBlockPosition newPosition)
    {
        if(currPosition != null)
            currPosition.setNextPosition(newPosition);
        else
            firstPosition = newPosition;
        
        currPosition = newPosition;
    }

    @Override
    public DefaultAlgorithmBlockBuilder run(NestedBuilder<Operator> opBuilder)
    {
        ProcessPosition posToAdd = getCurrentPositionAs(ProcessPosition.class);
        
        if(posToAdd == null)
        {
            posToAdd = new ProcessPosition();
            nextPosition(posToAdd);
        }
        
        posToAdd.addOperator(opBuilder);        
        return this;
    }

    @Override
    public DefaultAlgorithmBlockBuilder call(NestedBuilder<Step> subProcess)
    {
        SubProcessPosition pos = new SubProcessPosition();
        nextPosition(pos);
        pos.setSubStep(subProcess);
        
        return this;
    }

    @Override
    public DefaultAlgorithmBlockBuilder If(NestedBuilder<Condition> eval)
    {
        DecisionPosition pos = new DecisionPosition(this);
        nextPosition(pos);
        pos.setCondition(eval);
        
        return pos.getTrueBranch();
    }

    @Override
    public DefaultAlgorithmBlockBuilder Else()
    {
        if(parent == null)
            throw new BuilderInputException("No parent block is currently active to call 'else'");
        
        DecisionPosition pos = parent.getCurrentPositionAs(DecisionPosition.class);
        
        if(pos == null)
            throw new BuilderInputException("No 'if-else' block is currently active");
        
        return pos.getFalseBranch();
    }

    @Override
    public DefaultAlgorithmBlockBuilder elseIf(NestedBuilder<Condition> eval)
    {
        DefaultAlgorithmBlockBuilder parentIfFalseBranch = Else();
        return parentIfFalseBranch.If(eval);
    }

    @Override
    public DefaultAlgorithmBlockBuilder repeat()
    {
        RepeatUntilPosition pos = new RepeatUntilPosition(this);
        nextPosition(pos);
        
        return pos.getIterationBranch();
    }

    @Override
    public DefaultAlgorithmBlockBuilder until(NestedBuilder<Condition> eval)
    {
        if(parent == null)
            throw new BuilderInputException("No parent block is currently active to call 'until'");
        
        RepeatUntilPosition pos = parent.getCurrentPositionAs(RepeatUntilPosition.class);
        
        if(pos == null)
            throw new BuilderInputException("No 'repeat-until' block is currently active");
        
        pos.setCondition(eval);
        return parent;
    }

    @Override
    public DefaultAlgorithmBlockBuilder While(NestedBuilder<Condition> eval)
    {
        WhilePosition pos = new WhilePosition(this, eval);
        nextPosition(pos);
        
        return pos.getIterationBranch();
    }

    @Override
    public DefaultAlgorithmBlockBuilder end()
    {
        if(parent == null)
            throw new BuilderInputException("There is no parent block active to call 'end'");
        
        return parent;
    }

    @Override
    public AlgorithmBlockBuilder setNextStep(NestedBuilder<Step> next)
    {
        this.next = next;
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        InnerStep blockStep = new InnerStep();
        session.registerResult(this, blockStep);
        
        AlgorithmBlockPosition currPos = firstPosition;
        AlgorithmBlockPosition prevPos = null;
        
        while(currPos != null)
        {
            if(prevPos != null)
                prevPos.linkBuilderTo(currPos.getBuilder());
            
            prevPos = currPos;
            currPos = currPos.getNextPosition();
        }
        
        if(prevPos != null)
            prevPos.linkBuilderTo(next);
        
        if(firstPosition == null && next == null)
            throw new BuilderInputException("A single operation or a next step is required");
        
        if(firstPosition != null)
            blockStep.setStep(firstPosition.getBuilder().build(session));
        else
            blockStep.setStep(next.build(session));
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("AlgorithmBlock");
    }
}
