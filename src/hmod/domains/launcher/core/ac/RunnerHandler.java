
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.running.AlgorithmRunner;
import hmod.domains.launcher.core.running.RunnerInfo;

/**
 *
 * @author Enrique Urra C.
 */
public interface RunnerHandler extends DataHandler
{
    void setCurrentRunner(String id) throws DataHandlingException;
    RunnerInfo getCurrentRunnerInfo() throws DataHandlingException;
    AlgorithmRunner createNewRunnerFromCurrent() throws DataHandlingException;
    RunnerInfo getRunnerInfoFor(String id) throws DataHandlingException;
    String[] getSupportedRunnersIds();
}
