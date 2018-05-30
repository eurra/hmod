
package hmod.launcher;

/**
 * 
 * @author Enrique Urra C.
 */
public interface AlgorithmLoader
{
    AlgorithmLoader load(String input, Object... args) throws AlgorithmLoadException;
    void restart();
}