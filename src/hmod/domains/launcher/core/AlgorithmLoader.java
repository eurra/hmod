
package hmod.domains.launcher.core;

/**
 * 
 * @author Enrique Urra C.
 */
public interface AlgorithmLoader
{
    AlgorithmLoader load(String input, Object... args) throws AlgorithmLoadException;
    void restart();
}