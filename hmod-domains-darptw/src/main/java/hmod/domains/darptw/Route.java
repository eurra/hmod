
package hmod.domains.darptw;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
public class Route
{
    private ProblemInstance instance;
    private MutableEvent first;
    private MutableEvent last;
    private HashMap<Integer, MutableEvent> eventsTable;
    private BitSet clientsMask;

    Route(ProblemInstance instance)
    {
        if(instance == null)
            throw new NullPointerException("The problem instance provided cannot be null");
        
        this.instance = instance;
        this.clientsMask = new BitSet(instance.getClientsCount());
        this.eventsTable = new HashMap<>();
        
        this.first = new DepotEvent(this, this.instance.getDepotRequest(), true);
        this.last = new DepotEvent(this, this.instance.getDepotRequest(), false);
        
        this.first.setNextEvent(last);
        this.first.setCurrentLoad(0);
        
        this.last.setPrevEvent(first);
        this.last.setCurrentLoad(0);
        
        this.eventsTable.put(first.getPosId(), first);
        this.eventsTable.put(last.getPosId(), last);
    }
    
    private void updateAcumLoadRange(MutableEvent start, MutableEvent end)
    {
        int acumLoad = start.getPrevEvent().getCurrentLoad();
        MutableEvent concreteEnd = end.getNextEvent();
        MutableEvent aux = start;
        
        while(aux != concreteEnd)
        {
            ServiceRequest currReq = aux.getRequest();
            acumLoad += currReq.getLoadChange();
            
            aux.setCurrentLoad(acumLoad);
            aux = aux.getNextEvent();
        }
    }
    
    private boolean validateCapacityInsert(MutableEvent pickupPos, MutableEvent deliveryPos) throws DARPTWException
    {
        int maxLoad = instance.getVehicleCapacity();
        
        if((deliveryPos != null && pickupPos.getCurrentLoad() >= maxLoad) || pickupPos.getPrevEvent().getCurrentLoad() >= maxLoad)
            return false;
        
        if(deliveryPos != null)
        {
            MutableEvent aux = pickupPos;
            
            while(aux != deliveryPos)
            {
                if(aux.getCurrentLoad() >= maxLoad)
                    return false;
                
                aux = aux.getNextEvent();
            }
        }
        
        return true;
    }
    
    private boolean validateCapacityMove(MutableEvent event, MutableEvent destPos, boolean forward)
    { 
        EventType type = event.getType();
        
        // For pickup events, any forwards movement is harmless
        if(type == EventType.Pickup && forward)
            return true;        
        // For delivery events, any backwards movement is harmless
        else if(type == EventType.Delivery && !forward)
            return true;
        
        // We will check the positions
        MutableEvent aux = (forward ? event.getNextEvent() : event.getPrevEvent());
        int maxLoad = instance.getVehicleCapacity();
        
        while(aux != destPos)
        {
            if(aux.getCurrentLoad() >= maxLoad || (!forward && aux.getPrevEvent().getCurrentLoad() >= maxLoad))
                return false;
            
            if(forward)
                aux = aux.getNextEvent();
            else
                aux = aux.getPrevEvent();
        }
        
        return true;
    }
    
    public Event getFirstEvent()
    {
        return first;
    }

    public Event getLastEvent()
    {
        return last;
    }
    
    public Event getEvent(int posId)
    {
        return eventsTable.get(posId);
    }
    
    public Event getPickupEvent(int clientId)
    {
        ServiceRequest req = instance.getPickupRequest(clientId);
        
        if(req != null)
            return eventsTable.get(req.getId());
        
        return null;
    }

    public Event getDeliveryEvent(int clientId)
    {
        ServiceRequest req = instance.getDeliveryRequest(clientId);
        
        if(req != null)
            return eventsTable.get(req.getId());
        
        return null;
    }

    public Event[] getLoadPeaks()
    {
        ArrayList<MutableEvent> peaks = new ArrayList<>(eventsTable.size());
        MutableEvent aux = first.getNextEvent();
        int maxLoad = instance.getVehicleCapacity();
        
        while(aux != last)
        {
            if(aux.getCurrentLoad() >= maxLoad)
                peaks.add(aux);
            
            aux = aux.getNextEvent();
        }
                
        MutableEvent[] res = new MutableEvent[peaks.size()];
        return peaks.toArray(res);
    }
    
    public int getEventsCount()
    {
        return eventsTable.size();
    }
    
    public boolean isEmpty()
    {
        return first == last.getPrevEvent();
    }

    public int clientsCount()
    {
        return clientsMask.cardinality();
    }
    
    public boolean containsClient(int clientId)
    {        
        return clientsMask.get(clientId - 1);
    }

    public BitSet getClientMask()
    {
        return (BitSet)clientsMask.clone();
    }
    
    public void insertClient(int clientId) throws DARPTWException
    {
        insertClient(clientId, Event.ROUTE_END_ID);
    }
    
    public void insertClient(int clientId, int posId) throws DARPTWException
    {
        insertClient(clientId, posId, posId);
    }

    public void insertClient(int clientId, int pickupPosId, int deliveryPosId) throws DARPTWException
    {
        ServiceRequest pickupReq = instance.getPickupRequest(clientId);
        
        if(pickupReq == null)
            throw new DARPTWException("The provided client id is not valid for the current problem instance");

        if(containsClient(clientId))
            throw new DARPTWException("The specified client is already included in the route");
        
        ServiceRequest deliveryReq = instance.getDeliveryRequest(clientId);
        MutableEvent pickupPos = eventsTable.get(pickupPosId);
        
        if(pickupPos == null)
            throw new DARPTWException("The provided pickup position id is not included in the route");
        
        if(pickupPos == first)
            throw new DARPTWException("The pickup event position cannot be the start depot");
        
        MutableEvent deliveryPos = null;
        
        if(pickupPosId != deliveryPosId)
        {
            deliveryPos = eventsTable.get(deliveryPosId);
            
            if(deliveryPos == null)
                throw new DARPTWException("The provided delivery position id is not included in the route");
            
            if(deliveryPos == first)
                throw new DARPTWException("The delivery event position cannot be the start depot");

            MutableEvent aux = pickupPos.getNextEvent();
            
            while(aux != deliveryPos)
            {
                if(aux == null)
                    throw new DARPTWException("The pickup event position does not precedes the delivery one");
                
                aux = aux.getNextEvent();
            }   
        }
        
        if(!validateCapacityInsert(pickupPos, deliveryPos))
            throw new DARPTWException("The provided insertion positions violate the vehicle capacity (pickup: " + pickupPosId + ", delivery: " + (deliveryPos == null ? pickupPosId : deliveryPosId) + ")");
        
        MutableEvent pickupEvent = new ClientEvent(this, pickupReq, true);
        MutableEvent deliveryEvent = new ClientEvent(this, deliveryReq, false); 
        
        MutableEvent pickupPosPrev = pickupPos.getPrevEvent();        
        pickupPosPrev.setNextEvent(pickupEvent);
        pickupEvent.setPrevEvent(pickupPosPrev);
        
        if(deliveryPos == null)
        {
            pickupEvent.setNextEvent(deliveryEvent);
            deliveryEvent.setPrevEvent(pickupEvent);
            
            deliveryEvent.setNextEvent(pickupPos);
            pickupPos.setPrevEvent(deliveryEvent);
        }
        else
        {
            pickupEvent.setNextEvent(pickupPos);
            pickupPos.setPrevEvent(pickupEvent);
            
            MutableEvent deliveryPosPrev = deliveryPos.getPrevEvent();
            deliveryPosPrev.setNextEvent(deliveryEvent);
            deliveryEvent.setPrevEvent(deliveryPosPrev);            
            deliveryEvent.setNextEvent(deliveryPos);
            deliveryPos.setPrevEvent(deliveryEvent);
        }
        
        // We update the acumulated load an add the events to the table
        updateAcumLoadRange(pickupEvent, deliveryEvent);
        
        clientsMask.set(clientId - 1);
        eventsTable.put(pickupReq.getId(), pickupEvent);
        eventsTable.put(deliveryReq.getId(), deliveryEvent);
    }

    public void removeClient(int clientId) throws DARPTWException
    {
        ServiceRequest pickupReq = instance.getPickupRequest(clientId);
        
        if(pickupReq == null)
            throw new DARPTWException("The provided client id is not valid for the current problem instance");
        
        MutableEvent pickupEvent = eventsTable.get(pickupReq.getId());
        
        if(pickupEvent == null)
            throw new DARPTWException("The specified client is not included in the route");
        
        ServiceRequest deliveryReq = instance.getDeliveryRequest(clientId);
        MutableEvent deliveryEvent = eventsTable.get(deliveryReq.getId());
        
        if(pickupEvent.getNextEvent() == deliveryEvent)
        {
            pickupEvent.getPrevEvent().setNextEvent(deliveryEvent.getNextEvent());
            deliveryEvent.getNextEvent().setPrevEvent(pickupEvent.getPrevEvent());
        }
        else
        {
            pickupEvent.getPrevEvent().setNextEvent(pickupEvent.getNextEvent());
            pickupEvent.getNextEvent().setPrevEvent(pickupEvent.getPrevEvent());
            
            deliveryEvent.getPrevEvent().setNextEvent(deliveryEvent.getNextEvent());
            deliveryEvent.getNextEvent().setPrevEvent(deliveryEvent.getPrevEvent());
            
            // In this case, we need to update the acumulated load within the
            // events range
            updateAcumLoadRange(pickupEvent.getNextEvent(), deliveryEvent.getPrevEvent());
        }
        
        clientsMask.clear(clientId - 1);
        
        eventsTable.remove(pickupReq.getId());
        eventsTable.remove(deliveryReq.getId());
    }

    public void moveEvent(int toMovePosId, int destPosId) throws DARPTWException
    {
        MutableEvent toMoveEvent = eventsTable.get(toMovePosId);
        
        if(toMoveEvent == null)
            throw new DARPTWException("The position id to move is not included in the route");
        
        if(toMoveEvent == first || toMoveEvent == last)
            throw new DARPTWException("The event to move cannot be a depot event");
        
        MutableEvent destPosEvent = eventsTable.get(destPosId);        
        
        if(destPosEvent == null)
            throw new DARPTWException("The destination position id is not included in the route");
        
        if(destPosEvent == first)
            throw new DARPTWException("The destination position event cannot be the start depot");
        
        if(toMoveEvent == destPosEvent || toMoveEvent.getNextEvent() == destPosEvent)
            return;
         
        EventType type = toMoveEvent.getType();
        int clientId = toMoveEvent.getRequest().getClientId();
        boolean forward = false;
        MutableEvent aux = toMoveEvent;
        
        if(type == EventType.Pickup)
        {
            ServiceRequest deliveryReq = instance.getDeliveryRequest(clientId);
            MutableEvent deliveryEvent = eventsTable.get(deliveryReq.getId());
            boolean deliveryTrasversed = false;
            
            while(aux != null)
            {
                if(aux == destPosEvent)
                {
                    forward = true;
                    break;
                }
                else if(aux == deliveryEvent)
                {
                    deliveryTrasversed = true;
                }
                
                aux = aux.getNextEvent();
            }
            
            if(deliveryTrasversed && forward)
                throw new DARPTWException("Tried to move a pickup after its delivery");
        }
        else if(type == EventType.Delivery)
        {
            ServiceRequest pickupReq = instance.getPickupRequest(clientId);
            MutableEvent pickupEvent = eventsTable.get(pickupReq.getId());
            boolean pickupTrasversed = false;
            
            while(aux != null)
            {
                if(aux == pickupEvent)
                    pickupTrasversed = true;
                
                if(aux == destPosEvent)
                    break;
                
                aux = aux.getPrevEvent();
            }
            
            if(aux == null)
                forward = true;
            
            if(pickupTrasversed && !forward)
                throw new DARPTWException("Tried to move a delivery before its pickup");
        }
        
        if(!validateCapacityMove(toMoveEvent, destPosEvent, forward))
            throw new DARPTWException("The specified movement violates the vehicle capacity (to move: " + toMovePosId + ", to pos: " + destPosId + ")");
        
        MutableEvent prevEvent = toMoveEvent.getPrevEvent();  
        MutableEvent nextEvent = toMoveEvent.getNextEvent();
        prevEvent.setNextEvent(nextEvent);
        nextEvent.setPrevEvent(prevEvent);
        
        toMoveEvent.setNextEvent(destPosEvent);
        toMoveEvent.setPrevEvent(destPosEvent.getPrevEvent());
        
        destPosEvent.getPrevEvent().setNextEvent(toMoveEvent);
        destPosEvent.setPrevEvent(toMoveEvent);
        
        if(!forward)
            updateAcumLoadRange(toMoveEvent, prevEvent);
        else
            updateAcumLoadRange(nextEvent, toMoveEvent);
    }

    @Override
    public Route clone()
    {
        Route cloned = new Route(instance);
        cloned.eventsTable = new HashMap<>(eventsTable.size());
        cloned.clientsMask = (BitSet)clientsMask.clone();
        
        MutableEvent aux = first, prevAuxCloned = null;
        
        while(aux != null)
        {            
            MutableEvent clonedAux = aux.clone();
            
            if(aux == first)
                cloned.first = clonedAux;
            else if(aux == last)
                cloned.last = clonedAux;
            
            cloned.eventsTable.put(clonedAux.getPosId(), clonedAux);
            
            if(prevAuxCloned != null)
            {
                prevAuxCloned.setNextEvent(clonedAux);
                clonedAux.setPrevEvent(prevAuxCloned);
            }
            
            prevAuxCloned = clonedAux;
            aux = aux.getNextEvent();
        }
        
        return cloned;
    }
    
    public boolean isModifiable()
    {
        return clientsCount() > 1;
    }
    
    @Override
    public String toString()
    {
        MutableEvent aux = first;
        String ret = "";
        int maxCap = instance.getVehicleCapacity();
        
        while(aux != null)
        {
            ret += aux + (aux.getCurrentLoad() >= maxCap ? "!" : "");
            aux = aux.getNextEvent();
            
            if(aux != null)
                ret += " ";
        }
        
        return ret;
    }
}