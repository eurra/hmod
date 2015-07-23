
package hmod.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 *
 * @author Enrique Urra C.
 */
public final class FlowchartFactory
{    
    public static class IfInput
    {
        private final ElseBlock elseBlock;
        private final IfInput firstIf;

        private IfInput(Expression<Boolean> condition, IfInput firstIf)
        {
            this.firstIf = firstIf == null ? this : firstIf;
            this.elseBlock = new ElseBlock(condition, this.firstIf);
        }
        
        public ElseBlock then(Statement... trueSteps)
        {
            elseBlock.innerBlock.setTrueBlock(block(trueSteps));
            return firstIf.elseBlock;
        }
    }
    
    public static class ElseBlock implements Block
    {
        private final IfElseBlock innerBlock;
        private final IfInput firstIf;

        private ElseBlock(Expression<Boolean> condition, IfInput firstIf)
        {
            this.innerBlock = new IfElseBlock(condition);
            this.firstIf = firstIf;
        }
        
        public Block Else(Statement... falseSteps)
        {
            innerBlock.setFalseBlock(block(falseSteps));
            return firstIf.elseBlock;
        }
        
        public IfInput ElseIf(Expression<Boolean> condition)
        {
            IfInput ifBuilder = new IfInput(condition, firstIf);
            innerBlock.setFalseBlock(ifBuilder.elseBlock);
            
            return ifBuilder;
        }
        
        @Override
        public int getChildsCount()
        {
            return innerBlock.getChildsCount();
        }

        @Override
        public Statement[] getChilds()
        {
            return innerBlock.getChilds();
        }

        @Override
        public void run() throws AlgorithmException
        {
            innerBlock.run();
        }
        
        @Override
        public void stop() throws AlgorithmException
        {
            innerBlock.stop();
        }
    }
    
    public static class WhileInput
    {
        private final WhileBlock block;
        
        private WhileInput(Expression<Boolean> condition)
        {
            this.block = new WhileBlock(Objects.requireNonNull(condition, "null condition"));
        }
        
        public Block Do(Statement... steps)
        {
            block.setIterationBlock(block(steps));
            return block;
        }
    }
    
    public static class RepeatInput
    {
        private final Statement iterationBlock;
        
        private RepeatInput(Statement... steps)
        {
            this.iterationBlock = block(steps);
        }
        
        public Block until(Expression<Boolean> cond)
        {
            RepeatUntilBlock result = new RepeatUntilBlock(cond);
            result.setIterationBlock(iterationBlock);
            
            return result;
        }
    }
    
    public static class ForInput
    {
        private final Statement init;
        private final Expression<Boolean> condition;
        private final Statement update;

        ForInput(Block init, Expression<Boolean> condition, Statement update)
        {
            this.init = Objects.requireNonNull(init, "null init");
            this.condition = Objects.requireNonNull(condition, "null condition");
            this.update = Objects.requireNonNull(update, "null update");
        }
        
        public Block Do(Statement... steps)
        {
            return block(
                init,
                While(condition).Do(
                    block(steps),
                    update
                )
            );
        }
    }
    public static Block block(Supplier<Statement> blockSupplier)
    {
        return block(blockSupplier.get());
    }
    
    
    public static Block block(Statement... blocks)
    {
        return new ArrayBlock(blocks);
    }
    
    public static void run(Statement... blocks) throws AlgorithmException
    {
        block(blocks).run();
    }
    
    public static IfInput If(Expression<Boolean> cond)
    {
        return new IfInput(cond, null);
    }
    
    public static WhileInput While(Expression<Boolean> cond)
    {
        return new WhileInput(cond);
    }
    
    public static RepeatInput repeat(Statement... steps)
    {
        return new RepeatInput(steps);
    }
    
    public static ForInput For(Block init, Expression<Boolean> condition, Block update)
    {
        return new ForInput(init, condition, update);
    }
    
    public static Condition AND(Expression<Boolean>... targets)
    {
        return new ANDCondition(targets);
    }
    
    public static Condition OR(Expression<Boolean>... targets)
    {
        return new ORCondition(targets);
    }
    
    public static Condition NOT(Expression<Boolean> target)
    {
        return new NOTCondition(target);
    }
    
    public static UnaryOperator<Statement> append(Statement... toAppend)
    {
        List<Statement> blocks = new ArrayList<>(toAppend.length);
        
        for(int i = 0; i < toAppend.length; i++)
            blocks.add(toAppend[i]);
        
        return (input) -> {
            blocks.add(0, input);
            return new ArrayBlock(blocks.toArray(new Statement[0]));
        };
    }
    
    public static UnaryOperator<Statement> prepend(Statement... toPrepend)
    {
        List<Statement> blocks = new ArrayList<>(toPrepend.length);
        
        for(int i = 0; i < toPrepend.length; i++)
            blocks.add(toPrepend[i]);
        
        return (input) -> {
            blocks.add(input);
            return new ArrayBlock(blocks.toArray(new Statement[0]));
        };
    }
    
    public static final Statement[] expandBlocks(Statement[] toExpandList)
    {
        return expandBlocks(toExpandList, true);
    }
    
    public static final Statement[] expandBlocks(Statement[] toExpandList, boolean requireNonEmpty) throws AlgorithmException
    {
        List<Statement> expandedList = new ArrayList<>(toExpandList.length);
        
        for(Statement block : toExpandList)
            expandBlocks(expandedList, Objects.requireNonNull(block, "null block at position i"));
        
        if(requireNonEmpty && expandedList.isEmpty())
            throw new AlgorithmException("The expanded list is empty");
        
        return expandedList.toArray(new Statement[0]);
    }
    
    private static void expandBlocks(List<Statement> expandedList, Statement currBlock)
    {
        if(currBlock instanceof SequentialBlock)
        {
            SequentialBlock currSequentialBlock = (SequentialBlock)currBlock;
            int blocksCount = currSequentialBlock.getChildsCount();
            
            for(int i = 0; i < blocksCount; i++)
                expandBlocks(expandedList, currSequentialBlock.getChildAt(i));
        }
        else
        {
            expandedList.add(currBlock);
        }
    }

    private FlowchartFactory()
    {
    }
}
