
package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.NOT;
import static hmod.core.FlowchartFactory.While;
import static hmod.core.FlowchartFactory.block;
import hmod.core.PlaceholderStatement;
import hmod.core.Routine;
import hmod.core.Statement;
import hmod.core.ValidableRoutine;
import hmod.solvers.common.MutableIterationHandler;
import java.util.ArrayList;
import java.util.List;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Processable;
import optefx.loader.Resolvable;
import optefx.loader.SelectableValue;
import optefx.loader.Selector;

/**
 *
 * @author Enrique Urra C.
 */
public final class AltDARPTWDomain
{
    public static class DefaultHeuristic extends SelectableValue<Statement> implements DARPTWHeuristic
    {
        private DefaultHeuristic()
        {
            super(AltDARPTWDomain.class, (d) -> d.heuristics);
        }
    }
    
    public static final Parameter<String> INSTANCE = new Parameter<>("DARPTWDomain.INSTANCE");    
    public static final Parameter<Double> WEIGHT_TRANSIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_TRANSIT_TIME");
    public static final Parameter<Double> WEIGHT_ROUTE_DURATION = new Parameter<>("DARPTWDomain.WEIGHT_ROUTE_DURATION");
    public static final Parameter<Double> WEIGHT_SLACK_TIME = new Parameter<>("DARPTWDomain.WEIGHT_SLACK_TIME");
    public static final Parameter<Double> WEIGHT_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_EXCESS_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_WAIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_WAIT_TIME");
    public static final Parameter<DARPTWInitializer> INITIALIZER = new Parameter<>("DARPTWDomain.INITIALIZER");
    
    public static final DARPTWInitializer DEFAULT_INIT = Resolvable.boundTo(
        DARPTWInitializer.class,
        AltDARPTWDomain.class, 
        (domain) -> domain.initHeuristic
    );
    
    public static final DefaultHeuristic MOVE_RANDOM_CLIENT = new DefaultHeuristic();
    
    @LoadsComponent({ SolutionBuilder.class, SolutionHandler.class })
    public static void loadSolutionData(ComponentRegister cr,
                                        ParameterRegister pr,
                                        ProblemInstance pi)
    {
        MutableFactorMap weightsMap = new MutableFactorMap();        
        weightsMap.addFactor(Factor.TRANSIT_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_TRANSIT_TIME)));
        weightsMap.addFactor(Factor.ROUTE_DURATION, FactorValue.create(pr.getRequiredValue(WEIGHT_ROUTE_DURATION)));
        weightsMap.addFactor(Factor.SLACK_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_SLACK_TIME)));
        weightsMap.addFactor(Factor.RIDE_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_RIDE_TIME)));
        weightsMap.addFactor(Factor.EXCESS_RIDE_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_EXCESS_RIDE_TIME)));
        weightsMap.addFactor(Factor.WAIT_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_WAIT_TIME)));
        weightsMap.addFactor(Factor.TIME_WINDOWS_VIOLATION, FactorValue.create(pi.getClientsCount()));
        weightsMap.addFactor(Factor.MAXIMUM_RIDE_TIME_VIOLATION, FactorValue.create(pi.getClientsCount()));
        weightsMap.addFactor(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, FactorValue.create(pi.getClientsCount()));
        
        SolutionBuilder sb = cr.provide(new SolutionBuilder(pi));
        cr.provide(new SolutionHandler(pi, sb, weightsMap));
    }
    
    @LoadsComponent(ProblemInstance.class)
    public static void loadProblemInstance(ComponentRegister cr, ParameterRegister pr)
    {
        String file = pr.getRequiredValue(INSTANCE);
        cr.provide(DARPTWFactory.getInstance().createProblemInstance(file));
    }
    
    @LoadsComponent(AltDARPTWDomain.class)
    public static void loadDomain(ComponentRegister cr,
                                  ParameterRegister pr,
                                  ProblemInstance pi,
                                  SolutionHandler sh,
                                  SolutionBuilder sb)
    {
        AltDARPTWDomain domain = cr.provide(new AltDARPTWDomain(sh, pi, sb));        
        DARPTWInitializer initResolver = pr.getRequiredValue(INITIALIZER);
        pr.addBoundHandler(initResolver, (st) -> domain.init.set(st));
    }
    
    private final Statement loadNewSolution;
    private final Statement saveSolution;
    private final Statement reportSolution;
    private final PlaceholderStatement<Statement> init = new PlaceholderStatement<>();
    private final Statement initHeuristic;
    private final Selector<DefaultHeuristic, Statement> heuristics = new Selector<>();
    private final ValidableRoutine solutionCheck = new ValidableRoutine("DARPTWDomain.solutionCheck", false);

    private AltDARPTWDomain(SolutionHandler sh, ProblemInstance pi, SolutionBuilder sb)
    {
        initHeuristic = block(() -> {            
            List<Client> clients = new ArrayList<>();
            MutableIterationHandler iterator = new MutableIterationHandler();
            ClientHandler client = new ClientHandler();
            RouteHandler route = new RouteHandler();
            InsertionPositionHandler pos = new InsertionPositionHandler();
            
            return block(
                clients::clear,
                DARPTWProcesses.fillAvailableClients(sb, pi, clients),
                DARPTWProcesses.initIterationForClients(iterator, clients),
                While(NOT(iterator::areIterationsFinished)).Do(DARPTWProcesses.selectClientFromIterator(iterator, clients, client),
                    DARPTWProcesses.selectRandomRoute(sb, route),
                    DARPTWProcesses.selectRandomInsertionPointInRoute(route, pos),
                    DARPTWProcesses.insertClient(pos, route, client),
                    iterator::advanceIteration
                )
            );
        });
        
        heuristics.add(MOVE_RANDOM_CLIENT, block(() -> {
            RouteSet modRoutes = new RouteSet();
            RouteHandler sourceRoute = new RouteHandler();
            RouteHandler targetRoute = new RouteHandler();
            ClientHandler toMove = new ClientHandler();
            InsertionPositionHandler pos = new InsertionPositionHandler();
            
            return block(
                modRoutes::clear,
                DARPTWProcesses.pickModifiableRoutes(sb, modRoutes),
                DARPTWProcesses.selectRandomRoute(modRoutes, sourceRoute),
                DARPTWProcesses.selectOtherRoute(modRoutes, sourceRoute, targetRoute),
                DARPTWProcesses.selectRandomClientFromRoute(pi, sourceRoute, toMove),
                DARPTWProcesses.selectRandomInsertionPointInRoute(targetRoute, pos),
                DARPTWProcesses.moveClient(sourceRoute, targetRoute, toMove, pos)
            );
        }));
        
        loadNewSolution = block(
            sh::loadEmptySolution,
            init,
            solutionCheck
        );
        
        saveSolution = sh::saveSolutionToOutput;
        reportSolution = DARPTWProcesses.reportResult(sh);
    }
    
    public Statement loadNewSolution() { return loadNewSolution; }
    public Statement saveSolution() { return saveSolution; }
    public Statement reportSolution() { return reportSolution; }
    public Statement heuristic(DefaultHeuristic h) { return heuristics.get(h); }
    public Routine solutionCheck() { return solutionCheck; }
    
    @Processable
    private void process()
    {
        solutionCheck.validate();
    }
}
