
package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.MutableIterationHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
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
    
    public static final Parameter<String> INSTANCE = new Parameter<>("DARPTWDomain.INSTANCE_FILE");
    public static final Parameter<Double> WEIGHT_TRANSIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_TRANSIT_TIME");
    public static final Parameter<Double> WEIGHT_ROUTE_DURATION = new Parameter<>("DARPTWDomain.WEIGHT_ROUTE_DURATION");
    public static final Parameter<Double> WEIGHT_SLACK_TIME = new Parameter<>("DARPTWDomain.WEIGHT_SLACK_TIME");
    public static final Parameter<Double> WEIGHT_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_EXCESS_RIDE_TIME = new Parameter<>("DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME");
    public static final Parameter<Double> WEIGHT_WAIT_TIME = new Parameter<>("DARPTWDomain.WEIGHT_WAIT_TIME");
    public static final Parameter<Double> WEIGHT_TIME_WINDOWS_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_TIME_WINDOWS_VIOLATION");
    public static final Parameter<Double> WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION");
    public static final Parameter<Double> WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION = new Parameter<>("DARPTWDomain.WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION");
    
    public static final DefaultHeuristic FILL_AVAILABLE_CLIENTS_RANDOMLY = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_RANDOM_CLIENT = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_SINGLE_EVENT = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_CLIENT_ALL_ROUTES = new DefaultHeuristic();
    public static final DefaultHeuristic MOVE_EVENT_ALL_ROUTES = new DefaultHeuristic();
    
    @LoadsComponent({
        DARPTWDomain.class,
        ProblemInstance.class,
        SolutionHandler.class,
        SolutionBuilder.class
    })
    public static void load(ComponentRegister cr,
                            ParameterRegister pr)
    {
        MutableFactorMap weightsMap = new MutableFactorMap();
        
        weightsMap.addFactor(Factor.TRANSIT_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_TRANSIT_TIME)));
        weightsMap.addFactor(Factor.ROUTE_DURATION, FactorValue.create(pr.getRequiredValue(WEIGHT_ROUTE_DURATION)));
        weightsMap.addFactor(Factor.SLACK_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_SLACK_TIME)));
        weightsMap.addFactor(Factor.RIDE_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_RIDE_TIME)));
        weightsMap.addFactor(Factor.EXCESS_RIDE_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_EXCESS_RIDE_TIME)));
        weightsMap.addFactor(Factor.WAIT_TIME, FactorValue.create(pr.getRequiredValue(WEIGHT_WAIT_TIME)));
        
        String file = pr.getRequiredValue(INSTANCE);
        ProblemInstance pi = cr.provide(DARPTWFactory.getInstance().createProblemInstance(file));
        
        FactorValue twvWeight = FactorValue.create(pr.isValueSet(WEIGHT_TIME_WINDOWS_VIOLATION) ? pr.getValue(WEIGHT_TIME_WINDOWS_VIOLATION) : pi.getClientsCount());
        FactorValue mrtvWeight = FactorValue.create(pr.isValueSet(WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION) ? pr.getValue(WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION) : pi.getClientsCount());
        FactorValue mrdvWeight = FactorValue.create(pr.isValueSet(WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION) ? pr.getValue(WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION) : pi.getClientsCount());
        weightsMap.addFactor(Factor.TIME_WINDOWS_VIOLATION, twvWeight);
        weightsMap.addFactor(Factor.MAXIMUM_RIDE_TIME_VIOLATION, mrtvWeight);
        weightsMap.addFactor(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, mrdvWeight);
        
        SolutionBuilder sb = cr.provide(new SolutionBuilder(pi));
        SolutionHandler sh = cr.provide(new SolutionHandler(pi, sb, weightsMap));
        cr.provide(new DARPTWDomain(sh, pi, sb));
    }
    
    private final Consumer<DARPTWSolution> setInputSolution;
    private final Statement loadNewSolution;
    private final Statement loadSolution;
    private final Statement saveSolution;
    private final Supplier<DARPTWSolution> getOutputSolution;
    private final Statement reportBestSolution;
    private final Selector<DARPTWHeuristic, Statement> heuristics = new Selector<>();

    private DARPTWDomain(SolutionHandler sh,
                         ProblemInstance pi,
                         SolutionBuilder sb)
    {
    
        setInputSolution = (sol) -> sh.setInputSolution(sol);
        reportBestSolution = DARPTWProcesses.reportResult(sh);
        saveSolution = sh::saveSolutionToOutput;
        getOutputSolution = sh::getOutputSolution;
        
        loadSolution = If(sh::isInputSolutionProvided).then(sh::loadInputSolution); 
        
        heuristics.add(FILL_AVAILABLE_CLIENTS_RANDOMLY, block(() -> {            
            List<Client> clients = new ArrayList<>();
            MutableIterationHandler iterator = new MutableIterationHandler();
            ClientHandler client = new ClientHandler();
            RouteHandler route = new RouteHandler();
            InsertionPositionHandler pos = new InsertionPositionHandler();
            
            return block(
                clients::clear,
                DARPTWProcesses.fillAvailableClients(sb, pi, clients),
                DARPTWProcesses.initIterationForClients(iterator, clients),
                While(NOT(iterator::areIterationsFinished)).Do(
                    DARPTWProcesses.selectClientFromIterator(iterator, clients, client),
                    DARPTWProcesses.selectRandomRoute(sb, route),
                    DARPTWProcesses.selectRandomInsertionPointInRoute(route, pos),
                    DARPTWProcesses.insertClient(pos, route, client),
                    iterator::advanceIteration
                )
            );
            
            /*
            
            
            
            ////////////////////////////////////
            
            ListVariable<Client> clients = new ListVariable<>();
            MutableIterationVariable iterator = new MutableIterationVariable();
            SettableVariable<Client> client = new SettableVariable<>();
            SettableVariable<Route> route = new SettableVariable<>();
            SettableVariable<Integer> pos = new SettableVariable<>();
            
            return block(
                set(clients, ArrayList::new),
                set(iterator, FlowchartFactory::new),
                clients::clear,
                While(NOT(iterator::isFinished)).Do(
                    set(client, DARPTWProcesses::selectClientFromIterator, iterator, clients),
                    set(route, DARPTWProcesses::selectRandomRoute, SOLUTION_BUILDER),
                    set(pos, DARPTWProcesses::selectRandomInsertionPointInRoute, route),
                    run(DARPTWProcesses::insertClient, pos, route, client),
                    run(iterator::advance)
                )
            );
            
            ////////////////////////////////////
            
            return block(
                set(clients, ArrayList::new),
                set(iterator, FlowchartFactory::new),
                clients.clear(),
                While(NOT(iterator.isFinished())).Do(
                    set(client, DARPTWProcesses.selectClientFromIterator(iterator, clients)),
                    set(route, DARPTWProcesses.selectRandomRoute(SOLUTION_BUILDER)),
                    set(pos, DARPTWProcesses.selectRandomInsertionPointInRoute(route)),
                    run(DARPTWProcesses.insertClient(pos, route, client)),
                    run(iterator.advance())
                )
            );        
            
            ////////////////////////////////////
            
            Variable<List<Client>> clients = Variable.create();
            Variable<MutableIterationHandler> iterator = Variable.create();
            Variable<Client> client = Variable.create();
            Variable<Route> route = Variable.create();
            Variable<Integer> pos = Variable.create();
                       
            return block(
                set(clients, ArrayList::new),
                set(iterator, FlowchartFactory::new),
                run(clients, c -> c.clear()),
                While(NOT(eval(iterator, i -> i.isFinished()))).Do(
                While(NOT(eval(IterationHandler::isFinished))).Do(
                    set(client, DARPTWProcesses::selectClientFromIterator, iterator, clients),
                    set(route, DARPTWProcesses::selectRandomRoute, SOLUTION_BUILDER),
                    set(pos, DARPTWProcesses::selectRandomInsertionPointInRoute, route),
                    run(DARPTWProcesses::insertClient, pos, route, client),
                    run(iterator, i -> i.advance())
                )
            );
            
            ////////////////////////////////////
            
                List<Client> clients = new ArrayList<>();
                FlowchartFactory iterator = new FlowchartFactory();
                clients.clear();
                while(!iterator.isFinished()) {
                    Client client = DARPTWProcesses.selectClientFromIterator(iterator, clients);
                    Route route = DARPTWProcesses.selectRandomRoute(SOLUTION_BUILDER);
                    Integer pos = DARPTWProcesses.selectRandomInsertionPointInRoute(route);
                    DARPTWProcesses.insertClient(pos, route, client);
                    iterator.advance();
                }
            */
        }));
        
        loadNewSolution = block(
            sh::loadEmptySolution,
            heuristics.get(FILL_AVAILABLE_CLIENTS_RANDOMLY)
        );
        
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
                While(NOT(ih::areIterationsFinished)).Do(DARPTWProcesses.selectRouteFromIterator(ih, randomSet, sourceRoute),
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
                While(NOT(ih::areIterationsFinished)).Do(DARPTWProcesses.selectRouteFromIterator(ih, randomSet, route),
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
    
    public Consumer<DARPTWSolution> setInputSolution() { return setInputSolution; }
    public Statement loadNewSolution() { return loadNewSolution; }
    public Statement loadSolution() { return loadSolution; }
    public Statement saveSolution() { return saveSolution; }
    public Supplier<DARPTWSolution> getOutputSolution() { return getOutputSolution; };
    public Statement reportBestSolution() { return reportBestSolution; }
    public Statement heuristic(DefaultHeuristic h) { return heuristics.get(h); }
}
