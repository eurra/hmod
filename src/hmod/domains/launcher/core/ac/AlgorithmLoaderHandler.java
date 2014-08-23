
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.AlgorithmLoader;
import hmod.domains.launcher.core.AlgorithmLoaderInfo;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmLoaderHandler extends DataHandler
{
    void enableLoader(String id) throws DataHandlingException;
    AlgorithmLoader getCurrentLoader();
    AlgorithmLoaderInfo getInfoFor(String id) throws DataHandlingException;
    String[] getSupportedLoaders();
}
