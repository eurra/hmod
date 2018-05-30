
package hmod.domains.darptw;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
class ProblemInstanceParser
{
    private final String separator = " +";
    private final double defaultPlanningHorisonLength = 1440.0;
    
    private BufferedReader reader;
    private int lineNumber;
    private int vehicleNumber, vehicleMaxLoad, clientsCount;    
    private double routeDuration, mrt;
    private ServiceRequest depotRequest;
    private ArrayList<ServiceRequest> requests;
    private ArrayList<Location> locations;
    private HashMap<Double, HashMap<Double, Integer>> locationsTable;

    public ProblemInstance parse(String file) throws DARPTWException
    {
        try
        {
            // init variables
            initParse(Objects.requireNonNull(file, "null file"));
            
            // Read the global parameters
            parseGlobals();
            requests = new ArrayList<>((clientsCount * 2) + 1);
            
            // Read the depot info
            parseRequest();

            // Read all the requests
            int reqsCount = clientsCount * 2;

            for(int i = 0; i < reqsCount; i++)
                parseRequest();
            
            finishParse();
        }
        catch(FileNotFoundException ex)
        {
            throw new DARPTWException("wrong data file: " + ex.getLocalizedMessage(), ex);
        }
        catch(EOFException ex)
        {
            throw new DARPTWException("Unexpected end of file", ex);
        }
        catch(IOException ex)
        {
            throw new DARPTWException("[Line " + lineNumber + "] Error reading file", ex);
        }
        catch(IndexOutOfBoundsException ex)
        {
            throw new DARPTWException("[Line " + lineNumber + "] Wrong number of entries", ex);
        }
        catch(NumberFormatException ex)
        {
            throw new DARPTWException("[Line " + lineNumber + "] Wrong number format", ex);
        }
        
        // All ok!
        Location[] locsArray = locations.toArray(new Location[locations.size()]);
        ServiceRequest[] reqsArray = requests.toArray(new ServiceRequest[requests.size()]);
        
        return new ProblemInstance(
            file,
            mrt, 
            defaultPlanningHorisonLength, 
            routeDuration, 
            vehicleNumber, 
            vehicleMaxLoad,
            locsArray, 
            reqsArray,
            depotRequest
        );
    }
    
    private void initParse(String file) throws FileNotFoundException
    {
        reader = new BufferedReader(new FileReader(file));        
        lineNumber = 0;
        vehicleNumber = vehicleMaxLoad = clientsCount = 0;
        routeDuration = mrt = 0.0;
        locations = new ArrayList<>();
        locationsTable = new HashMap<>();
        requests = null;
    }
    
    private void parseGlobals() throws IOException
    {
        String[] parsed = processLine(reader);
        
        vehicleNumber = Integer.parseInt(parsed[0]);
        clientsCount = Integer.parseInt(parsed[1]) / 2;
        routeDuration = Double.parseDouble(parsed[2]);
        vehicleMaxLoad = Integer.parseInt(parsed[3]);
        mrt = Double.parseDouble(parsed[4]);
    }
    
    private void parseRequest() throws IOException
    {
        String[] parsed = processLine(reader);
        
        int reqId = Integer.parseInt(parsed[0]);
        int clientId = 0;
        
        if(reqId > 0 && reqId <= clientsCount)
            clientId = reqId;
        else if(reqId > clientsCount)
            clientId = reqId - clientsCount;
        
        double locx = Double.parseDouble(parsed[1]);
        double locy = Double.parseDouble(parsed[2]);
        int locId = getLocationId(locx, locy);
        
        int stime = Integer.parseInt(parsed[3]);
        int load = Integer.parseInt(parsed[4]);
        int et = Integer.parseInt(parsed[5]);
        int lt = Integer.parseInt(parsed[6]);

        ServiceRequest request = new ServiceRequest(et, lt, locId, clientId, stime, load);
        
        if(reqId > 0)
            requests.add(request);
        else
            depotRequest = request;
    }
    
    private int getLocationId(double locx, double locy)
    {
        Integer locId = null;
        HashMap<Double, Integer> locCheck = locationsTable.get(locx);
        
        if(locCheck != null)
            locId = locCheck.get(locy);
        
        if(locId == null)
        {
            Location loc = new Location(locx, locy);
            locations.add(loc);
            locId = locations.size() - 1;
            
            if(locCheck == null)
            {
                locCheck = new HashMap<>();
                locationsTable.put(locx, locCheck);
            }
            
            locCheck.put(locy, locId);
        }
        
        return locId;
    }
    
    private void finishParse() throws IOException
    {
        reader.close();
    }
    
    private String[] processLine(BufferedReader reader) throws IOException
    {
        lineNumber++;
        String line = reader.readLine();
        
        if(line == null)
            throw new EOFException();
        
        return (line.trim()).split(separator);
    }
}
