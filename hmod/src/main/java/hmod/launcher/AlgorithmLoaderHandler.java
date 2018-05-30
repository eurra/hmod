
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmLoaderHandler
{
    void enableLoader(String id) throws IllegalArgumentException;
    AlgorithmLoader getCurrentLoader();
    AlgorithmLoaderInfo getInfoFor(String id) throws IllegalArgumentException;
    String[] getSupportedLoaders();
}
