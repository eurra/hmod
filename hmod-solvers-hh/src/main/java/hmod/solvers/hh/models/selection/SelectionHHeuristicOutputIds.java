
package hmod.solvers.hh.models.selection;

/**
 *
 * @author Enrique Urra C.
 */
public final class SelectionHHeuristicOutputIds
{
    private SelectionHHeuristicOutputIds(){}
    
    /**
     * Output that informs when a new best solution was found.
     */
    public static final String NEW_BEST_FITNESS = "hmod.solvers.hh.models.selection.newBestFitness";
    
    /**
     * Same as above but in sheet mode
     */
    public static final String NEW_BEST_FITNESS_SHEET = "hhAdapterCore-bestFitness-sheet";
}
