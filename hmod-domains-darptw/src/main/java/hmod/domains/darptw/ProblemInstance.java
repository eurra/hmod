
package hmod.domains.darptw;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public class ProblemInstance
{
    private String file;
    
    private double mrt;
    private double planningHorizonLength;
    private double routeDuration;
    private int vehicleNumber;
    private int vehicleMaxLoad;
    private int clientsCount;
            
    private Map<Integer, Client> clientCache;
    private Location[] locations;
    private HashMap<Double, HashMap<Double, Integer>> locationIndex;
    private double[][] drtTable;

    private ServiceRequest[] requests;
    private ServiceRequest depotRequest;
    
    ProblemInstance(String file, 
                    double mrt, 
                    double planningHorizonLength, 
                    double routeDuration, 
                    int vehicleNumber, 
                    int vehicleMaxLoad,
                    Location[] locations,
                    ServiceRequest[] requests, 
                    ServiceRequest depotRequest) throws DARPTWException
    {
        if(mrt < 0.0)
            throw new IllegalArgumentException("The MRT cannot be negative");
        
        if(planningHorizonLength < 0.0)
            throw new IllegalArgumentException("The planning horizon length cannot be negative");
        
        if(routeDuration < 0.0)
            throw new IllegalArgumentException("The route duration cannot be negative");
        
        if(vehicleNumber < 0)
            throw new IllegalArgumentException("The vehicles number cannot be negative");
        
        if(vehicleMaxLoad < 0)
            throw new IllegalArgumentException("The vehicle maximum load cannot be negative");
        
        if(locations == null)
            throw new NullPointerException("The provided location set cannot be null");
        
        if(requests == null)
            throw new NullPointerException("The provided service request set cannot be null");
        
        if(depotRequest == null)
            throw new NullPointerException("The provided depot request cannot be null");
        
        this.mrt = mrt;
        this.planningHorizonLength = planningHorizonLength;
        this.routeDuration = routeDuration;
        this.vehicleNumber = vehicleNumber;
        this.vehicleMaxLoad = vehicleMaxLoad;
        
        this.locations = locations;
        this.requests = requests;
        this.depotRequest = depotRequest;
        
        initLocations();
        initRequests();
        
        this.clientCache = new HashMap<>(requests.length / 2);
    }

    public String getFile()
    {
        return file;
    }
    
    private void initLocations() throws DARPTWException
    {
        locationIndex = new HashMap<>(locations.length);
        drtTable = new double[locations.length][locations.length];
        
        for(int i = 0; i < locations.length; i++)
        {
            Location locOrig = locations[i];
            
            if(locOrig == null)
                throw new DARPTWException("null location detected at pos " + i);
            
            double x = locOrig.getX();
            double y = locOrig.getY();
            
            // Update the location index
            HashMap<Double, Integer> check = locationIndex.get(x);
            
            if(check == null)
                check = new HashMap<>();
            
            check.put(y, i);
            
            // Update the DRTs
            for(int j = 0; j < locations.length; j++)
            {
                Location locDest = locations[j];
                
                if(locDest == null)
                    throw new DARPTWException("null location detected at pos " + j);
                
                if(j == i)
                    drtTable[i][j] = 0.0;
                else
                    drtTable[i][j] = drtTable[j][i] = calculateDist(locOrig.getX(), locOrig.getY(), locDest.getX(), locDest.getY());
            }
        }
    }
    
    private double calculateDist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
    }
    
    private void initRequests() throws DARPTWException
    {
        int reqsCount = requests.length;
        clientsCount = reqsCount / 2;
        
        for(int i = 0; i < this.clientsCount; i++)
        {
            ServiceRequest pReq = requests[i];
            ServiceRequest dReq = requests[i + clientsCount];

            if(!pReq.isValidTimeWindow())
                pReq.updateTimesFromDelivery(dReq, this);
            else
                dReq.updateTimesFromPickup(pReq, this);
        }
    }
    
    public double getMaximumRideTime()
    {
        return mrt;
    }
    
    public double getPlanningHorizonLength()
    {
        return planningHorizonLength;
    }

    public double getMaximumRouteDuration()
    {
        return routeDuration;
    }
    
    public int getVehiclesNumber()
    {
        return vehicleNumber;
    }

    public int getVehicleCapacity()
    {
        return vehicleMaxLoad;
    }
    
    public int getLocationCount()
    {
        return locations.length;
    }
    
    public double getDRT(int locOrigId, int locDestId)
    {        
        if(drtTable == null)
            return -1.0;
        
        if(locOrigId < 0 || locOrigId >= drtTable.length)
            throw new IllegalArgumentException("The origin location id is invalid (" + locOrigId + ")");
        
        if(locDestId < 0 || locDestId >= drtTable[locOrigId].length)
            throw new IllegalArgumentException("The destination location id is invalid (" + locDestId + ")");
        
        return drtTable[locOrigId][locDestId];
    }
    
    public int getLocationId(double x, double y)
    {
        HashMap<Double, Integer> checkX = locationIndex.get(x);
        Integer loc = null;
            
        if(checkX != null)
            loc = checkX.get(y);
        
        if(loc == null)
            return -1;
        
        return loc;
    }

    public Location getLocation(int locId)
    {
        if(locId < 0 || locations.length <= locId)
            throw new IllegalArgumentException("The specified location id is invalid (" + locId + ")");
        
        return locations[locId];
    }
    
    public int getClientsCount()
    {
        return this.clientsCount;
    }
    
    public Client getClient(int clientId)
    {
        checkClient(clientId);
        Client client = clientCache.get(clientId);
        
        if(client == null)
        {
            client = new Client(clientId, requests[clientId - 1], requests[clientId - 1 + clientsCount]);
            clientCache.put(clientId, client);
        }
        
        return client;
    }

    public ServiceRequest getPickupRequest(int clientId)
    {
        checkClient(clientId);
        return requests[clientId - 1];
    }

    public ServiceRequest getDeliveryRequest(int clientId)
    {
        checkClient(clientId);
        return requests[clientId - 1 + clientsCount];
    }
    
    public ServiceRequest getDepotRequest()
    {
        return depotRequest;
    }
    
    private void checkClient(int clientId)
    {
        if(clientId <= 0 || clientsCount < clientId)
            throw new IllegalArgumentException("The specified client id is invalid (" + clientId + ")");
    }    
    
    public String getInstanceInfo()
    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
        
        String res = "**** DARPTW Instance data ****\n\n"
                
                + "A) Global parameters info\n\n"
                
                + "\tMRT: " + mrt + "\n"
                + "\tPlanning horizon length: " + planningHorizonLength + "\n"
                + "\tRoute duration: " + routeDuration + "\n"
                + "\tVehicles number: " + vehicleNumber + "\n"
                + "\tVehicle max. load: " + vehicleMaxLoad + "\n\n"
                
                + "B) Locations info\n\n";
        
        if(locations.length == 0)
        {
            res += "\tNo locations have been loaded.\n";
        }
        else
        {            
            for(int i = 0; i < locations.length; i++)
            {
                Location loc = locations[i];
                res += "\tLoc. " + i + ": (" + loc.getX() + ", " + loc.getY() + ")\n";
            }
        }
        
        res += "\nC) Location DRTs matrix\n\n";
        
        if(drtTable.length == 0)
        {
            res += "\tNo locations have been loaded.\n";
        }
        else
        {
            res += "\t";
            
            for(int i = 0; i < drtTable.length; i++)
                res += "(" + i + ")\t";
            
            res += "\n";
            
            for(int i = 0; i < drtTable.length; i++)
            {
                res += "(" + i + ")\t";
                
                for(int j = 0; j < drtTable[i].length; j++)
                    res += df.format(drtTable[i][j]) + "\t";
                
                res += "\n";
            }
        }
        
        res += "\nC) Clients info\n\n";
        
        if(clientsCount == 0)
        {
            res += "\tNo clients and requests have been loaded.\n";
        }
        else
        {
            for(int i = 0; i < clientsCount; i++)
            {
                ServiceRequest pickup = requests[i];
                ServiceRequest delivery = requests[i + clientsCount];
                
                res += "\tClient " + (i + 1) + ": pickup(loc=" + pickup.getLocationId() + ", tw=[" + df.format(pickup.getET()) + "," + df.format(pickup.getLT()) + "]), "
                        + "delivery(loc=" + delivery.getLocationId() + ",tw=[" + df.format(delivery.getET()) + "," + df.format(delivery.getLT()) + "])\n";
            }
        }
        
        return res;
    }
}