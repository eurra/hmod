
package hmod.domains.binary;

import hmod.core.Statement;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.SelectableValue;
import optefx.loader.Selector;

/**
 *
 * @author Enrique Urra C.
 */
public final class BinaryDomain
{    
    public final static class BinaryHeuristic extends SelectableValue<Runnable>
    {
        private BinaryHeuristic()
        {
            super(BinaryDomain.class, (d) -> d.heuristics);
        }
    }
    
    @LoadsComponent({ BinarySolutionHandler.class, BinaryDomain.class })
    public static void load(ComponentRegister cr)
    {
        BinarySolutionHandler sh = cr.provide(new BinarySolutionHandler());
        cr.provide(new BinaryDomain(sh));
    }
    
    public static final BinaryHeuristic FLIP_ALL = new BinaryHeuristic();
    public static final BinaryHeuristic RANDOM_MUTATE = new BinaryHeuristic();
    public static final BinaryHeuristic RANDOMIZE = new BinaryHeuristic();
    
    private final Statement initSolution;
    private final Statement printBestSolution;
    private final Selector<BinaryHeuristic, Statement> heuristics = new Selector<>();

    private BinaryDomain(BinarySolutionHandler sh)
    {
        BinaryOperators bo = new BinaryOperators(sh);
        
        heuristics.add(FLIP_ALL, bo.convertSolution(BinaryOperators::flipAllConverter));
        heuristics.add(RANDOM_MUTATE, bo.convertSolution(BinaryOperators::randomMutateConverter));
        heuristics.add(RANDOMIZE, bo.convertSolution(BinaryOperators::randomizeConverter));  
        
        initSolution = bo::initSolution;
        printBestSolution = bo::printBestSolution;
    }

    public Statement initSolution() { return initSolution; }
    public Statement printBestSolution() { return printBestSolution; }
    public Statement heuristic(BinaryHeuristic h) { return heuristics.get(h); }
}
