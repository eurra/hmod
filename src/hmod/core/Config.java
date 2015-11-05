
package hmod.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Enrique Urra C.
 */
public class Config
{
    private static Config instance;
    
    public static Config getInstance()
    {
        if(instance == null)
            instance = new Config();
        
        return instance;
    }
    
    private Properties props;

    private Config()
    {
        FileInputStream fis = null;
        
        try
        {
            this.props = new Properties();
            File configFile = new File("hmod.properties");
            
            if(!configFile.exists())
                throw new RuntimeException("The config file 'hmod.properties' does not exists");
            
            fis = new FileInputStream(configFile);
            props.load(fis);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Cannot load the 'hmod.properties' configuration file", ex);
        }
        finally
        {
            if(fis != null)
            {
                try
                {
                    fis.close();
                }
                catch(IOException ex)
                {
                }
            }
        }
    }
    
    public String getEntry(String name)
    {
        return props.getProperty(name);
    }
}
