
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class Client
{
    private final int id;
    private final ServiceRequest upRequest;
    private final ServiceRequest downRequest;

    public Client(int id, ServiceRequest upRequest, ServiceRequest downRequest)
    {
        if(upRequest == null)
            throw new NullPointerException("null pickup request");
        
        if(downRequest == null)
            throw new NullPointerException("null delivery request");
        
        this.id = id;
        this.upRequest = upRequest;
        this.downRequest = downRequest;
    }

    public int getId()
    {
        return id;
    }
    
    public ServiceRequest getPickupRequest()
    {
        return upRequest;
    }

    public ServiceRequest getDeliveryRequest()
    {
        return downRequest;
    }
}
