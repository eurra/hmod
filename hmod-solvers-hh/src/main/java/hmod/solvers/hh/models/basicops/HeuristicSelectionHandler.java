
package hmod.solvers.hh.models.basicops;

import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
public class HeuristicSelectionHandler
{
    private HHSolution toProcess;
    private HHSolution processed;

    HeuristicSelectionHandler()
    {
    }

    void storeSolutionToProcess(HHSolution solution)
    {
        toProcess = solution;
    }

    HHSolution retrieveSolutionProcessed()
    {
        return processed;
    }

    public HHSolution retrieveSolutionToProcess()
    {
        return toProcess;
    }

    public void storeProcessedSolution(HHSolution solution)
    {
        processed = solution;
    }
}
