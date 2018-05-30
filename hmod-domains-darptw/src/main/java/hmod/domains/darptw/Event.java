
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public interface Event
{
    static final int ROUTE_START_ID = Integer.MIN_VALUE;
    static final int ROUTE_END_ID = Integer.MAX_VALUE;
    
    int getPosId();
    Event getNextEvent();
    Event getPrevEvent();
    EventType getType();
    ServiceRequest getRequest();    
    int getCurrentLoad();
    Event clone();
}
