
package hmod.solvers.hh.adapters.binary;

import hmod.domains.binary.BinaryDomain;
import hmod.domains.binary.BinarySolution;
import hmod.domains.binary.BinarySolutionHandler;
import hmod.solvers.hh.models.adapter.AdapterComponents;
import hmod.solvers.hh.models.selection.LLHeuristicsRegister;
import hmod.solvers.hh.models.selection.LowLevelHeuristicInfo;
import static hmod.solvers.hh.models.selection.LowLevelHeuristicInfo.tagLowLevelHeuristic;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class BinaryDomainAdapter
{    
    @LoadsComponent({ LLHeuristicsRegister.class})
    public static void loadHeuristics(BinaryDomain bd,
                                      LLHeuristicsRegister llhr)
    { 
        llhr.setInitHeuristic(bd.initSolution());
        llhr.setFinishHeuristic(bd.printBestSolution());
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            bd.heuristic(BinaryDomain.FLIP_ALL),
            new LowLevelHeuristicInfo.Builder().
                id("flip-all").
                name("Flip all").
                description("Flip all bits in solution").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            bd.heuristic(BinaryDomain.RANDOM_MUTATE),
            new LowLevelHeuristicInfo.Builder().
                id("random-mutate").
                name("Random mutate").
                description("Flip a random bit within the solution").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            bd.heuristic(BinaryDomain.RANDOMIZE),
            new LowLevelHeuristicInfo.Builder().
                id("randomize").
                name("Randomize").
                description("Flip different bits within a solution at random").
                build()
        ));
    }
    
    @LoadsComponent({ AdapterComponents.class })
    public static void loadComponents(BinarySolutionHandler bsh,
                                      AdapterComponents<HHBinarySolution, BinarySolution> ac)
    {       
        ac.setDecoder((solution) -> solution.getInnerSolution());
        ac.setEncoder((solution) -> new HHBinarySolution(solution));
        ac.setDownloader((solution) -> bsh.setSolution(solution));
        ac.setUploader(() -> bsh.getSolution());
    }
}
