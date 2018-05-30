
package hmod.solvers.hh.adapters.darptw;

import hmod.domains.darptw.DARPTWSolution;
import hmod.domains.darptw.Event;
import hmod.domains.darptw.EventType;
import hmod.domains.darptw.Route;
import hmod.solvers.hh.models.attr.AttributeRegister;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public final class DARPTWAttributeProcessors
{
    private static final Map<String, Object> vehicleClientAttrs = new HashMap<>();
    private static final Map<String, Object> sameVehicleAttrs = new HashMap<>();
    private static final Map<String, Object> seqEventsAttrs = new HashMap<>();
    
    public static void clientProcessors(DARPTWSolution solution, AttributeRegister reg)
    {
        int routesCount = solution.getRoutesCount();
        
        for(int i = 0; i < routesCount; i++)
        {
            Route route = solution.getRouteInfo(i + 1).getRoute();
            BitSet clientMask = route.getClientMask();
            
            for(int j = clientMask.nextSetBit(0); j >= 0; j = clientMask.nextSetBit(j + 1))
            {
                String vehicleClientKey = i + "-" + j;
                
                if(!vehicleClientAttrs.containsKey(vehicleClientKey))
                    vehicleClientAttrs.put(vehicleClientKey, new Object());
                    
                reg.addAttribute(vehicleClientAttrs.get(vehicleClientKey));
                
                for(int k = clientMask.nextSetBit(j + 1); k >= 0; k = clientMask.nextSetBit(k + 1))
                {
                    String sameVehicleKey = j + "-" + k;
                    
                    if(!sameVehicleAttrs.containsKey(sameVehicleKey))
                        sameVehicleAttrs.put(sameVehicleKey, new Object());
                    
                    reg.addAttribute(sameVehicleAttrs.get(sameVehicleKey));
                }
            }
        }
    }
    
    public static void eventProcessors(DARPTWSolution solution, AttributeRegister reg)
    {
        int routesCount = solution.getRoutesCount();
        
        for(int i = 0; i < routesCount; i++)
        {
            Route route = solution.getRouteInfo(i + 1).getRoute();
            Event currEv = route.getFirstEvent().getNextEvent().getNextEvent();
            
            if(currEv == null)
                continue;
            
            while(currEv.getType() != EventType.StopDepot)
            {
                Event prevEv = currEv.getPrevEvent();
                String seqEventskey = prevEv.getRequest().getId() + "_" + currEv.getRequest().getId();
                
                if(!seqEventsAttrs.containsKey(seqEventskey))
                    seqEventsAttrs.put(seqEventskey, new Object());
                
                reg.addAttribute(seqEventsAttrs.get(seqEventskey));
                currEv = currEv.getNextEvent();
            }
        }
    }
    
    private DARPTWAttributeProcessors()
    {
    }
}
