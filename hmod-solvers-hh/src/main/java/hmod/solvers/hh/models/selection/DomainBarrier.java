
package hmod.solvers.hh.models.selection;

import hmod.core.Routine;
import hmod.core.ValidableRoutine;
import optefx.loader.Processable;

/**
 *
 * @author Enrique Urra C.
 */
public final class DomainBarrier
{    
    private final ValidableRoutine callHeuristic = new ValidableRoutine("DomainBarrier.callHeuristic", true);

    DomainBarrier(HeuristicRunnerHandler hrh)
    {
        callHeuristic.append(hrh::runHeuristic);
    }

    public Routine callHeuristic() { return callHeuristic; }
    
    @Processable
    private void validate()
    {
        callHeuristic.validate();
    }
}
