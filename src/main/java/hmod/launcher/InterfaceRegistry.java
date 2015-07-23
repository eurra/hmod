
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface InterfaceRegistry
{
    InterfaceRegistry addFactories(AlgorithmInterfaceFactory... factories);
    InterfaceRegistry addFactory(AlgorithmInterfaceFactory factory);
    boolean interfaceExists(String id);
}
