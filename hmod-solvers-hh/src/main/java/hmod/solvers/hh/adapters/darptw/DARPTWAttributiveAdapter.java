
package hmod.solvers.hh.adapters.darptw;

import hmod.domains.darptw.DARPTWDomain;
import hmod.domains.darptw.DARPTWSolution;
import hmod.solvers.hh.models.adapter.AdapterComponents;
import hmod.solvers.hh.models.attr.AttributesProcessor;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class DARPTWAttributiveAdapter
{    
    @LoadsComponent(AdapterComponents.class)
    public static void loadComponents(DARPTWDomain darptw,
                                      AdapterComponents<DARPTWHHSolution, DARPTWSolution> ac)
    {
        AttributesProcessor<DARPTWSolution, Object> clientsProcessors = DARPTWAttributeProcessors::clientProcessors;
        AttributesProcessor<DARPTWSolution, Object> eventsProcessors = DARPTWAttributeProcessors::eventProcessors;
        
        ac.setEncoder((solution) -> 
            new DARPTWAttributiveSolution<>(
                solution, 
                clientsProcessors,
                eventsProcessors
            )
        );
    }
}
