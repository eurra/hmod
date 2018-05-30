
package hmod.solvers.hh.models.selection;

import hmod.core.Statement;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterationHandler;
import java.io.PrintWriter;
import optefx.util.metadata.MetadataManager;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class SelectionHHeuristicProcesses
{
    private final LLHeuristicsHandler llhh;
    private final HeuristicRunnerHandler hrh;

    SelectionHHeuristicProcesses(LLHeuristicsHandler llhh, 
                                 HeuristicRunnerHandler hrh)
    {
        this.llhh = llhh;
        this.hrh = hrh;
    }    
    
    public void selectInitHeuristic()
    {
        Statement initHeuristic = llhh.getInitializerHeuristic();
        hrh.setHeuristicToRun(initHeuristic);
    }
    
    public void selectFinishHeuristic()
    {
        Statement initHeuristic = llhh.getFinisherHeuristic();
        hrh.setHeuristicToRun(initHeuristic);
    }
    
    public Statement selectHeuristicFromIterator(IterationHandler iterationData)
    {
        return () -> {
            int currIndex = iterationData.getCurrentIteration();
            Statement toSelect = llhh.getHeuristicAt(currIndex);
            hrh.setHeuristicToRun(toSelect);
        };
    }
    
    public void printLowLevelHeuristics()
    {
        PrintWriter pw = OutputManager.getCurrent().getOutput(HeuristicOutputIds.EXECUTION_INFO);
        
        if(pw != null)
        {
            StringBuilder sb = new StringBuilder();
            int count = llhh.getHeuristicsCount();
            MetadataManager mdm = MetadataManager.getInstance();
            
            for(int i = 0; i < count; i++)
            {
                LowLevelHeuristicInfo llhInfo = mdm.getDataFor(llhh.getHeuristicAt(i), LowLevelHeuristicInfo.class);
                
                if(llhInfo != null)
                    sb.append("\n- ").append(llhInfo.getName()).append(": ").append(llhInfo.getDescription());
            }
            
            if(sb.length() > 0)
            {
                sb.insert(0, "Low-level heuristics used:");
                pw.println(sb.toString());
            }
        }
    }
}
