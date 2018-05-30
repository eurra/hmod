
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
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import hmod.solvers.hh.models.tabulist.TabuList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
public final class FrequencyAcceptance
{   
    public static final Parameter<Integer> QUALITY_POOL_SIZE = new Parameter<>("FrequencyAcceptance.QUALITY_POOL_SIZE");
    public static final Parameter<Double> QUALITY_TOLERANCE = new Parameter<>("FrequencyAcceptance.QUALITY_TOLERANCE");
    public static final Parameter<Double> CONVERGENCE_SPEED_TOLERANCE = new Parameter<>("FrequencyAcceptance.CONVERGENCE_SPEED_TOLERANCE");
    
    public static final MoveAcceptance FREQUENCY_ACCEPTANCE = Resolvable.boundTo(
        MoveAcceptance.class, 
        FrequencyAcceptance.class, 
        (bes) -> bes.acceptanceBlock
    );
    
    @LoadsComponent(FrequencyAcceptance.class)
    public static void loadGlobalReplacing(ComponentRegister cr,
                                           ParameterRegister pr,
                                           MoveAcceptanceProcesses map,
                                           IterationHandler ih,
                                           TabuList<HHAttributiveSolution> tl,
                                           TrackableSolutionHandler tsh,
                                           OscillationHandler oh,
                                           IterativeHeuristic iHeu)
    {        
        int qualityPoolSize = pr.getRequiredValue(QUALITY_POOL_SIZE);
        double qualityTolerance = pr.getRequiredValue(QUALITY_TOLERANCE);
        double convergenceSpeedTolerance = pr.getRequiredValue(CONVERGENCE_SPEED_TOLERANCE);
                
        iHeu.initReporting().append(block(
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Frequency acceptance parameters:"),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Quality pool size: " + qualityPoolSize),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Quality tolerance: " + qualityTolerance),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Convergence speed tolerance: " + convergenceSpeedTolerance)
        ));
        
        cr.provide(new FrequencyAcceptance(ih, tl, tsh, oh, map, qualityPoolSize, qualityTolerance, convergenceSpeedTolerance));
    }
    
    private final IterationHandler ih;
    private final TabuList<HHAttributiveSolution> tl;
    private final TrackableSolutionHandler<HHAttributiveSolution> tsh;
    private final OscillationHandler oh;
    private final Statement acceptanceBlock;
    //private final Map<Object, Integer> frequencyTable = new HashMap<>();
    private final LinkedList<Integer> convergenceList = new LinkedList<>();
    //private boolean isAboveQualityZone = false;
    //private final QualityPool<HHAttributiveSolution> qualityPool;
    private final DiversityPool<HHAttributiveSolution> diversityPool;
    private final int qualityPoolSize;
    private int lastImproveIteration = 0;
    private int lastDiversityJumpIteration = 0;
    private final double qualityTolerance;
    private final double convergenceSpeedTolerance;
    
    private FrequencyAcceptance(IterationHandler ih,
                                TabuList<HHAttributiveSolution> tl,
                                TrackableSolutionHandler<HHAttributiveSolution> tsh,
                                OscillationHandler oh,
                                MoveAcceptanceProcesses map,
                                int qualityPoolSize,
                                double qualityTolerance,
                                double convergenceSpeedTolerance)
    {
        this.ih = ih;
        this.tl = tl;
        this.tsh = tsh;
        this.oh = oh;
        this.qualityPoolSize = qualityPoolSize;
        //this.qualityPool = new QualityPool<>(qualityPoolSize); // 1
        this.diversityPool = new DiversityPool<>(ih, tsh, qualityPoolSize);
        //this.improveDelaySum = (int) (ih.getMax() * 0.01);
        this.qualityTolerance = qualityTolerance;
        this.convergenceSpeedTolerance = convergenceSpeedTolerance;
        
        acceptanceBlock = map.getMoveAcceptanceBlock(
            this::evaluateAcceptance, 
            "Attributive acceptance", 
            "Acceptance based on a quality solution list from which attributes are extracted."
        );
    }
    
    /*private double calculateDiversityScore(HHAttributiveSolution target, boolean AddAttrData)
    {
        double diversitySum = 0.0;
        int currIteration = ih.getCurrent();
        
        for(Object attr : target.getAttributesCollection())
        {
            Integer currFrequency = frequencyTable.get(attr);
            
            if(currFrequency == null)
                currFrequency = 0;
            
            if(AddAttrData)
            {
                currFrequency++;
                frequencyTable.put(attr, currFrequency);
            }
            
            double attrFrequencyRatio = ((double) currFrequency) / currIteration;
            diversitySum += (1 - attrFrequencyRatio);
        }
        
        return diversitySum / (double) target.getAttributesCollection().getAttributesCount();
    }*/
    
    private HHAttributiveSolution getQualityZoneBottom()
    {
        /*
        double decreaseSpeed = Math.max(1.0, ih.getMax() * 0.001);
        int iterations = ih.getCurrent() - lastDiversityJumpIteration;
        double calculatedFactor = Math.tanh(iterations / decreaseSpeed);
        return diversityPool.getSolutionAt(calculatedFactor * 0.1).getSolution();
        */
        //double oscillationRate = oh.getCurrentOscillationRate();
        
        //int selectPos = (int)((qualityPool.getCurrentCount() / 4) * calculatedFactor);
        //return qualityPool.getSolutionAt(selectPos).getEvaluation();
               
        return diversityPool.getSolutionAt(qualityTolerance).getSolution();
    }
    
    /*private double getMinJumpZone()
    {
        return qualityPool.getWorser().getEvaluation();
    }*/
    
    private int getIterationsSinceLastImprove()
    {
        return ih.getCurrentIteration()- lastImproveIteration;
    }
    
    private int getIterationsSinceLastDiversityJump()
    {
        return ih.getCurrentIteration()- lastDiversityJumpIteration;
    }
    
    private boolean searchIsStuck(double speedProp)
    {
        return getIterationsSinceLastDiversityJump() >= getImproveSpeedReference(speedProp) && RandomTool.getDouble() > oh.getCurrentOscillationRate();
        //return oh.getCurrentOscillationRate() == 0 && getIterationsSinceLastDiversityJump() > getImproveSpeedReference();
        //return getIterationsSinceLastQualityJump() >= getImproveSpeedReference() && searchIsStuckAt(1.0);
    }
    
    private void resetLastImproveIteration()
    {
        lastImproveIteration = ih.getCurrentIteration();
    }
    
    private void resetLastDiversityJumpIteration()
    {
        lastDiversityJumpIteration = ih.getCurrentIteration();
    }
    
    private void registerImprovement()
    {
        if(convergenceList.size() == 100)
            convergenceList.removeLast();
        
        ListIterator<Integer> iterator = convergenceList.listIterator(convergenceList.size());
        int toRegister = ih.getCurrentIteration()- lastImproveIteration;
        
        while(iterator.hasPrevious())
        {
            int value = iterator.previous();
            
            if(value >= toRegister)
            {
                iterator.next();
                break;
            }
        }
        
        iterator.add(toRegister);
    }
    
    private int getImproveSpeedReference(double prop)
    {
        if(convergenceList.isEmpty())
            convergenceList.add(ih.getCurrentIteration());
        
        //return (int) (improveDelaySum / (double) improveDelayCount);
        return convergenceList.get((int)(convergenceList.size() * prop));
    }
    
    private boolean isAboveQualityZone(HHAttributiveSolution solution, double zoneProp)
    {
        return diversityPool.getSolutionAt(zoneProp).getSolution().compareTo(solution) <= 0;
    }
    
    private HHAttributiveSolution jumpTo(HHAttributiveSolution candidate)
    {
        return jumpTo(null, candidate);
    }
    
    private HHAttributiveSolution jumpTo(HHAttributiveSolution current, HHAttributiveSolution candidate)
    {
        if(current != null && current.compareTo(candidate) < 0)
        {
            registerImprovement();
            resetLastImproveIteration();
        }
        
        return candidate;
    }
    
    private boolean canAcceptWorserSolution(HHAttributiveSolution current, HHAttributiveSolution candidate)
    {
        if(!isAboveQualityZone(candidate, qualityTolerance))
            return false;
        
        double currentRatio = diversityPool.calculateDiversityRatio(current);
        double candidateRatio = diversityPool.calculateDiversityRatio(candidate);
        
        return candidateRatio > currentRatio;
        
        //if(!searchIsStuckAt(convergenceSpeedTolerance))
        //    return false;
        
        //if(getMinJumpZone() >= candidate.getEvaluation())
        //    return false;
        
        //double currentRatio = diversityPool.calculateDiversityRatio(current);
        //double candidateRatio = diversityPool.calculateDiversityRatio(candidate);
        
        //return candidateRatio > currentRatio/* || currentRatio * 0.5 <= candidateRatio*/;
        //return isNearQualityZone(candidate, 0.99);
    }
    
    private HHAttributiveSolution grabQualitySolution()
    {
        //return qualityPool.getSolutionAt(RandomTool.getInt(qualityPool.getCurrentCount()));
        //return diversityPool.getSolutionAt(RandomTool.getDouble()).getSolution();
        //return diversityPool.getBest().getSolution();
        
        //*        
        if(diversityPool.getCurrentCount() == 0)
            return tsh.getBestSolution();
        
        List<SolutionDiversityData<HHAttributiveSolution>> solutionsData = diversityPool.getInvertedSolutionData();
        
        RouletteSelector<SolutionDiversityData<HHAttributiveSolution>> selector = new RouletteSelector<>(
            solutionsData.size(),
            (se) -> se.getDiversityRatio()
        );
        
        //selector.setAmplificator(10.0);        
        selector.addAll(solutionsData);
        HHAttributiveSolution selected = selector.select().getSolution();
        
        return selected;
        //*/
    }
    
    private HHAttributiveSolution evaluateAcceptance(HHAttributiveSolution current, HHAttributiveSolution candidate)
    {        
        if(!tl.contains(candidate))
        {
            //qualityPool.tryAdd(candidate);
            //double candidateDiversity = calculateDiversityScore(candidate, true);
            diversityPool.tryAdd(candidate);
            
            if(candidate.compareTo(current) > 0)
            {
                resetLastDiversityJumpIteration();                
                return jumpTo(current, candidate);
            }
            
            if(canAcceptWorserSolution(current, candidate))
            {
                resetLastDiversityJumpIteration();
                return jumpTo(candidate);
            }
        }
        
        if(searchIsStuck(0.1))
        {
            resetLastDiversityJumpIteration();
            return jumpTo(current, grabQualitySolution());
        }
        
        return current;
    }
}

