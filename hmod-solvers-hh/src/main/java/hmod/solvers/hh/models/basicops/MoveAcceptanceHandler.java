
package hmod.solvers.hh.models.basicops;

import hmod.solvers.hh.HHSolution;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class MoveAcceptanceHandler
{
    private HHSolution currAccepted;
    private HHSolution candidate;

    MoveAcceptanceHandler()
    {
    }

    void storeCandidateSolution(HHSolution candidate)
    {
        this.candidate = candidate;
    }

    public HHSolution retrieveCandidateSolution()
    {
        return candidate;
    }

    public HHSolution retrieveCurrentlyAccepted()
    {
        return currAccepted;
    }

    void acceptSolution(HHSolution solution)
    {
        currAccepted = Objects.requireNonNull(solution, "accepted solution is null");
    }
}
