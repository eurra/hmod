
package hmod.launcher;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author Enrique Urra C.
 */
public class LauncherConfig
{
    private static LauncherConfig instance;
    
    public static LauncherConfig getInstance()
    {
        if(instance == null)
            instance = new LauncherConfig();
        
        return instance;
    }
    
    private final PropertiesConfiguration propsConfiguration;

    private LauncherConfig()
    {
        try
        {
            File file = new File("hmod.properties");
            this.propsConfiguration = new PropertiesConfiguration(file);
        }
        catch(ConfigurationException ex)
        {
            throw new RuntimeException("Cannot initialize the hmod.properties file", ex);
        }
    }
    
    public String getEntry(String name, String defaultValue)
    {
        String result = getEntry(name);
        
        if(result == null)
            setEntry(name, defaultValue);
        
        return getEntry(name);
    }
    
    public String getEntry(String name)
    {
        return propsConfiguration.getString(name);
    }
    
    public void setEntry(String name, String value)
    {
        propsConfiguration.setProperty(name, value);
        
        try
        {
            propsConfiguration.save();
        }
        catch(ConfigurationException ex)
        {
            throw new RuntimeException("Cannot save into the hmod.properties file", ex);
        }
    }
}
