
package optefx.util.properties;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiPropertiesConfig
{    
    static MultiPropertiesConfig loadFrom(String... propertiesFiles)
    {
        return new DefaultPropertiesConfig(propertiesFiles);
    }
    
    String checkEntry(String entry);
    String getEntry(String entry) throws IllegalArgumentException;
    boolean entryExists(String entry);
}
