
package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.*;
import hmod.core.PlaceholderStatement;
import hmod.core.Routine;
import hmod.core.Statement;
import hmod.core.ValidableRoutine;
import hmod.solvers.common.MutableIterationHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
public final class DARPTWDomain
{
    public static class DefaultHeuristic extends SelectableValue<Statement> implements DARPTWHeuristic
    {
        private DefaultHeuristic()
        {
            super(DARPTWDomain.class, (d) -> d.heuristics);
        }
    }
    
    public static final Parameter<String> INSTANCE = new Parameter<>("DARPTWDomain.INSTANCE");    
    public static final Parameter<Double> WEIGHT_TRANSIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_TRANSIT_TIME");
    public static final Parameter<Double> WEIGHT_ROUTE_DURATION = new Parameter<>("DARPTWDomain.WEIGHT_ROUTE_DURATION");
    public static final Parameter<Double> WEIGHT_SLACK_TIME = new Parameter<>("DARPTWDomain.WEIGHT_SLACK_TIME");
    public static final Parameter<Double> WEIGHT_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_EXCESS_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_WAIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_WAIT_TIME");
    public static final Parameter<Double> WEIGHT_TIME_WINDOWS_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_TIME_WINDOWS_VIOLATION");
    public static final Parameter<Double> WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION");
    public static final Parameter<Double> WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION");
    public static final Parameter<DARPTWInitializer> INITIALIZER = new Parameter<>("DARPTWDomain.INITIALIZER");
    
    public static final DARPTWInitializer DEFAULT_INIT = Resolvable.boundTo(DARPTWInitializer.class,
        DARPTWDomain.class, 
        (domain) -> domain.initHeuristic
    );
    
    public static final DefaultHeuristic FILL_AVAILABLE_CLIENTS_RANDOMLY = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_RANDOM_CLIENT = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_CLIENT_ALL_ROUTES = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_SINGLE_EVENT = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_EVENT_ALL_ROUTES = new DefaultHeuristic();
    
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
    
    @LoadsComponent(DARPTWDomain.class)
    public static void loadDomain(ComponentRegister cr,
                                  ParameterRegister pr,
                                  ProblemInstance pi,
                                  SolutionHandler sh,
                                  SolutionBuilder sb)
    {
        DARPTWDomain domain = cr.provide(new DARPTWDomain(sh, pi, sb));        
        DARPTWInitializer initResolver = pr.getRequiredValue(INITIALIZER);
        pr.addBoundHandler(initResolver, (st) -> domain.init.set(st));
    }
    
    private final Consumer<DARPTWSolution> setInputSolution;    
    private final Statement loadNewSolution;
    private final Statement loadSolution;
    private final Statement saveSolution;
    private final Supplier<DARPTWSolution> getOutputSolution;
    private final Statement reportSolution;
    private final PlaceholderStatement<Statement> init = new PlaceholderStatement<>();
    private final Statement initHeuristic;
    private final Selector<DefaultHeuristic, Statement> heuristics = new Selector<>();
    private final ValidableRoutine solutionCheck = new ValidableRoutine("DARPTWDomain.solutionCheck", false);

    private DARPTWDomain(SolutionHandler sh, ProblemInstance pi, SolutionBuilder sb)
    {
        setInputSolution = (sol) -> sh.setInputSolution(sol);
        reportSolution = DARPTWProcesses.reportResult(sh);
        saveSolution = sh::saveSolutionToOutput; 
        getOutputSolution = sh::getOutputSolution;
        loadSolution = If(sh::isInputSolutionProvided).then(sh::loadInputSolution);
        
        loadNewSolution = block(
            sh::loadEmptySolution,
            init,
            solutionCheck
        );
        
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
                If(DARPTWProcesses.thereAreRoutes(modRoutes, 2)).then(
                    DARPTWProcesses.selectRandomRoute(modRoutes, sourceRoute),
                    DARPTWProcesses.selectOtherRoute(modRoutes, sourceRoute, targetRoute),
                    DARPTWProcesses.selectRandomClientFromRoute(pi, sourceRoute, toMove),
                    DARPTWProcesses.selectRandomInsertionPointInRoute(targetRoute, pos),
                    DARPTWProcesses.moveClient(sourceRoute, targetRoute, toMove, pos)
                )
            );
        }));
        
        heuristics.add(MOVE_CLIENT_ALL_ROUTES, block(() -> {
            RouteSet randomSet = new RouteSet();
            RouteHandler sourceRoute = new RouteHandler();
            RouteHandler targetRoute = new RouteHandler();
            ClientHandler toMove = new ClientHandler();
            InsertionPositionHandler pos = new InsertionPositionHandler();
            MutableIterationHandler ih = new MutableIterationHandler();
            
            return block(
                randomSet::clear,
                DARPTWProcesses.storeRandomRouteSequence(sb, randomSet),
                DARPTWProcesses.initIterationFromRoutes(ih, randomSet),
                While(NOT(ih::areIterationsFinished)).Do(
                    DARPTWProcesses.selectRouteFromIterator(ih, randomSet, sourceRoute),
                    If(sourceRoute::isCurrentRouteModifiable).then(
                        DARPTWProcesses.selectOtherRoute(randomSet, sourceRoute, targetRoute),
                        DARPTWProcesses.selectRandomClientFromRoute(pi, sourceRoute, toMove),
                        DARPTWProcesses.selectRandomInsertionPointInRoute(targetRoute, pos),
                        DARPTWProcesses.moveClient(sourceRoute, targetRoute, toMove, pos)
                    ),
                    ih::advanceIteration
                )
            );
        }));
        
        heuristics.add(MOVE_SINGLE_EVENT, block(() -> {
            RouteSet modRoutes = new RouteSet();
            RouteHandler route = new RouteHandler();
            EventPositionList movableEvents = new EventPositionList();
            EventPositionHandler origEvent = new EventPositionHandler();
            EventPositionHandler destEvent = new EventPositionHandler();
            
            return block(
                modRoutes::clear,
                DARPTWProcesses.pickModifiableRoutes(sb, modRoutes),
                DARPTWProcesses.selectRandomRoute(modRoutes, route),
                DARPTWProcesses.pickMovableEvents(pi, route, movableEvents),
                DARPTWProcesses.pickRandomEventFromList(movableEvents, origEvent),
                DARPTWProcesses.selectRandomMovePoint(pi, route, origEvent, destEvent),
                DARPTWProcesses.moveEvent(route, origEvent, destEvent)
            );
        }));
        
        heuristics.add(MOVE_EVENT_ALL_ROUTES, block(() -> {
            RouteSet randomSet = new RouteSet();
            RouteHandler route = new RouteHandler();
            EventPositionList movableEvents = new EventPositionList();
            EventPositionHandler origEvent = new EventPositionHandler();
            EventPositionHandler destEvent = new EventPositionHandler();
            MutableIterationHandler ih = new MutableIterationHandler();
            
            return block(
                randomSet::clear,
                DARPTWProcesses.storeRandomRouteSequence(sb, randomSet),
                DARPTWProcesses.initIterationFromRoutes(ih, randomSet),
                While(NOT(ih::areIterationsFinished)).Do(
                    DARPTWProcesses.selectRouteFromIterator(ih, randomSet, route),
                    If(route::isCurrentRouteModifiable).then(
                        DARPTWProcesses.pickMovableEvents(pi, route, movableEvents),
                        DARPTWProcesses.pickRandomEventFromList(movableEvents, origEvent),
                        DARPTWProcesses.selectRandomMovePoint(pi, route, origEvent, destEvent),
                        DARPTWProcesses.moveEvent(route, origEvent, destEvent)
                    ),
                    ih::advanceIteration
                )
            );
        }));
    }
    
    public Statement loadNewSolution() { return loadNewSolution; }
    public Statement loadSolution() { return loadSolution; }
    public Statement saveSolution() { return saveSolution; }
    public Supplier<DARPTWSolution> getOutputSolution() { return getOutputSolution; };
    public Consumer<DARPTWSolution> setInputSolution() { return setInputSolution; };
    public Statement reportSolution() { return reportSolution; }
    public Statement heuristic(DefaultHeuristic h) { return heuristics.get(h); }
    public Routine solutionCheck() { return solutionCheck; }
    
    @Processable
    private void process()
    {
        solutionCheck.validate();
    }
}
