
package hmod.solvers.hh.models.selection;

import hmod.core.AlgorithmException;
import hmod.core.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import optefx.loader.Processable;

/**
 *
 * @author Enrique Urra C.
 */
public final class MutableHeuristicsHandler implements LLHeuristicsHandler, LLHeuristicsRegister
{
    private final List<Statement> llHeuristics = new ArrayList<>();
    private final Set<Statement> llHeuristicsSet = new HashSet<>();
    private Statement initHeuristic;
    private Statement finishHeuristic;

    private void checkIndex(int index)
    {
        checkIndex(index, false);
    }
    
    private void checkIndex(int index, boolean add)
    {
        if(index < 0 || index >= (add ? llHeuristics.size() + 1 : llHeuristics.size()))
            throw new ArrayIndexOutOfBoundsException(index);
    }

    @Override
    public int getHeuristicsCount()
    {
        return llHeuristics.size();
    }

    @Override
    public Statement getInitializerHeuristic()
    {
        return initHeuristic;
    }

    @Override
    public Statement getFinisherHeuristic()
    {
        return finishHeuristic;
    }

    @Override
    public Statement getHeuristicAt(int index) throws AlgorithmException
    {
        checkIndex(index);
        return llHeuristics.get(index);
    }

    @Override
    public void setInitHeuristic(Statement ih)
    {
        initHeuristic = ih;
    }

    @Override
    public void setFinishHeuristic(Statement fh)
    {
        finishHeuristic = fh;
    }

    @Override
    public void addHeuristic(Statement heuristic)
    {
        addHeuristic(heuristic, llHeuristics.size());
    }

    public void addHeuristic(Statement heuristic, int index)
    {
        checkIndex(index, true);
        
        if(heuristic == null)
            throw new NullPointerException("Null heuristics");
        
        if(llHeuristicsSet.contains(heuristic))
            throw new IllegalArgumentException("The provided heuristic is already added");
        
        llHeuristics.add(index, heuristic);
        llHeuristicsSet.add(heuristic);
    }

    public void setHeuristic(Statement heuristic, int index)
    {
        checkIndex(index);
        
        if(llHeuristicsSet.contains(heuristic))
            throw new IllegalArgumentException("The provided heuristic is already added");
        
        llHeuristics.set(index, heuristic);
        llHeuristicsSet.add(heuristic);
    }

    public void removeHeuristic(Statement heuristic)
    {
        if(!llHeuristicsSet.contains(heuristic))
            throw new IllegalArgumentException("The provided heuristic has not been added");
        
        llHeuristics.remove(heuristic);
        llHeuristicsSet.remove(heuristic);
    }

    public Statement removeHeuristic(int index)
    {
        checkIndex(index);
        Statement heuristic = llHeuristics.get(index);
        
        llHeuristicsSet.remove(heuristic);
        return llHeuristics.remove(index);
    }
    
    @Processable
    public void validate() throws IllegalStateException
    {
        if(initHeuristic == null)
            throw new IllegalStateException("The init heuristic has not been set");
        
        if(finishHeuristic == null)
            throw new IllegalStateException("The finish heuristic has not been set");
        
        if(llHeuristics.isEmpty())
            throw new IllegalStateException("No heuristics have been added");
    }
}
