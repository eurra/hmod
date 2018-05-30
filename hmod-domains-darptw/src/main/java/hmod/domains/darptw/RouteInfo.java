
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public class RouteInfo
{
    private Route route;
    private EventInfo[] eventsInfos;
    private FactorMap evaluation;
    
    public RouteInfo(Route route, EventInfo[] eventsInfos, FactorMap evaluation)
    {        
        if(route == null)
            throw new NullPointerException("The route provided provided cannot be null");
        
        if(eventsInfos == null)
            throw new NullPointerException("The events information provided cannot be null");
        
        if(evaluation == null)
            throw new NullPointerException("The evaluation provided cannot be null");
        
        this.route = route;
        this.eventsInfos = eventsInfos;
        this.evaluation = evaluation;
    }
    
    public int getEventsCount()
    {
        return eventsInfos.length;
    }
    
    public EventInfo getEventInfo(int index)
    {
        if(index < 0 || index >= eventsInfos.length)
            throw new IllegalArgumentException("The provided index is not valid");
        
        return eventsInfos[index];
    }
    
    public Route getRoute()
    {
        return route;
    }
    
    public FactorMap getEvaluation()
    {
        return evaluation;
    }
}
