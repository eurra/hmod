
package hmod.solvers.hh.models.attr;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.common.RouletteSelector;
import hmod.solvers.hh.models.basicops.MoveAcceptance;
import hmod.solvers.hh.models.basicops.MoveAcceptanceProcesses;
import hmod.solvers.hh.models.oscillation.OscillationHandler;
import java.util.function.Function;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Resolvable;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class AttributiveAcceptance
{
    private static <T extends HHAttributiveSolution> Function<SolutionPool<T>, T> randomEliteSelector()
    {
        return (set) -> {
            int count = set.getCurrentCount();
            return set.getSolutionAt(RandomTool.getInt(count));
        };
    }
    
    private static <T extends HHAttributiveSolution> Function<SolutionPool<T>, T> rouletteEliteSelector(double amplificator)
    {
        return (set) -> {
            int count = set.getCurrentCount();
            RouletteSelector<T> selector = new RouletteSelector<>(count, (sol) -> sol.getEvaluation());
            selector.setAmplificator(amplificator);
            
            for(int i = 0; i < count; i++)
                selector.addElement(set.getSolutionAt(i));
                       
            return selector.select();
        };
    }
    
    public static final Parameter<Integer> MAX_SOLUTIONS = new Parameter<>("AttributiveAcceptance.MAX_SOLUTIONS");
    public static final Parameter<Double> QUALITY_TOLERANCE = new Parameter<>("AttributiveAcceptance.QUALITY_TOLERANCE");
    public static final Parameter<ReplaceCriteria> REPLACE_CRITERIA = new Parameter<>("AttributiveAcceptance.REPLACE_CRITERIA");
    public static final Parameter<Double> ROULETTE_CRITERIA_AMPLIFICATOR = new Parameter<>("AttributiveAcceptance.ROULETTE_CRITERIA_AMPLIFICATOR");
    
    public static final MoveAcceptance ATTRIBUTIVE_ACCEPTANCE = Resolvable.boundTo(
        MoveAcceptance.class, 
        AttributiveAcceptance.class, 
        (bes) -> bes.acceptanceBlock
    );
    
    @LoadsComponent({ AttributivePool.class, AttributiveAcceptance.class })
    public static void loadGlobalReplacing(ComponentRegister cr,
                                           ParameterRegister pr,
                                           MoveAcceptanceProcesses map,
                                           IterationHandler ih,
                                           OscillationHandler oh,
                                           IterativeHeuristic iHeu)
    {
        int maxElites = pr.getRequiredValue(MAX_SOLUTIONS);
        AttributivePool esh = cr.provide(new AttributivePool<>(maxElites));        
        double qualityTolerance = pr.getRequiredValue(QUALITY_TOLERANCE);        
        ReplaceCriteria criteria = pr.getRequiredValue(REPLACE_CRITERIA);
        Function<SolutionPool<HHAttributiveSolution>, HHAttributiveSolution> selector;
        
        switch(criteria)
        {
            case ROULETTE: {
                Double ampl = pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR);
                if(ampl == null) ampl = 1.0;
                selector = rouletteEliteSelector(ampl);
                
                break;
            }
            case RANDOM: 
            default: selector = randomEliteSelector();
        }
        
        iHeu.initReporting().append(block(
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Attributive acceptance parameters:"),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Max. elite solutions: " + maxElites),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Quality tolerance: " + qualityTolerance),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Replacement criteria: " + criteria)        
        ));
        
        if(criteria == ReplaceCriteria.ROULETTE && pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR) != null)
        {
            iHeu.initReporting().append(
                () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Amplification for " + criteria + " criteria: " + pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR))
            );
        }
        
        cr.provide(new AttributiveAcceptance(esh, ih, oh, map, qualityTolerance, selector));
    }
    
    private final AttributivePool<HHAttributiveSolution> esh;
    private final IterationHandler ih;
    private final OscillationHandler oh;
    private final Function<SolutionPool<HHAttributiveSolution>, HHAttributiveSolution> selector;
    private final Statement acceptanceBlock;
    private final double qualityTolerance;
    private int lastEliteChange = 0;
    private AttributesSnapshot lastSnapshot;
    private double lastTolerance;

    private AttributiveAcceptance(AttributivePool<HHAttributiveSolution> esh,
                                  IterationHandler ih,
                                  OscillationHandler oh,
                                  MoveAcceptanceProcesses map,
                                  double qualityTolerance,
                                  Function<SolutionPool<HHAttributiveSolution>, HHAttributiveSolution> selector)
    {
        this.esh = esh;
        this.ih = ih;
        this.oh = oh;
        this.selector = selector;
        this.lastSnapshot = esh.getAttributesSnapshot();
        this.qualityTolerance = qualityTolerance;
        
        acceptanceBlock = map.getMoveAcceptanceBlock(
            this::evaluateAcceptance, 
            "Attributive acceptance", 
            "Acceptance based on a quality solution list from which attributes are extracted."
        );
    }
    
    private void registerEliteChange()
    {
        lastEliteChange = ih.getCurrentIteration();
        lastSnapshot = esh.getAttributesSnapshot();
        lastTolerance = (1 - oh.getCurrentOscillationRate()) * qualityTolerance;
    }
    
    private HHAttributiveSolution evaluateAcceptance(HHAttributiveSolution current, HHAttributiveSolution candidate)
    {
        boolean shouldReplaceElite = true;
        
        if(esh.isFull() && candidate.compareTo(current) <= 0)
        {
            double eliteAvgEvaluation = esh.getAverageEvaluationWithin(lastTolerance);
            
            if(candidate.getEvaluation() >= eliteAvgEvaluation)
            {
                double attrExistenceScore = lastSnapshot.getAttributeExistenceScore(candidate);
                double attrAbsenceScore = 1 - attrExistenceScore;
                
                if(attrAbsenceScore < RandomTool.getDouble())
                    shouldReplaceElite = false;
            }
            else
            {
                shouldReplaceElite = false;
            }
        }
        
        if(shouldReplaceElite)
            esh.tryReplaceWithQuality(candidate);
        
        if(candidate.compareTo(current) > 0)
        {
            registerEliteChange();
            return candidate;
        }
        else if(((double)lastEliteChange / ih.getCurrentIteration()) < RandomTool.getDouble())
        {
            HHAttributiveSolution accepted = selector.apply(esh);
            registerEliteChange();
            OutputManager.println(AttributiveAcceptanceOutputIds.EVENTS, "Current solution restarted to: " + accepted.evaluationToString());
            
            return accepted;
        }
        else
        {
            return current;
        }
    }
}
