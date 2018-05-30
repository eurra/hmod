
package hmod.domains.mkp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPLPOptimalSet
{
    private final Map<String, Double> values = new HashMap<>();

    public MKPLPOptimalSet(String file) throws IOException
    {
        double lineNumber = 0;
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String line;
            int instances = Integer.parseInt(reader.readLine());
            
            for(int i = 0; i < instances; i++)
            {
                lineNumber++;
                line = reader.readLine();
                String[] splitted = line.trim().split(" +");
                values.put(splitted[0], Double.parseDouble(splitted[1]));
            }
        }
        catch(FileNotFoundException ex)
        {
            throw new IOException("Wrong data file", ex);
        }
        catch(IOException ex)
        {
            throw new IOException("[Line " + lineNumber + "] Error reading file", ex);
        }
        catch(IndexOutOfBoundsException ex)
        {
            throw new IOException("[Line " + lineNumber + "] Wrong number of entries", ex);
        }
        catch(NumberFormatException ex)
        {
            throw new IOException("[Line " + lineNumber + "] Wrong number format", ex);
        }
    }
    
    public double getLPOptimumFor(int resourcesCount, int itemsCount, int instanceNum) throws IllegalArgumentException
    {
        return getLPOptimumFor(resourcesCount + "." + itemsCount + "-" + (instanceNum < 10 ? "0" + instanceNum : instanceNum));
    }
    
    public double getLPOptimumFor(String key) throws IllegalArgumentException
    {
        if(!values.containsKey(key))
            throw new IllegalArgumentException("The provided instance key (" + key + ") does not exists");
        
        return values.get(key);
    }
    
    public double getGapFor(int resourcesCount, int itemsCount, int instanceNum, double solutionValue) throws IllegalArgumentException
    {
        double lpOpt = getLPOptimumFor(resourcesCount, itemsCount, instanceNum);
        return (lpOpt - solutionValue) / lpOpt;
    }
    
    public double getGapFor(String key, double solutionValue) throws IllegalArgumentException
    {
        double lpOpt = getLPOptimumFor(key);
        return (lpOpt - solutionValue) / lpOpt;
    }
}
