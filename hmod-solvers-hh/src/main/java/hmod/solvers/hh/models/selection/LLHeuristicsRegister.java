
package hmod.solvers.hh.models.selection;

import hmod.core.Statement;

/**
 *
 * @author Enrique Urra C.
 */
public interface LLHeuristicsRegister
{
    public void setInitHeuristic(Statement ih);
    public void setFinishHeuristic(Statement fh);
    public void addHeuristic(Statement h);
}
