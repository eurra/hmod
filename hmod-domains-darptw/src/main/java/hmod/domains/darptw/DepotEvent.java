
package hmod.domains.darptw;
/**
 *
 * @author Enrique Urra C.
 */
class DepotEvent extends MutableEvent
{
    public DepotEvent(Route parentRoute, ServiceRequest request, boolean start)
    {
        super(parentRoute, request, start ? EventType.StartDepot : EventType.StopDepot);
    }
    
    @Override
    public int getPosId()
    {
        return getType() == EventType.StartDepot ? Event.ROUTE_START_ID : Event.ROUTE_END_ID;
    }

    @Override
    public String toString()
    {
        return getType() == EventType.StartDepot ? "<D0>" : "<DN>";
    }
    
    @Override
    public MutableEvent clone()
    {
        MutableEvent cloned = new DepotEvent(getParentRoute(), getRequest(), getType() == EventType.StartDepot);
        cloned.copyData(this);
        
        return cloned;
    }
}
