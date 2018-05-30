
package hmod.solvers.hh.adapters.mkp;

import hmod.domains.mkp.MKPDomain;
import hmod.domains.mkp.MKPSolution;
import hmod.domains.mkp.SolutionHandler;
import static hmod.core.FlowchartFactory.block;
import hmod.core.Statement;
import hmod.solvers.hh.models.adapter.AdapterComponents;
import hmod.solvers.hh.models.selection.LLHeuristicsRegister;
import hmod.solvers.hh.models.selection.LowLevelHeuristicInfo;
import static hmod.solvers.hh.models.selection.LowLevelHeuristicInfo.tagLowLevelHeuristic;
import java.util.HashMap;
import java.util.Map;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPDomainAdapter
{    
    private static Statement getHeuristic(MKPDomain mkp, Statement baseSt)
    {
        return block(
            mkp.loadSolution(),
            baseSt,
            mkp.saveSolution()
        );
    }
    
    @LoadsComponent(LLHeuristicsRegister.class)
    public static void loadHeuristics(MKPDomain mkp,
                                      LLHeuristicsRegister llhr)
    { 
        llhr.setInitHeuristic(block(mkp.initSolution(), mkp.saveSolution()));
        llhr.setFinishHeuristic(mkp.reportSolution());
        
        Map<String, Statement> fillMethods = new HashMap<>(2);
        fillMethods.put("fill-random", mkp.fillMethod(MKPDomain.RANDOM_FILL));
        fillMethods.put("fill-greedy", mkp.fillMethod(MKPDomain.GREEDY_FILL));
        
        Map<String, Statement> removeMethods = new HashMap<>(2);
        removeMethods.put("remove-random", mkp.removeMethod(MKPDomain.REMOVE_RANDOM));
        removeMethods.put("remove-greedy", mkp.removeMethod(MKPDomain.REMOVE_GREEDY));
        
        //Double[] removeDegrees = { 1.0, 0.75, 0.25, 0.5, 0.1, 0.0 };
        double[] removeDegrees = { 0.75, 0.25, 0.0 };
        
        for(String fillMethodName : fillMethods.keySet())
        {
            Statement fillMethod = fillMethods.get(fillMethodName);
            
            for(String removeMethodname : removeMethods.keySet())
            {
                Statement removeMethod = removeMethods.get(removeMethodname);
                
                for(Double removeDegree : removeDegrees)
                {
                    String fullName = removeMethodname + "-" + fillMethodName + "-" + removeDegree;

                    llhr.addHeuristic(tagLowLevelHeuristic(
                        getHeuristic(mkp, block(
                            mkp.multiRemove(removeMethod, removeDegree, true),
                            fillMethod
                        )),
                        new LowLevelHeuristicInfo.Builder().
                            id(fullName).
                            name(fullName).
                            build()
                    ));
                }
            }
        }
        
        /*llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, block(
                mkp.multiRemove(removeMethods.get("remove-random"), 0.25, true),
                fillMethods.get("fill-random")
            )),
            new LowLevelHeuristicInfo.Builder().
                id("remove-random-fill-random-variant").
                name("remove-random-fill-random-variant").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, block(
                mkp.multiRemove(removeMethods.get("remove-random"), 0.25, true),
                fillMethods.get("fill-greedy")
            )),
            new LowLevelHeuristicInfo.Builder().
                id("remove-random-fill-greedy-variant").
                name("remove-random-fill-greedy-variant").
                build()
        ));*/
        
        /*llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, block(
                
            )),
            new LowLevelHeuristicInfo.Builder().
                id("add-random").
                name("Add Random").
                description("Adds a random (available) item to the knapsack").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.ADD_GREEDY)),
            new LowLevelHeuristicInfo.Builder().
                id("add-greedy").
                name("Add Greedy").
                description("Adds an (available) item to the knapsack based on its profit").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.REMOVE_RANDOM)),
            new LowLevelHeuristicInfo.Builder().
                id("remove-random").
                name("Remove Random").
                description("Removes a random (added) item from the knapsack").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.REMOVE_GREEDY)),
            new LowLevelHeuristicInfo.Builder().
                id("remove-greedy").
                name("Remove greedy").
                description("Removes an (added) item from the knapsack based on its profit").
                build()
        ));*/
        /*
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.MULTI_REMOVE)),
            new LowLevelHeuristicInfo.Builder().
                id("multi-remove").
                name("Multi Remove").
                description("Applies 'Remove Random' to a random subset of all items").
                build()
        ));*/
        
        /*llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.MULTI_REMOVE_AND_GREEDY_FILL)),
            new LowLevelHeuristicInfo.Builder().
                id("multi-remove-greedy-fill").
                name("Multi Remove and Greedy Fill").
                description("Applies 'Multi Remove' and tries to fill the knapsack with a greedy item selection approach").
                build()
        ));
        
        llhr.addHeuristic(tagLowLevelHeuristic(
            getHeuristic(mkp, mkp.removeMethod(MKPDomain.MULTI_REMOVE_AND_RANDOM_FILL)),
            new LowLevelHeuristicInfo.Builder().
                id("multi-remove-random-fill").
                name("Multi Remove and Random Fill").
                description("Applies 'Multi Remove' and tries to fill the knapsack with a random item selection approach").
                build()
        ));*/
    }
    
    @LoadsComponent(AdapterComponents.class)
    public static void loadComponents(SolutionHandler sh,
                                      AdapterComponents<MKPHHSolution, MKPSolution> ac)
    {
        ac.setDecoder((solution) -> solution.getInnerSolution());
        ac.setEncoder((solution) -> new MKPHHSolution(solution));
        ac.setDownloader((solution) -> sh.provideSolution(solution));
        ac.setUploader(() -> sh.retrieveSolution());
    }
}
