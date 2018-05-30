
package hmod.solvers.hh.models.attr;

import hmod.core.Statement;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicSelectionScore
{
    private final Statement heuristic;
    private final double score;

    HeuristicSelectionScore(Statement heuristic, double initScore)
    {
        this.heuristic = heuristic;
        this.score = initScore;
    }

    public double getScore()
    {
        return score;
    }

    public Statement getHeuristic()
    {
        return heuristic;
    }
}
