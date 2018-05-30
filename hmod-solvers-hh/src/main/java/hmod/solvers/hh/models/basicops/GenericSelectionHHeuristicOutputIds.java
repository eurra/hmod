
package hmod.solvers.hh.models.basicops;

/**
 *
 * @author Enrique Urra C.
 */
public final class GenericSelectionHHeuristicOutputIds
{
    private GenericSelectionHHeuristicOutputIds(){}
    
    /**
     * Output that provides a tabular description of the hyperheuristic 
     * execution results, such as the best evaluation and the total cpu time.
     * Can be used through appending to construct tabular reports regarding
     * multiple executions.
     */
    public static final String RESULT_SHEET = "hhGeneric-resultSheet";
    
    public static final String WARNINGS = "hmod.solvers.hh.models.basicops.warnings";
}
