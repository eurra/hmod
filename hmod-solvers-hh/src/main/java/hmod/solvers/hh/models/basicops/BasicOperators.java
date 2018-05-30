
package hmod.solvers.hh.models.basicops;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.MutableIterationHandler;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.selection.SelectionHHeuristicProcesses;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.SelectableValue;
import optefx.loader.Selector;

/**
 *
 * @author Enrique Urra C.
 */
public final class BasicOperators
{   
    public static final class BasicHS extends SelectableValue<Statement> implements HeuristicSelection
    {
        private BasicHS()
        {
            super(BasicOperators.class, (bo) -> bo.hsMethods);
        }
    }
    
    public static final class BasicMA extends SelectableValue<Statement> implements MoveAcceptance
    {
        private BasicMA()
        {
            super(BasicOperators.class, (bo) -> bo.maMethods);
        }
    }
    
    public static final BasicHS SIMPLE_RANDOM = new BasicHS();
    public static final BasicHS SIMPLE_RANDOM_DESCENT = new BasicHS();
    public static final BasicHS RANDOM_PERMUTATION = new BasicHS();
    public static final BasicHS RANDOM_PERMUTATION_DESCENT = new BasicHS();
    public static final BasicHS GREEDY = new BasicHS();
    
    public static final BasicMA ACCEPT_ALL_MOVES = new BasicMA();
    public static final BasicMA IMPROVING_OR_EQUALS = new BasicMA();
    public static final BasicMA ONLY_IMPROVING = new BasicMA();
    
    @LoadsComponent({
            HeuristicSelectionHandler.class, 
            MoveAcceptanceHandler.class, 
            HeuristicSelectionProcesses.class,
            MoveAcceptanceProcesses.class
    })
    public static void loadProcesses(ComponentRegister cr,
                                     TrackableSolutionHandler sh,
                                     LLHeuristicsHandler llhh,
                                     HeuristicRunnerHandler hrh)
    {
        HeuristicSelectionHandler hsh = cr.provide(new HeuristicSelectionHandler());
        MoveAcceptanceHandler mah = cr.provide(new MoveAcceptanceHandler());
        cr.provide(new HeuristicSelectionProcesses(hsh, sh, llhh, hrh));
        cr.provide(new MoveAcceptanceProcesses(mah));
    }
    
    @LoadsComponent(BasicOperators.class)
    public static void loadOperators(ComponentRegister cr, 
                                     DomainBarrier db,
                                     TrackableSolutionHandler sh,
                                     HeuristicRunnerHandler hrh,
                                     LLHeuristicsHandler llhh,
                                     SelectionHHeuristicProcesses shhp,
                                     HeuristicSelectionHandler hsh,
                                     MoveAcceptanceHandler mah,
                                     HeuristicSelectionProcesses hsp,
                                     MoveAcceptanceProcesses map)
    {
        cr.provide(new BasicOperators(db, sh, hrh, llhh, shhp, hsh, mah, hsp, map));
    }
    
    private final Statement callRandomHeuristic;
    private final Statement callHeuristicPermutation;
    private final Selector<BasicHS, Statement> hsMethods = new Selector<>();
    private final Selector<BasicMA, Statement> maMethods = new Selector<>();

    public BasicOperators(DomainBarrier db,
                          TrackableSolutionHandler sh,
                          HeuristicRunnerHandler hrh,
                          LLHeuristicsHandler llhh,
                          SelectionHHeuristicProcesses shhp,
                          HeuristicSelectionHandler hsh,
                          MoveAcceptanceHandler mah,
                          HeuristicSelectionProcesses hsp,
                          MoveAcceptanceProcesses map)
    {
        HeuristicPermutationHandler hph = new HeuristicPermutationHandler();
        RandomPermutationProcesses rpp = new RandomPermutationProcesses(llhh, hph, hrh);
        GreedyHandler gh = new GreedyHandler();
        GreedyProcesses gp = new GreedyProcesses(gh, llhh, sh, hsh);
        
        //////////// Heuristic selection
        
        callRandomHeuristic = block(
            If(NOT(hrh::isHeuristicActive)).then(
                hsp::selectRandomHeuristic
            ),
            hsp::setSolutionToProcessAsInput,
            db.callHeuristic()
        );
        
        callHeuristicPermutation = block(() -> {            
            MutableIterationHandler iterationData = new MutableIterationHandler();
            
            return block(
                rpp.initializePermutationIteration(iterationData),
                hsp::setSolutionToProcessAsInput,
                repeat(rpp.nextHeuristicInPermutation(iterationData),
                    db.callHeuristic(),
                    If(sh::outputImprovedInput).then(
                        sh::setInputFromOutput
                    ),
                    iterationData::advanceIteration
                ).until(iterationData::areIterationsFinished)
            );
        });
        
        hsMethods.add(SIMPLE_RANDOM, HeuristicSelectionProcesses.getHeuristicSelectionBlock(
            block(
                hrh::clear,
                callRandomHeuristic,
                hsp::storeOutputAsProcessed
            ),
            "Simple Random",
            "Select a random low-level heuristic and executes it"
        ));
        
        hsMethods.add(SIMPLE_RANDOM_DESCENT, HeuristicSelectionProcesses.getHeuristicSelectionBlock(            
            block(
                If(NOT(hsp::checkOutputImprovedToProcess)).then(
                    hrh::clear
                ),
                callRandomHeuristic,
                hsp::storeOutputAsProcessed
            ),
            "Simple Random Descent",
            "Selects a random low-level heuristic and executes it while " +
                "the solution is not improved"
        ));

        hsMethods.add(RANDOM_PERMUTATION, HeuristicSelectionProcesses.getHeuristicSelectionBlock(
            block(
                rpp::storeRandomPermutation,
                callHeuristicPermutation,
                hsp::storeOutputAsProcessed
            ),
            "Random permutation",
            "Creates a random low-level heuristic permutation and executes it" + 
                " in order"
        ));
        
        hsMethods.add(RANDOM_PERMUTATION_DESCENT, HeuristicSelectionProcesses.getHeuristicSelectionBlock(
            block(
                If(hph::isPermutationDeleted).then(
                    rpp::storeRandomPermutation
                ),
                callHeuristicPermutation,
                If(NOT(hsp::checkOutputImprovedToProcess)).then(
                    hph::deletePermutation
                ),
                hsp::storeOutputAsProcessed
            ),
            "Random permutation Descent",
            "Creates a random low-level heuristic permutation and uses " +
                "it until the solution is not improved"
        ));
        
        hsMethods.add(GREEDY, HeuristicSelectionProcesses.getHeuristicSelectionBlock(block(() -> {
                MutableIterationHandler iterationData = new MutableIterationHandler();

                return block(
                    gp.initGreedyIteration(iterationData),
                    repeat(shhp.selectHeuristicFromIterator(iterationData),
                        hsp::setSolutionToProcessAsInput,
                        db.callHeuristic(),
                        gp::registerSolutionFromOutput,
                        iterationData::advanceIteration
                    ).until(iterationData::areIterationsFinished),
                    gp::storeBestAsProcessed
                );
            }),
            "Greedy",
            "Evaluates the result of all low-level heuristics and " +
                "select the one whose result is the best"
        ));
        
        //////////// Move Acceptance
        
        maMethods.add(ACCEPT_ALL_MOVES, map.getMoveAcceptanceBlock(
            (curr, cand) -> cand,
            "Accept all moves", 
            "All solution changes are accepted"
        ));
        
        maMethods.add(ONLY_IMPROVING, map.getMoveAcceptanceBlock(
            (curr, cand) -> cand.compareTo(curr) > 0 ? cand : curr,
            "Only Improving", 
            "Only solution changes that improve the current solution " +
                "quality are accepted"
        ));
        
        maMethods.add(IMPROVING_OR_EQUALS, map.getMoveAcceptanceBlock(
            (curr, cand) -> cand.compareTo(curr) >= 0 ? cand : curr,
            "Improving or Equals", 
            "Only solution changes that improve or maintain the current " + 
                "solution quality are accepted"
        ));
    }
    
    public Statement hsMethod(BasicHS hs) { return hsMethods.get(hs); }
    public Statement maMethod(BasicMA ma) { return maMethods.get(ma); }
}
