
package hmod.solvers.hh.models.selection;

import hmod.core.Statement;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicRunnerHandler
{
    private Statement selected;

    HeuristicRunnerHandler()
    {
    }
    
    public void setHeuristicToRun(Statement heuristic)
    {
        if(heuristic == null)
            throw new NullPointerException("Null heuristic");
        
        this.selected = heuristic;
    }

    public Statement getHeuristicToRun()
    {
        if(selected == null)
            throw new IllegalStateException("The current heuristic has not been set");
        
        return selected;
    }

    public boolean isHeuristicActive()
    {
        return selected != null;
    }
    
    public void runHeuristic()
    {
        getHeuristicToRun().run();
    }

    public void clear()
    {
        selected = null;
    }
}