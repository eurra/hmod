
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
@InterfaceInfo(
    id = "default",
    description = "Default interface that runs through the launcher console. "
        + "Does not support threading."
)
public class DefaultAlgorithmInterfaceFactory extends AlgorithmInterfaceFactory
{
    @Override
    public AlgorithmInterface createInterface(String algName)
    {
        return new DefaultAlgorithmInterface();
    }
}