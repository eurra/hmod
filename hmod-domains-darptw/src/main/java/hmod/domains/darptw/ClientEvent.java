
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
class ClientEvent extends MutableEvent
{
    public ClientEvent(Route parentRoute, ServiceRequest request, boolean pickup)
    {
        super(parentRoute, request, pickup ? EventType.Pickup : EventType.Delivery);
    }
    
    @Override
    public int getPosId()
    {
        return getRequest().getId();
    }

    @Override
    public String toString()
    {
        return "(" + getRequest().getId() + ")";
    }

    @Override
    public ClientEvent clone()
    {
        ClientEvent cloned = new ClientEvent(getParentRoute(), getRequest(), getType() == EventType.Pickup);
        cloned.copyData(this);
        
        return cloned;
    }
}
