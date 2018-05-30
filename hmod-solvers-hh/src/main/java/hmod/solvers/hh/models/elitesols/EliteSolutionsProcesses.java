package hmod.solvers.hh.models.elitesols;

import hmod.core.Condition;
import hmod.core.Statement;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.RouletteSelector;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.HHSolutionHandler;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

public class EliteSolutionsProcesses<T extends HHSolution> {
    private final EliteSolutionsHandler<T> esh;
    private final HHSolutionHandler<T> sh;
    private final IterationHandler ih;
    private int lastReplaceIteration = 0;

    public static <T extends HHSolution> Function<EliteSolutionsSet<T>, T> randomEliteSelector() {
        return set -> {
            int count = set.getCurrentCount();
            return set.getEliteAt(RandomTool.getInt((int)count));
        }
        ;
    }

    public static <T extends HHSolution> Function<EliteSolutionsSet<T>, T> rouletteEliteSelector(double amplificator) {
        return set -> {
            int count = set.getCurrentCount();
            ArrayList<T> scores = new ArrayList<>(count);
            for (int i = 0; i < count; ++i) {
                scores.add(set.getEliteAt(i));
            }
            RouletteSelector<T> rs = new RouletteSelector<>((T s) -> s.getEvaluation()).setAmplificator(amplificator);
            rs.addAll(scores);
            return rs.select();
        }
        ;
    }

    public EliteSolutionsProcesses(EliteSolutionsHandler<T> esh, HHSolutionHandler<T> sh, IterationHandler ih) {
        this.esh = Objects.requireNonNull(esh, "null elite solutions handler");
        this.sh = Objects.requireNonNull(sh, "null solutions handler");
        this.ih = Objects.requireNonNull(ih, "null iteration handler");
    }

    public void updateEliteSolutions() {
        T outputSolution = this.sh.getOutputSolution();
        this.esh.tryAddElite(outputSolution);
    }

    public Condition areIterationsPassedSinceLastReplace(int numIterations) {
        return () -> this.ih.getCurrentIteration()- this.lastReplaceIteration >= numIterations;
    }

    public Statement replaceOutputSolutionWithElite(Function<EliteSolutionsSet<T>, T> criteria) {
        Objects.requireNonNull(criteria, "null criteria");
        return () -> {
            T sol = criteria.apply(this.esh);
            this.sh.setOutputSolution(sol);
            this.lastReplaceIteration = this.ih.getCurrentIteration();
            OutputManager.println((String)"hmod.solvers.hh.models.elitesols.events", (Object)("Current solution restarted to: " + sol.getEvaluation()));
        }
        ;
    }
}