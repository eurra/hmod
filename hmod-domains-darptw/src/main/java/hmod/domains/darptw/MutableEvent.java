
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
abstract class MutableEvent implements Event
{
    private final Route parentRoute;
    private MutableEvent next;
    private MutableEvent prev;
    private final ServiceRequest request;    
    private final EventType type;
    private double positionFactor;
    private int acumLoad;
    
    public MutableEvent(Route parentRoute, ServiceRequest request, EventType type)
    {
        this.parentRoute = parentRoute;
        this.request = request;
        this.type = type;
    }

    public Route getParentRoute()
    {
        return parentRoute;
    }
    
    public void setNextEvent(MutableEvent next)
    {
        this.next = next;
    }
    
    @Override
    public MutableEvent getNextEvent()
    {
        return next;
    }

    public void setPrevEvent(MutableEvent prev)
    {
        this.prev = prev;
    }
    
    @Override
    public MutableEvent getPrevEvent()
    {
        return prev;
    }

    @Override
    public ServiceRequest getRequest()
    {
        return request;
    }
    
    @Override
    public EventType getType()
    {
        return type;
    }
    
    public void setCurrentLoad(int acumLoad)
    {
        this.acumLoad = acumLoad;
    }

    @Override
    public int getCurrentLoad()
    {
        return acumLoad;
    }

    protected void copyData(MutableEvent orig)
    {
        positionFactor = orig.positionFactor;
        acumLoad = orig.acumLoad;
    }
    
    @Override
    public abstract MutableEvent clone();
}
