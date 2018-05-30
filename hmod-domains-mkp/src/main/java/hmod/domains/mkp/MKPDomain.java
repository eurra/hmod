
package hmod.domains.mkp;

import static hmod.core.FlowchartFactory.*;
import hmod.core.PlaceholderStatement;
import hmod.core.Statement;
import hmod.solvers.common.MutableIterationHandler;
import java.util.function.BiFunction;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.ModuleLoadException;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.SelectableValue;
import optefx.loader.Selector;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPDomain
{    
    public static final class DefaultFillMethod extends SelectableValue<Statement> implements MKPFillMethod
    {
        private DefaultFillMethod()
        {
            super(MKPDomain.class, (d) -> d.fillMethods);
        }
    }
    
    public static final class DefaultRemoveMethod extends SelectableValue<Statement> implements MKPRemoveMethod
    {
        private DefaultRemoveMethod()
        {
            super(MKPDomain.class, (d) -> d.heuristics);
        }
    }
    
    public static final DefaultFillMethod RANDOM_FILL = new DefaultFillMethod();
    public static final DefaultFillMethod GREEDY_FILL = new DefaultFillMethod();
    public static final DefaultRemoveMethod REMOVE_RANDOM = new DefaultRemoveMethod();
    public static final DefaultRemoveMethod REMOVE_GREEDY = new DefaultRemoveMethod();
    
    public static final Parameter<MKPFillMethod> FILL_METHOD = new Parameter<>("MKPDomain.FILL_METHOD");  
    public static final Parameter<String> INSTANCE = new Parameter<>("MKPDomain.INSTANCE_FILE");
    public static final Parameter<String> LP_OPTIMUM_SET = new Parameter<>("MKPDomain.LP_OPTIMUM_SET");
    
    @LoadsComponent({ MKPDomain.class, ProblemInstanceHandler.class, SolutionHandler.class, SolutionBuilderHandler.class })
    public static void load(ComponentRegister cr, ParameterRegister pr) throws ModuleLoadException
    {
        MKPFillMethod fm = pr.getRequiredValue(FILL_METHOD);
        String[] instanceFileInfo = pr.getRequiredValue(INSTANCE).split(":");
        String instanceFile = instanceFileInfo[0];
        int instanceNumber = Integer.parseInt(instanceFileInfo[1]);
        String lpOptimumsFile = pr.getValue(LP_OPTIMUM_SET);
        
        ProblemInstanceHandler pih = cr.provide(new ProblemInstanceHandler(instanceFile, instanceNumber, lpOptimumsFile));
        MutableSolutionHandler sh = cr.provide(new MutableSolutionHandler(), SolutionHandler.class);
        SolutionBuilderHandler sbh = cr.provide(new SolutionBuilderHandler(pih));
        MKPDomain mkpDomain = cr.provide(new MKPDomain(pih, sh, sbh));
        
        pr.addBoundHandler(fm, (v) -> mkpDomain.fillMethod.set(v));
    }
    
    private Statement initSolution;
    private Statement loadSolution;
    private Statement saveSolution;
    private Statement reportSolution;
    private MKPOperators mkpOps;
    private SolutionBuilderHandler sbh;
    private ProblemInstanceHandler pih;
    private final Selector<MKPFillMethod, Statement> fillMethods = new Selector<>();
    private final Selector<MKPRemoveMethod, Statement> heuristics = new Selector<>();
    private final PlaceholderStatement<Statement> fillMethod = new PlaceholderStatement<>();

    public Statement initSolution() { return initSolution; }
    public Statement loadSolution() { return loadSolution; }
    public Statement saveSolution() { return saveSolution; }
    public Statement reportSolution() { return reportSolution; }
    public Statement fillMethod(DefaultFillMethod fm) { return fillMethods.get(fm); }
    public Statement removeMethod(DefaultRemoveMethod h) { return heuristics.get(h); }

    private MKPDomain(ProblemInstanceHandler pih,
                      MutableSolutionHandler sh,
                      SolutionBuilderHandler sbh)
    {
        this.mkpOps = new MKPOperators(sh, sbh);
        this.sbh = sbh;
        this.pih = pih;
        
        initSolution = block(
            If(NOT(sh::isSolutionProvided)).then(
                sbh::clear,
                fillMethod
            )
        );
        
        loadSolution = block(
            If(sh::isSolutionProvided).then(
                mkpOps::loadProvidedSolutionInBuilder
            )
        );
        
        saveSolution = mkpOps::saveBuildedSolutionForRetrieving;
        reportSolution = mkpOps::reportSolution;
        
        heuristics.add(REMOVE_RANDOM, block(() -> {
            SelectedItemHandler sih = new SelectedItemHandler(pih);

            return block(
                sih::clear,
                mkpOps.selectRandomIncludedItemInBuild(sih),
                mkpOps.removeSelectedItemFromBuild(sih)
            );
        }));
        
        heuristics.add(REMOVE_GREEDY, block(() -> {
            SelectedItemHandler sih = new SelectedItemHandler(pih);
            ItemListHandler ilh = new ItemListHandler(pih);

            return block(
                sih::clear, ilh::clear,
                mkpOps.storeCurrentItemsInList(ilh),
                MKPOperators.selectWorstProfitableItemInList(ilh, sih),
                mkpOps.removeSelectedItemFromBuild(sih)
            );
        }));
        
        fillMethods.add(RANDOM_FILL, fillMethod(MKPOperators::selectRandomItemInList));
        fillMethods.add(GREEDY_FILL, fillMethod(MKPOperators::selectMostProfitableItemInList));
    }
    
    public Statement multiRemove(Statement removeMethodBlock, double perc, boolean random)
    {
        return block(() -> {
            MutableIterationHandler iterationHandler = new MutableIterationHandler();

            return block(
                mkpOps.initRandomIteratorFromCurrentItems(iterationHandler, perc, random),
                While(NOT(iterationHandler::areIterationsFinished)).Do(
                    removeMethodBlock,
                    iterationHandler::advanceIteration
                )
            );
        });
    }
    
    public Statement fillMethod(BiFunction<ItemListHandler, SelectedItemHandler, Statement> selector)
    {
        ItemListHandler itemListHandler = new ItemListHandler(pih);
        SelectedItemHandler selectedItemHandler = new SelectedItemHandler(pih);
        
        return block(
            itemListHandler::clear,
            mkpOps.storeAvailableItemsInList(itemListHandler),
            While(NOT(itemListHandler::isEmpty)).Do(
                selector.apply(itemListHandler, selectedItemHandler),
                If(mkpOps.checkFeasibleAdd(selectedItemHandler)).then(
                    mkpOps.addSelectedItemToBuild(selectedItemHandler)
                ),
                MKPOperators.removeSelectedItemFromList(selectedItemHandler, itemListHandler)
            )
        );
    }
}
