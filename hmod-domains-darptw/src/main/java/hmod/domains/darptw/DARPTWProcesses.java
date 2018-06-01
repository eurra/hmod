
package hmod.domains.darptw;

import hmod.core.AlgorithmException;
import hmod.core.Condition;
import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.MutableIterationHandler;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Supplier;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class DARPTWProcesses
{
    public static final Statement insertClient(InsertionPositionHandler iph,
                                               RouteHandler rh,
                                               ClientHandler ch)
    {
        return () -> {
            Route route = rh.getCurrentRoute();
            Client client = ch.getCurrentClient();
            int pickupPosId = iph.getSelectedPosition().getPickupPosition();
            int deliveryPosId = iph.getSelectedPosition().getDeliveryPosition();

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "******************\nInserting client '" + client.getId()
                    + "' at the positions '" + pickupPosId + "','" + deliveryPosId + "' "
                    + "of the following route:\n" + route);

            try
            {
                route.insertClient(client.getId(), pickupPosId, deliveryPosId);
            }
            catch(DARPTWException ex)
            {
                throw new AlgorithmException("Cannot insert a client into the route", ex);
            }

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "Result:\n" + route + "\n");
        };
    }
    
    public static Statement removeClient(RouteHandler route, ClientHandler client)
    {
        return () -> {
            Route targetRoute = route.getCurrentRoute();
            Client targetClient = client.getCurrentClient();

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "******************\nRemoving client '" + targetClient.getId()
                + "' from the following route:\n" + targetRoute);

            try
            {
                targetRoute.removeClient(targetClient.getId());
            }
            catch(DARPTWException ex)
            {
                throw new AlgorithmException("Cannot remove a client: " + ex.getLocalizedMessage(), ex);
            }

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "Result:\n" + targetRoute + "\n");
        };
    }
    
    public static Statement moveClient(RouteHandler orig, RouteHandler dest, ClientHandler client, InsertionPositionHandler posDest)
    {
        return block(
            removeClient(orig, client),
            insertClient(posDest, dest, client)
        );
    }
    
    public static Statement pickModifiableRoutes(RouteSet source, RouteSet target)
    {
        return () -> {
            for(Route route : source)
            {
                if(route.isModifiable())
                    target.addRoute(route);
            }
        };
    }
    
    public static Statement selectRandomRoute(RouteSet sourceSet, RouteHandler rh)
    {
        return () -> {
            if(sourceSet.getRoutesCount() == 0)
                throw new DARPTWException("Empty route set");
            
            int routesCount = sourceSet.getRoutesCount();
            Route selRoute = sourceSet.getRouteAt(RandomTool.getInt(routesCount));
            rh.selectRoute(selRoute);
        };
    }
    
    public static Statement selectOtherRoute(RouteSet sourceSet, RouteHandler toFilter, RouteHandler target)
    {
        return () -> {
            int filterIndex = sourceSet.indexOf(toFilter.getCurrentRoute());
            int targetIndex = RandomTool.getInt(sourceSet.getRoutesCount() - 1);
            
            if(targetIndex >= filterIndex)
                targetIndex++;
            
            target.selectRoute(sourceSet.getRouteAt(targetIndex));
        };
    }
    
    public static Statement selectRandomClientFromRoute(ProblemInstance pi, RouteHandler route, ClientHandler target)
    {
        return () -> {
            Route targetRoute = route.getCurrentRoute();
        
            BitSet clientMask = targetRoute.getClientMask();
            int clientsCount = clientMask.length();

            int randomPos = RandomTool.getInt(clientsCount);
            int selClient = clientMask.nextSetBit(randomPos);

            if(selClient == -1)
                selClient = clientMask.previousSetBit(randomPos);

            Client client = pi.getClient(selClient + 1);
            target.selectCurrentClient(client);
        };
    }
    
    public static Statement selectRouteFromIterator(IterationHandler ih, RouteSet routes, RouteHandler target)
    {
        return () -> {
            Route route = routes.getRouteAt(ih.getCurrentIteration());
            target.selectRoute(route);  
        };
    }
    
    public static final Statement selectRandomInsertionPointInRoute(RouteHandler rh, InsertionPositionHandler iph)
    {
        return () -> {
            Route route = rh.getCurrentRoute();
            Event[] loadPeaks = route.getLoadPeaks();        
            Event selectFirst, selectLast;

            if(loadPeaks.length == 0)
            {
                selectFirst = route.getFirstEvent().getNextEvent();
                selectLast = route.getLastEvent();
            }
            else if(loadPeaks.length == 1)
            {
                if(RandomTool.getBoolean())
                {
                    selectFirst = route.getFirstEvent().getNextEvent();
                    selectLast = loadPeaks[0];
                }
                else
                {
                    selectFirst = loadPeaks[0].getNextEvent().getNextEvent();
                    selectLast = route.getLastEvent();
                }
            }
            else
            {
                int peakIndex = RandomTool.getInt(loadPeaks.length);

                if(peakIndex == 0)
                {
                    selectFirst = route.getFirstEvent().getNextEvent();
                    selectLast = loadPeaks[peakIndex];
                }
                else if(peakIndex == loadPeaks.length - 1)
                {
                    selectFirst = loadPeaks[peakIndex].getNextEvent().getNextEvent();
                    selectLast = route.getLastEvent();
                }
                else
                {
                    selectFirst = loadPeaks[peakIndex].getNextEvent().getNextEvent();
                    selectLast = loadPeaks[peakIndex + 1];
                }          
            }

            ArrayList<Event> selectableEvents = new ArrayList<>(route.getEventsCount());
            Event aux = selectFirst;

            while(aux != selectLast)
            {
                selectableEvents.add(aux);
                aux = aux.getNextEvent();
            }

            selectableEvents.add(selectLast);

            int randPickupIndex = RandomTool.getInt(selectableEvents.size());
            int randDeliveryIndex = randPickupIndex + RandomTool.getInt(selectableEvents.size() - randPickupIndex);

            Event pickupEvent = selectableEvents.get(randPickupIndex);
            Event deliveryEvent = selectableEvents.get(randDeliveryIndex);

            iph.selectPosition(new InsertPosition(pickupEvent.getPosId(), deliveryEvent.getPosId()));
        };
    }
    
    public static Statement storeRandomRoutes(RouteSet source, RouteSet target, Supplier<Integer> countSupplier)
    {
        return () -> {
            int max = countSupplier.get();
            
            for(int i = 0; i < max; i++)
                target.addRoute(source.getRouteAt(RandomTool.getInt(source.getRoutesCount())));
        };
    }
    
    public static Statement storeRandomRouteSequence(RouteSet source, RouteSet target)
    {
        return () -> {
            int routesCount = source.getRoutesCount();
            List<Route> baseRoutes = new ArrayList<>(routesCount);

            for(int i = 0; i < routesCount; i++)
            {
                Route toAdd = source.getRouteAt(i);
                baseRoutes.add(toAdd);
            }

            Route[] randomRoutes = RandomTool.fastArrayShuffle(baseRoutes.toArray(new Route[0]));

            for(int i = 0; i < routesCount; i++)
                target.addRoute(randomRoutes[i]);
        };
    }
    
    public static Statement initIterationFromRoutes(MutableIterationHandler ih, RouteSet routes)
    {
        return () -> {
            int routesCount = routes.getRoutesCount();
            ih.resetIterations();
            ih.setMaxIterations(routesCount);
        };
    }
    
    public static Statement pickMovableEvents(ProblemInstance pi, RouteHandler route, EventPositionList eventPos)
    {
        return () -> {
            eventPos.clear();
            Route targetRoute = route.getCurrentRoute();
            Event toCheck = targetRoute.getFirstEvent();
            
            while(toCheck != null)
            {
                EventType toCheckType = toCheck.getType();
                boolean shouldAdd = true;
        
                if(toCheckType == EventType.StartDepot || toCheckType == EventType.StopDepot)
                {
                    shouldAdd = false;
                }
                else
                {
                    Event prevEvent = toCheck.getPrevEvent();
                    Event nextEvent = toCheck.getNextEvent();
                    int clientId = toCheck.getRequest().getClientId();        
                    int maxLoad = pi.getVehicleCapacity();

                    if(toCheckType == EventType.Pickup)
                    {
                        int nextClientId = nextEvent.getRequest().getClientId();
                        int prevLoad = prevEvent.getCurrentLoad();
                        int prevPrevLoad = (prevEvent.getPrevEvent() != null ? prevEvent.getPrevEvent().getCurrentLoad() : 0);
                        EventType prevType = prevEvent.getType();

                        if(clientId == nextClientId && (prevType == EventType.StartDepot || prevLoad >= maxLoad || prevPrevLoad >= maxLoad))
                            shouldAdd = false;
                    }
                    else
                    {
                        int prevClientId = prevEvent.getRequest().getClientId();
                        int nextLoad = nextEvent.getCurrentLoad();
                        EventType nextType = nextEvent.getType();

                        if(clientId == prevClientId && (nextType == EventType.StopDepot || nextLoad >= maxLoad))
                            shouldAdd = false;
                    }
                }
                
                if(shouldAdd)
                    eventPos.addPosition(new EventPosition(toCheck.getPosId()));
                
                toCheck = toCheck.getNextEvent();
            }
            
            if(eventPos.count() == 0)
                throw new DARPTWException("No movable events have been found on route:\n" + targetRoute);
        };
    }
    
    public static Statement pickRandomEventFromList(EventPositionList list, EventPositionHandler toSelect)
    {
        return () -> toSelect.selectEventPosition(list.getPositionAt(RandomTool.getInt(list.count())));
    }
    
    public static Statement selectRandomMovePoint(ProblemInstance pi, 
                                                  RouteHandler route,
                                                  EventPositionHandler origEvent, 
                                                  EventPositionHandler destEvent)
    {
        return () -> {
            Route targetRoute = route.getCurrentRoute();
            Event toMove = targetRoute.getEvent(origEvent.getSelectedPosition().getPosition());
            int maxLoad = pi.getVehicleCapacity();
            ArrayList<Event> selectableEvents = new ArrayList<>(targetRoute.getEventsCount());

            if(toMove.getType() == EventType.Pickup)
            {
                Event delivery = targetRoute.getDeliveryEvent(toMove.getRequest().getClientId());
                Event aux = toMove.getNextEvent().getNextEvent();

                while(aux != delivery.getNextEvent())
                {
                    selectableEvents.add(aux);
                    aux = aux.getNextEvent();
                }

                aux = toMove;
                Event first = targetRoute.getFirstEvent();

                while(aux != first && aux.getPrevEvent().getCurrentLoad() < maxLoad)
                {
                    if(aux != toMove)
                        selectableEvents.add(aux);

                    aux = aux.getPrevEvent();
                }
            }
            else
            {
                Event pickup = targetRoute.getPickupEvent(toMove.getRequest().getClientId());
                Event aux = toMove.getPrevEvent();

                while(aux != pickup)
                {
                    selectableEvents.add(aux);
                    aux = aux.getPrevEvent();
                }

                aux = toMove.getNextEvent();

                while(aux != null && aux.getPrevEvent().getCurrentLoad() < maxLoad)
                {
                    selectableEvents.add(aux);
                    aux = aux.getNextEvent();
                }
            }

            int randomPos = RandomTool.getInt(selectableEvents.size());
            EventPosition destPos = new EventPosition(selectableEvents.get(randomPos).getPosId());
            destEvent.selectEventPosition(destPos);
        };
    }
    
    public static Statement moveEvent(RouteHandler route, EventPositionHandler origEvent, EventPositionHandler destEvent)
    {
        return () -> {
            Route targetRoute = route.getCurrentRoute();
            int posOrig = origEvent.getSelectedPosition().getPosition();
            int posDest = destEvent.getSelectedPosition().getPosition();

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "******************\nMoving event '" + posOrig 
                + "' at the position '" + posDest + "' in the following route:\n" + targetRoute);

            try
            {
                targetRoute.moveEvent(posOrig, posDest);
            }
            catch(DARPTWException ex)
            {
                throw new AlgorithmException("Cannot move a event in the route", ex);
            }

            OutputManager.println(DARPTWOutputIds.OPERATION_INFO, "Result:\n" + targetRoute + "\n");
        };
    }
    
    public static Statement selectClientFromIterator(IterationHandler iterator, List<Client> iteratedList, ClientHandler target)
    {
        return () -> {
            int curriteration = iterator.getCurrentIteration();
            Client client = iteratedList.get(curriteration);        
            target.selectCurrentClient(client);
        };
    }
    
    public static Statement fillAvailableClients(SolutionBuilder sb, ProblemInstance pi, List<Client> targetList)
    {
        return () -> {
            BitSet availables = sb.getAvailableClients();
            int clientId = availables.nextClearBit(0);

            while(clientId < pi.getClientsCount())
            {
                targetList.add(pi.getClient(clientId + 1));
                clientId = availables.nextClearBit(clientId + 1);
            }
        };
    }
    
    public static Statement initIterationForClients(MutableIterationHandler ih, List<Client> clients)
    {
        return () -> {
            int clientsCount = clients.size();
            
            if(clientsCount == 0)
                throw new DARPTWException("No clients available for iteration");
            
            ih.resetIterations();
            ih.setMaxIterations(clientsCount);
        };
    }
    
    public static Condition thereAreRoutes(RouteSet rs, int minRoutes) {
        return () -> {
            if(rs.getRoutesCount() >= minRoutes)
                return true;
            
            return false;
        };
    }
    
    public static Statement reportResult(SolutionHandler sh)
    {
        return () -> {
            DARPTWSolution best = sh.getBestSolution();
            FactorMap eval = best.getEvaluation();

            OutputManager.println(DARPTWOutputIds.RESULT_DETAIL, "Best solution found:\n" + best);        
            OutputManager.println(DARPTWOutputIds.RESULT_SHEET,
                eval.getFactorValue(Factor.TRANSIT_TIME) + "\t" +
                eval.getFactorValue(Factor.ROUTE_DURATION) + "\t" +
                eval.getFactorValue(Factor.SLACK_TIME) + "\t" +
                eval.getFactorValue(Factor.RIDE_TIME) + "\t" +
                eval.getFactorValue(Factor.EXCESS_RIDE_TIME) + "\t" +
                eval.getFactorValue(Factor.WAIT_TIME) + "\t" +
                eval.getFactorValue(Factor.TIME_WINDOWS_VIOLATION) + "\t" +
                eval.getFactorValue(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION) + "\t" +
                eval.getFactorValue(Factor.MAXIMUM_RIDE_TIME_VIOLATION)
            );
        };
    }

    private DARPTWProcesses()
    {
    }
}
