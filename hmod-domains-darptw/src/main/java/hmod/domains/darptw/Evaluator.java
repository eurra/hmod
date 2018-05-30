
package hmod.domains.darptw;

import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public class Evaluator
{
    private final ProblemInstance instance;
    private final Map<Factor, Double> evaluation;
    private RouteInfo[] routesInfos;
    private int routeIndex;
    private BitSet clientMask;

    Evaluator(ProblemInstance instance)
    {
        if(instance == null)
            throw new NullPointerException("The provided problem instance cannot be null");
        
        this.instance = instance;
        this.evaluation = new LinkedHashMap<>();
        this.routesInfos = new RouteInfo[instance.getVehiclesNumber()];
        this.clientMask = new BitSet(instance.getClientsCount());
        
        resetEvaluation();
    }
    
    private RouteInfo processRoute(Route route)
    {
        if(route == null)
            throw new NullPointerException("The provided route cannot be null");
        
        double at = 0.0;
        
        double tempTransitTime = 0.0;
        double tempSlackTime = 0.0;        
        double tempRideTime = 0.0;
        double tempExcessRideTime = 0.0;
        double tempWaitTime = 0.0;        
        double tempTimeWindowsViolation = 0.0;
        double tempMaximumRideTimeViolation = 0.0;
        
        // The event information array is initialized
        EventInfo[] eventInfos = new EventInfo[route.getEventsCount()];
        int eventIndex = 0;       
                
        // We set the required data to perform the route checking
        Event prevEvent = route.getFirstEvent();
        ServiceRequest prevEventReq = prevEvent.getRequest();
        Event currEvent = prevEvent.getNextEvent();
        ServiceRequest currEventReq = currEvent.getRequest();
        
        // We add the start depot event
        double firstDrt = instance.getDRT(prevEventReq.getLocationId(), currEventReq.getLocationId());        
        eventInfos[eventIndex++] = new EventInfo(prevEventReq, EventType.StartDepot, currEventReq.getET() - firstDrt, 0.0, 0.0, 0);
        at = currEventReq.getET();
        
        // The following table will store the clients pickup times, to check the
        // regarding travel times
        HashMap<Integer, Double> pickupTimes = new HashMap<>(eventInfos.length);
        
        // The evaluation will finish at the end depot event
        while(currEvent.getType() != EventType.StopDepot)
        {
            // First, we process the direct ride time from the previous event
            // to the current one
            double drt = 0.0;
                    
            if(prevEvent.getType() == EventType.StartDepot)
            {
                drt = firstDrt;
            }
            else
            {
                drt = instance.getDRT(prevEventReq.getLocationId(), currEventReq.getLocationId());
                at += drt;
            }            
            
            // The direct ride time affects directly the transit time
            tempTransitTime += drt;
            
            // At this point, it is possible that we has arrived earlier to 
            // the current request, so we need to add a slack time to reach 
            // exactly at the earlier time of such event's TW. Otherwise, it is 
            // possible that the vehicle has arrived late regarding to the 
            // current request TW. If there is at least one client is onboard 
            // the vehicle, we sum the wait time for penalization
            double eventSlackTime = 0.0;
            
            if(at < currEventReq.getET())
            {
                eventSlackTime = currEventReq.getET() - at;
                tempSlackTime += eventSlackTime;
                at += eventSlackTime;                
                tempWaitTime += eventSlackTime * currEvent.getCurrentLoad();
            } 
            else if(at > currEventReq.getLT())
            {
                tempTimeWindowsViolation += at - currEventReq.getLT();
            }

            // If the event is a delivery, we can evaluate the excess on the 
            // ride time and the maximum ride time violation for the related client
            if(currEvent.getType() == EventType.Delivery)
            {
                int clientId = currEventReq.getClientId();
                
                double clientPickupAt = pickupTimes.get(clientId);
                ServiceRequest pickupReq = instance.getPickupRequest(clientId);
                double directRideTime = instance.getDRT(pickupReq.getLocationId(), currEventReq.getLocationId());
                double realRideTime = at - clientPickupAt;
                tempRideTime += realRideTime;
                tempExcessRideTime += (realRideTime - directRideTime);
                
                if(realRideTime > instance.getMaximumRideTime())
                    tempMaximumRideTimeViolation += realRideTime - instance.getMaximumRideTime();
                
                pickupTimes.remove(clientId);
            }
            
            // At this point, we can add the event info.
            eventInfos[eventIndex++] = new EventInfo(currEventReq, currEvent.getType(), at, eventSlackTime, drt, prevEvent.getCurrentLoad());
            
            // We sum the service time to consider the client board
            at += currEventReq.getServiceTime();
            
            // With the client onboard, if the event is a pickup, we can store 
            // its starting ride time span
            if(currEvent.getType() == EventType.Pickup)
                pickupTimes.put(currEventReq.getClientId(), at);

            // Finally, we move to the next event
            prevEvent = currEvent;
            prevEventReq = prevEvent.getRequest();            
            currEvent = currEvent.getNextEvent();
            currEventReq = currEvent.getRequest();
        }
        
        // We add the event related to the end depot,
        double lastDrt = instance.getDRT(prevEventReq.getLocationId(), currEventReq.getLocationId());
        at += lastDrt;
        tempTransitTime += lastDrt;
        eventInfos[eventIndex] = new EventInfo(currEventReq, currEvent.getType(), at, 0.0, lastDrt, 0);
        
        // We calculate the total routing time, and we check if it exceeds the 
        // maximum one
        double tempRouteDuration = eventInfos[eventInfos.length - 1].getAT() - eventInfos[0].getAT();
        double tempMaximumRouteDurationViolation = 0.0;
        
        if(tempRouteDuration > instance.getMaximumRouteDuration())
            tempMaximumRouteDurationViolation += tempRouteDuration - instance.getMaximumRouteDuration();
        
        // Finished the route, we update the statistics and add the route information
        evaluation.put(Factor.TRANSIT_TIME, evaluation.get(Factor.TRANSIT_TIME) + tempTransitTime);
        evaluation.put(Factor.ROUTE_DURATION, evaluation.get(Factor.ROUTE_DURATION) + tempRouteDuration);
        evaluation.put(Factor.SLACK_TIME, evaluation.get(Factor.SLACK_TIME) + tempSlackTime);
        evaluation.put(Factor.RIDE_TIME, evaluation.get(Factor.RIDE_TIME) + tempRideTime);
        evaluation.put(Factor.EXCESS_RIDE_TIME, evaluation.get(Factor.EXCESS_RIDE_TIME) + tempExcessRideTime);
        evaluation.put(Factor.WAIT_TIME, evaluation.get(Factor.WAIT_TIME) + tempWaitTime);
        evaluation.put(Factor.TIME_WINDOWS_VIOLATION, evaluation.get(Factor.TIME_WINDOWS_VIOLATION) + tempTimeWindowsViolation);
        evaluation.put(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, evaluation.get(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION) + tempMaximumRouteDurationViolation);
        evaluation.put(Factor.MAXIMUM_RIDE_TIME_VIOLATION, evaluation.get(Factor.MAXIMUM_RIDE_TIME_VIOLATION) + tempMaximumRideTimeViolation);
        
        MutableFactorMap routeEvaluation = new MutableFactorMap();
        routeEvaluation.addFactor(Factor.TRANSIT_TIME, FactorValue.create(tempTransitTime));
        routeEvaluation.addFactor(Factor.ROUTE_DURATION, FactorValue.create(tempRouteDuration));
        routeEvaluation.addFactor(Factor.SLACK_TIME, FactorValue.create(tempSlackTime));
        routeEvaluation.addFactor(Factor.RIDE_TIME, FactorValue.create(tempRideTime));
        routeEvaluation.addFactor(Factor.EXCESS_RIDE_TIME, FactorValue.create(tempExcessRideTime));
        routeEvaluation.addFactor(Factor.WAIT_TIME, FactorValue.create(tempWaitTime));
        routeEvaluation.addFactor(Factor.TIME_WINDOWS_VIOLATION, FactorValue.create(tempTimeWindowsViolation));
        routeEvaluation.addFactor(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, FactorValue.create(tempMaximumRouteDurationViolation));
        routeEvaluation.addFactor(Factor.MAXIMUM_RIDE_TIME_VIOLATION, FactorValue.create(tempMaximumRideTimeViolation));
        
        return new RouteInfo(route, eventInfos, routeEvaluation);   
    }

    public void addRoute(Route route) throws DARPTWException
    {
        if(route == null)
            throw new NullPointerException("The provided route cannot be null");
        
        if(routeIndex >= routesInfos.length)
            throw new DARPTWException("Cannot add a route: the maximum vehicle count has been reached");
        
        BitSet routeMask = route.getClientMask();
        
        if(routeMask.intersects(clientMask))
            throw new DARPTWException("The route contains clients that are already added to the evaluator");
        
        // We process the route
        routesInfos[routeIndex++] = processRoute(route);
        
        // We update the client mask
        clientMask.or(routeMask);
    }
    
    private void resetEvaluation()
    {
        evaluation.clear();
        
        evaluation.put(Factor.TRANSIT_TIME, 0.0);
        evaluation.put(Factor.ROUTE_DURATION, 0.0);
        evaluation.put(Factor.SLACK_TIME, 0.0);
        evaluation.put(Factor.RIDE_TIME, 0.0);
        evaluation.put(Factor.EXCESS_RIDE_TIME, 0.0);
        evaluation.put(Factor.WAIT_TIME, 0.0);
        evaluation.put(Factor.TIME_WINDOWS_VIOLATION, 0.0);
        evaluation.put(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, 0.0);
        evaluation.put(Factor.MAXIMUM_RIDE_TIME_VIOLATION, 0.0);
    }
    
    public void reset()
    {
        resetEvaluation();
        clientMask.clear();
        routesInfos = new RouteInfo[instance.getVehiclesNumber()];
        routeIndex = 0;
    }

    public DARPTWSolution getSolution(FactorMap evalWeights, boolean requireAllClients) throws DARPTWException
    {
        if(requireAllClients)
        {
            int clientDiff = instance.getClientsCount() - clientMask.cardinality();
        
            if(requireAllClients && clientDiff > 0)
                throw new DARPTWException("Cannot generate a solution: not all clients has been included in the route (" + clientDiff + " remaining)");
        }
        
        int maxVehicles = instance.getVehiclesNumber();
        
        if(routeIndex < maxVehicles)
        {
            DARPTWFactory factory = DARPTWFactory.getInstance();
            
            for(int i = routeIndex; i < maxVehicles; i++)
            {
                Route voidRoute = factory.createRoute(instance);
                addRoute(voidRoute);
            }
        }
        
        MutableFactorMap solutionEvaluation = new MutableFactorMap();
        
        for(Factor factor : evaluation.keySet())
            solutionEvaluation.addFactor(factor, FactorValue.create(evaluation.get(factor)));
           
        return new DARPTWSolution(solutionEvaluation, routesInfos, clientMask, evalWeights);
    }
}
