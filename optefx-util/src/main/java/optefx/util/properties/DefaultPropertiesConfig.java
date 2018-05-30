
package optefx.util.properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultPropertiesConfig implements MultiPropertiesConfig
{
    private final String[] files;
    private final Properties config;

    public DefaultPropertiesConfig(String... propertiesFiles)
    {
        if(propertiesFiles == null)
            throw new NullPointerException("Null properties files");
        
        if(propertiesFiles.length == 0)
            throw new NullPointerException("Zero-length properties files");
        
        config = new Properties();
        files = new String[propertiesFiles.length];
        
        for (int i = 0; i < propertiesFiles.length; i++)
        {
            Properties currProps = new Properties();
            String propFile = propertiesFiles[i];
            
            if(propFile == null)
                throw new NullPointerException("Null properties file at position " + i);
            
            if(propFile.isEmpty())
                throw new IllegalArgumentException("Empty properties file at position " + i);
            
            try
            {
                currProps.load(new FileReader(propFile));
            }
            catch (FileNotFoundException ex)
            {
                throw new IllegalArgumentException("The properties file '" + propFile + "' does not exists", ex);
            }
            catch (IOException ex)
            {
                throw new IllegalArgumentException("Cannot read the properties file '" + propFile + "'", ex);
            }
            
            config.putAll(currProps);
            files[i] = propFile;
        }
    }

    @Override
    public String checkEntry(String entry)
    {
        try
        {
            return getEntry(entry);
        }
        catch(IllegalArgumentException ex)
        {
            return null;
        }
    }
    
    @Override
    public String getEntry(String entry) throws IllegalArgumentException
    {
        String value = config.getProperty(entry);
        
        if(value == null)
        {
            String allFiles = "";
            
            for(String file : files)
                allFiles += file + ";";
            
            throw new IllegalArgumentException("The entry '" + entry + "' was not found in properties files (" + allFiles + ")");
        }
        
        return value;
    }
    
    @Override
    public final boolean entryExists(String entry)
    {
        return config.getProperty(entry) != null;
    }
    
}
