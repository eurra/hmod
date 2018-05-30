
package hmod.solvers.hh.adapters.mkp;

import hmod.domains.mkp.MKPSolution;
import hmod.solvers.hh.models.adapter.AdapterComponents;
import hmod.solvers.hh.models.attr.AttributesProcessor;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPDomainAttributiveAdapter
{    
    @LoadsComponent(AdapterComponents.class)
    public static void load(AdapterComponents<MKPHHAttributiveSolution, MKPSolution> ac)
    {        
        AttributesProcessor<MKPSolution, String> itemsProcessor = MKPAttributeProcessors::generalItemInfoProcessor;
        //AttributesProcessor<MKPSolution, String> resourceUsageProcessor = MKPAttributeProcessors::resourceUsageProcessor;
        //AttributesProcessor<MKPSolution, String> itemsNotIncludedProcessor = MKPAttributeProcessors::generalItemInfoProcessor;
        
        ac.setEncoder((solution) -> 
            new MKPHHAttributiveSolution(
                solution, 
                itemsProcessor//,
                //itemsNotIncludedProcessor//,
                //resourceUsageProcessor
            )
        );
    }
}
