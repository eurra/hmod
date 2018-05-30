
package hmod.solvers.hh.models.basicops;

import hmod.core.OperatorInfo;
import hmod.solvers.common.HeuristicOutputIds;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
class OperatorInfoHandler
{
    private OperatorInfo hsInfo;
    private OperatorInfo maInfo;

    public void setHSInfo(OperatorInfo hsInfo)
    {
        this.hsInfo = hsInfo;
    }

    public void setMAInfo(OperatorInfo maInfo)
    {
        this.maInfo = maInfo;
    }

    public OperatorInfo getHeuristicSelectionInfo()
    {
        return hsInfo;
    }

    public OperatorInfo getMoveAcceptanceInfo()
    {
        return maInfo;
    }
    
    public void printInfo()
    {
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Heuristic selection operator: " + (hsInfo != null ? hsInfo.getName() + " (" + hsInfo.getDescription() + ")" : "(no-info)"));
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Move acceptance operator: " + (maInfo != null ? maInfo.getName() + " (" + maInfo.getDescription() + ")" : "(no-info)"));
    }
}
