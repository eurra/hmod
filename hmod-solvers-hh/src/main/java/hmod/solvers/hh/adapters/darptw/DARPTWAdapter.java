
package hmod.solvers.hh.adapters.darptw;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.domains.darptw.DARPTWDomain;
import hmod.domains.darptw.DARPTWSolution;
import hmod.solvers.hh.models.adapter.AdapterComponents;
import hmod.solvers.hh.models.selection.LLHeuristicsRegister;
import hmod.solvers.hh.models.selection.LowLevelHeuristicInfo;
import static hmod.solvers.hh.models.selection.LowLevelHeuristicInfo.tagLowLevelHeuristic;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class DARPTWAdapter
{
    private static Statement getHeuristic(DARPTWDomain darptw, Statement baseSt)
    {
        return block(
            darptw.loadSolution(),
            baseSt,
            darptw.saveSolution()
        );
    }
    
    @LoadsComponent(LLHeuristicsRegister.class)
    public static void loadHeuristics(DARPTWDomain darptw,
                                      LLHeuristicsRegister llhr)
    {
        llhr.setInitHeuristic(block(darptw.loadNewSolution(), darptw.saveSolution()));
        llhr.setFinishHeuristic(darptw.reportSolution());
        
        llhr.addHeuristic(tagLowLevelHeuristic(getHeuristic(darptw, darptw.heuristic(DARPTWDomain.MOVE_RANDOM_CLIENT)),
            new LowLevelHeuristicInfo.Builder().
                id("move-random-client").
                name("Move Random Client").
                description("From a randomly selected route, moves a client to another different route").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(getHeuristic(darptw, darptw.heuristic(DARPTWDomain.MOVE_SINGLE_EVENT)),
            new LowLevelHeuristicInfo.Builder().
                id("move-single-event").
                name("Move Single Event").
                description("From a randomly selected route, moves an event within the route").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(getHeuristic(darptw, darptw.heuristic(DARPTWDomain.MOVE_CLIENT_ALL_ROUTES)),
            new LowLevelHeuristicInfo.Builder().
                id("move-client-all-routes").
                name("Move Client All Routes").
                description("For each route in a solution, moves a randomly selected client to a different route").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(getHeuristic(darptw, darptw.heuristic(DARPTWDomain.MOVE_EVENT_ALL_ROUTES)),
            new LowLevelHeuristicInfo.Builder().
                id("move-event-all-routes").
                name("Move Event All Routes").
                description("For each route in a solution, moves a randomly selected event within the route").
                build()
        ));
    }
    
    @LoadsComponent(AdapterComponents.class)
    public static void loadComponents(DARPTWDomain darptw,
                                      AdapterComponents<DARPTWHHSolution, DARPTWSolution> ac)
    {
        ac.setDecoder((solution) -> solution.getInnerSolution());
        ac.setEncoder((solution) -> new DARPTWHHSolution(solution));
        ac.setDownloader((solution) -> darptw.setInputSolution().accept(solution));
        ac.setUploader(() -> darptw.getOutputSolution().get());
    }
}
