
package hmod.domains.mkp;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Enrique Urra C.
 */
class MKPParser
{
    private static final String separator = " +";
    
    private static final class InnerInstance implements MKPProblemInstance
    {
        private final int number;
        private final ArrayList<Item> itemsList;
        private final HashSet<Item> itemsSet;
        private final ArrayList<Resource> resourcesList;
        private final HashSet<Resource> resourcesSet;
        private final HashMap<Integer, HashMap<Integer, Integer>> weightsMap;

        public InnerInstance(int number, int itemsCount, int resourcesCount)
        {
            this.number = number;
            this.itemsList = new ArrayList<>(itemsCount);
            this.itemsSet = new HashSet<>(itemsCount);
            this.resourcesList = new ArrayList<>(resourcesCount);
            this.resourcesSet = new HashSet<>(resourcesCount);
            this.weightsMap = new HashMap<>(resourcesCount);
        }

        @Override
        public int getNumber()
        {
            return number;
        }
        
        @Override
        public int getItemsCount()
        {
            return itemsList.size();
        }

        @Override
        public int getResourcesCount()
        {
            return resourcesList.size();
        }

        @Override
        public Item getItem(int itemId)
        {
            if(itemId < 0 || itemId >= itemsList.size())
                throw new IllegalArgumentException("Wrong item index: " + itemId);

            return itemsList.get(itemId);
        }

        @Override
        public Resource getResource(int resourceId)
        {
            if(resourceId < 0 || resourceId >= resourcesList.size())
                throw new IllegalArgumentException("Wrong resource index: " + resourceId);

            return resourcesList.get(resourceId);
        }

        @Override
        public boolean itemExists(Item item)
        {
            return itemsSet.contains(item);
        }

        @Override
        public boolean resourceExists(Resource res)
        {
            return resourcesSet.contains(res);
        }

        @Override
        public void checkItem(Item item) throws IllegalArgumentException
        {
            if(!itemExists(item))
                throw new IllegalArgumentException("The item do not belongs to the problem instance");
        }

        @Override
        public void checkResource(Resource res) throws IllegalArgumentException
        {
            if(!resourceExists(res))
                throw new IllegalArgumentException("The resource do not belongs to the problem instance");
        }

        @Override
        public int getWeight(Item item, Resource resource)
        {
            if(!itemExists(item))
                throw new IllegalArgumentException("The item '" + item.getId() + "' do not belongs to the instance.");

            if(!resourceExists(resource))
                throw new IllegalArgumentException("The resource '" + resource.getId() + "' do not belongs to the instance.");

            return weightsMap.get(resource.getId()).get(item.getId());
        }
    }
    
    private int lineNumber;
    
    public MKPProblemInstance[] parse(String file) throws IOException
    {
        lineNumber = 0;
        BufferedReader reader = null;
        
        try
        {
            reader = new BufferedReader(new FileReader(file));
            int instancesCount = Integer.parseInt(processLine(reader)[0]);
            InnerInstance[] instances = new InnerInstance[instancesCount];
            
            for(int i = 0; i < instancesCount; i++)
            {
                String[] headers = processLine(reader);
                int itemsCount = Integer.parseInt(headers[0]);
                int resourcesCount = Integer.parseInt(headers[1]);                
                instances[i] = new InnerInstance(i, itemsCount, resourcesCount);
                
                String[] itemProfits = processNextEntries(itemsCount, reader);

                for(int j = 0; j < itemsCount; j++)
                {
                    Item newItem = new Item(j, Integer.parseInt(itemProfits[j]));

                    instances[i].itemsList.add(newItem);
                    instances[i].itemsSet.add(newItem);
                }

                for(int j = 0; j < resourcesCount; j++)
                {
                    HashMap<Integer, Integer> currMap = new HashMap<>();
                    instances[i].weightsMap.put(j, currMap);
                    String[] itemWeigths = processNextEntries(itemsCount, reader);

                    for(int k = 0; k < itemsCount; k++)
                        currMap.put(k, Integer.parseInt(itemWeigths[k]));
                }

                String[] constraints = processNextEntries(resourcesCount, reader);

                for(int j = 0; j < resourcesCount; j++)
                {
                    Resource newRes = new Resource(j, Integer.parseInt(constraints[j]));

                    instances[i].resourcesList.add(newRes);
                    instances[i].resourcesSet.add(newRes);
                }
            }
            
            return instances;
        }
        catch(FileNotFoundException ex)
        {
            throw new IOException("Wrong data file", ex);
        }
        catch(EOFException ex)
        {
            throw new IOException("Unexpected end of file", ex);
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
        finally
        {
            if(reader != null)
                reader.close();
        }
    }
    
    public double findLPOptimum(String file, int resourcesNum, int itemsNum, int instanceNum) throws IOException
    {
        lineNumber = 0;
        BufferedReader reader = null;
        
        try
        {
            reader = new BufferedReader(new FileReader(file));
            int numInstances = Integer.parseInt(processLine(reader)[0]);
            
            for(int i = 0; i < numInstances; i++)
            {
                String[] splitted = processLine(reader);
                String[] instanceNumSplit = splitted[0].split("-");
                String[] itemsResourcesSplit = instanceNumSplit[0].split("\\.");
                int checkResourcesNum = Integer.parseInt(itemsResourcesSplit[0]);
                int checkItemsNum = Integer.parseInt(itemsResourcesSplit[1]);
                int checkInstanceNum = Integer.parseInt(instanceNumSplit[1]);               
                
                if(checkResourcesNum == resourcesNum && checkItemsNum == itemsNum && checkInstanceNum == instanceNum)
                    return Double.parseDouble(splitted[1]);
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
        finally
        {
            if(reader != null)
                reader.close();
        }
        
        return -1.0;
    }
    
    private String[] processLine(BufferedReader reader) throws IOException
    {
        lineNumber++;
        String line = reader.readLine();
        
        if(line == null)
            throw new EOFException();
        
        return (line.trim()).split(separator);
    }
    
    private String[] processNextEntries(int entriesCount, BufferedReader reader) throws IOException
    {
        String[] entries = new String[entriesCount];
        int counter = 0;
        
        while(counter < entriesCount)
        {
            lineNumber++;
            String line = reader.readLine();
            
            if(line == null)
                throw new EOFException();
            
            String[] currLine = (line.trim()).split(separator);
            
            for(int i = 0; i < currLine.length; i++)
            {
                entries[counter] = currLine[i];
                counter++;
            }
        }
        
        return entries;
    }
}
