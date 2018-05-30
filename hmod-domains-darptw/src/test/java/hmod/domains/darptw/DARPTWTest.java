package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.common.MutableIterationHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import optefx.loader.Module;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWTest
{
    @BeforeClass
    public static void init()
    {
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(DARPTWOutputIds.RESULT_DETAIL)//.
            //addSystemOutputId(DARPTWOutputIds.OPERATION_INFO)
        ); 
    }
    
    private Statement initHeuristic(SolutionBuilder sb,
                                    ProblemInstance pi)
    {
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
    }
    
    private Statement moveRandomClient(SolutionBuilder sb,
                                       ProblemInstance pi)
    {
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
    }
    
    @Test
    public void standaloneTest()
    {
        ProblemInstance pi = DARPTWFactory.getInstance().createProblemInstance("../launcher-testing/input/problems/darptw/pr01.txt");
        
        MutableFactorMap weightsMap = new MutableFactorMap();
        weightsMap.addFactor(Factor.TRANSIT_TIME, FactorValue.create(8.0));
        weightsMap.addFactor(Factor.ROUTE_DURATION, FactorValue.create(1.0));
        weightsMap.addFactor(Factor.SLACK_TIME, FactorValue.create(0.0));
        weightsMap.addFactor(Factor.RIDE_TIME, FactorValue.create(0.0));
        weightsMap.addFactor(Factor.EXCESS_RIDE_TIME, FactorValue.create(3.0));
        weightsMap.addFactor(Factor.WAIT_TIME, FactorValue.create(1.0));
        weightsMap.addFactor(Factor.TIME_WINDOWS_VIOLATION, FactorValue.create(pi.getClientsCount()));
        weightsMap.addFactor(Factor.MAXIMUM_RIDE_TIME_VIOLATION, FactorValue.create(pi.getClientsCount()));
        weightsMap.addFactor(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION, FactorValue.create(pi.getClientsCount()));
        
        SolutionBuilder sb = new SolutionBuilder(pi);
        SolutionHandler sh = new SolutionHandler(pi, sb, weightsMap);
        
        Statement heuristic = block(
            sh::loadEmptySolution,
            initHeuristic(sb, pi),
            moveRandomClient(sb, pi),
            sh::saveSolutionToOutput,
            DARPTWProcesses.reportResult(sh)
        );        
        
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(DARPTWOutputIds.RESULT_DETAIL)
        );
        
        heuristic.run();
    }
    
    @Test
    public void moduleTest()
    {
        AltDARPTWDomain domain = new ModuleLoader().
            loadAll(AltDARPTWDomain.class, DummyDARPTW.class).
            setParameter(AltDARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt").
            setParameter(AltDARPTWDomain.WEIGHT_TRANSIT_TIME, 8.0).
            setParameter(AltDARPTWDomain.WEIGHT_RIDE_TIME, 0.0).
            setParameter(AltDARPTWDomain.WEIGHT_EXCESS_RIDE_TIME, 3.0).
            setParameter(AltDARPTWDomain.WEIGHT_WAIT_TIME, 1.0).
            setParameter(AltDARPTWDomain.WEIGHT_SLACK_TIME, 0.0).
            setParameter(AltDARPTWDomain.WEIGHT_ROUTE_DURATION, 1.0).
            setParameter(AltDARPTWDomain.INITIALIZER, AltDARPTWDomain.DEFAULT_INIT).
            getInstance(AltDARPTWDomain.class);
                
        domain.solutionCheck().append(
            () -> OutputManager.println("This won't have absolutely no effect!")
        );
        
        Statement heuristic = block(
            domain.loadNewSolution(),
            domain.heuristic(AltDARPTWDomain.MOVE_RANDOM_CLIENT),
            domain.saveSolution(),
            domain.reportSolution()
        );        
        
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(DARPTWOutputIds.RESULT_DETAIL)
        );
        
        heuristic.run();
    }
    
    @Test
    public void iterativeTest()
    {
        Heuristic heuristic = new ModuleLoader().
            loadAll(
                IterativeHeuristic.class,
                AltDARPTWDomain.class,
                IterativeDARPTW.class
            ).
            setParameter(AltDARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt").
            setParameter(AltDARPTWDomain.WEIGHT_TRANSIT_TIME, 8.0).
            setParameter(AltDARPTWDomain.WEIGHT_RIDE_TIME, 0.0).
            setParameter(AltDARPTWDomain.WEIGHT_EXCESS_RIDE_TIME, 3.0).
            setParameter(AltDARPTWDomain.WEIGHT_WAIT_TIME, 1.0).
            setParameter(AltDARPTWDomain.WEIGHT_SLACK_TIME, 0.0).
            setParameter(AltDARPTWDomain.WEIGHT_ROUTE_DURATION, 1.0).
            setParameter(AltDARPTWDomain.INITIALIZER, AltDARPTWDomain.DEFAULT_INIT).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 100).
            setParameter(IterativeHeuristic.MAX_SECONDS, 30.0).
            getInstance(Heuristic.class);
        
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(DARPTWOutputIds.RESULT_DETAIL).
            addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO)
        );
        
        heuristic.run();
    }
    
    @Test
    public void doTest() throws IOException
    {
        Module mod = new ModuleLoader().
            load(DARPTWDomain.class).
            setParameter(DARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt").
            setParameter(DARPTWDomain.WEIGHT_TRANSIT_TIME, 8.0).
            setParameter(DARPTWDomain.WEIGHT_RIDE_TIME, 0.0).
            setParameter(DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME, 3.0).
            setParameter(DARPTWDomain.WEIGHT_WAIT_TIME, 1.0).
            setParameter(DARPTWDomain.WEIGHT_SLACK_TIME, 0.0).
            setParameter(DARPTWDomain.WEIGHT_ROUTE_DURATION, 1.0).
            setParameter(DARPTWDomain.WEIGHT_TIME_WINDOWS_VIOLATION, 24.0).
            setParameter(DARPTWDomain.WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION, 24.0).
            setParameter(DARPTWDomain.WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION, 24.0).
            getModule();
        
        DARPTWDomain domain = mod.getInstance(DARPTWDomain.class);
        SolutionHandler sh = mod.getInstance(SolutionHandler.class);
        
        run(domain.loadNewSolution(),
            sh::saveSolutionToOutput,
            domain.reportBestSolution(),
            

            domain.heuristic(DARPTWDomain.MOVE_RANDOM_CLIENT),
            domain.saveSolution(),
            domain.reportBestSolution(),

            domain.heuristic(DARPTWDomain.MOVE_SINGLE_EVENT),
            domain.saveSolution(),
            domain.reportBestSolution(),

            domain.heuristic(DARPTWDomain.MOVE_CLIENT_ALL_ROUTES),
            domain.saveSolution(),
            domain.reportBestSolution(),

            domain.heuristic(DARPTWDomain.MOVE_EVENT_ALL_ROUTES),
            domain.saveSolution(),
            domain.reportBestSolution()
        );
    }
}
