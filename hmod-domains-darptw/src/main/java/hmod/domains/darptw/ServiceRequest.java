
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public class ServiceRequest
{
    private double et;
    private double lt;
    private final double serviceTime;
    private final int clientId;
    private final int locationId;    
    private int loadChange;

    public int getId()
    {
        if(loadChange > 0)
            return clientId;
        else
            return -clientId;
    }
    
    public double getET()
    {
        return et;
    }
    
    public double getLT()
    {
        return lt;
    }
    
    public double getServiceTime()
    {
        return serviceTime;
    }
    
    public int getClientId()
    {
        return clientId;
    }
    
    public int getLocationId()
    {
        return locationId;
    }
    
    public int getLoadChange()
    {
        return loadChange;
    }
    
    public ServiceRequest(double et, double lt, int locationId, int clientId, double serviceTime, int loadChange)
    {
        this.et = et;
        this.lt = lt;
        this.serviceTime = serviceTime;
        this.clientId = clientId;
        this.locationId = locationId;
        this.loadChange = loadChange;
    }
      
    public boolean isValidTimeWindow()
    {
        return lt - et < 1440;
    }

    public void updateTimesFromDelivery(ServiceRequest delivery, ProblemInstance instance) throws DARPTWException
    {       
        et = Math.max(0.0, delivery.getET() - instance.getMaximumRideTime() - serviceTime);
        lt = Math.min(delivery.getLT() - instance.getDRT(delivery.getLocationId(), locationId) - serviceTime, instance.getPlanningHorizonLength());
    }
    
    public final void updateTimesFromPickup(ServiceRequest pickup, ProblemInstance instance) throws DARPTWException
    {
        et = Math.max(0, pickup.getET() + pickup.getServiceTime() + instance.getDRT(pickup.getLocationId(), locationId));
        lt = Math.min(pickup.getLT() + pickup.getServiceTime() + instance.getMaximumRideTime(), instance.getPlanningHorizonLength());
    }
}
