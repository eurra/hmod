
package hmod.solvers.hh.models.oldattr;

import hmod.core.Statement;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.hh.HHSolutionHandler;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.selection.LowLevelHeuristicInfo;
import hmod.solvers.hh.models.soltrack.SolutionTrackingHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import optefx.util.output.OutputManager;

public final class StrategicOscillationProcesses {
    private final IterationHandler ih;
    private final StrategicOscillationHandler soh;
    private final HeuristicRunnerHandler hrh;
    private final HeuristicScoringHandler<String> hsh;
    private final HHSolutionHandler<HHAttributiveSolution> sh;
    private final LLHeuristicsHandler llhh;
    private final SolutionTrackingHandler sth;
    private long printTimeStamp = -1;

    StrategicOscillationProcesses(IterationHandler ih, StrategicOscillationHandler soh, HeuristicRunnerHandler hrh, HeuristicScoringHandler hsh, HHSolutionHandler<HHAttributiveSolution> sh, LLHeuristicsHandler llhh, SolutionTrackingHandler sth) {
        this.ih = ih;
        this.soh = soh;
        this.hrh = hrh;
        this.hsh = hsh;
        this.sh = sh;
        this.llhh = llhh;
        this.sth = sth;
    }

    public void updatePIMultiplier() {
        this.soh.updatePIMultiplier(this.sth.getGlobalNoImproveIterations());
    }

    public void updateStatsOfCurrentHeuristic() {
        Statement currStep = this.hrh.getHeuristicToRun();
        if (!this.hsh.hasScore(currStep)) {
            return;
        }
        HHAttributiveSolution<String, ?> inputSol = this.sh.getInputSolution();
        HHAttributiveSolution<String, ?> outputSol = this.sh.getOutputSolution();
        AttributesCollection<String> inputAttrs = inputSol.getAttributesCollection();
        AttributesCollection<String> outputAttrs = outputSol.getAttributesCollection();
        this.hsh.addReadingFor(currStep, inputAttrs, outputAttrs);
        this.printHeuristicStatsSheet();
    }

    public void selectHeuristicByChangeImpact() {
        double oscillationRate = this.soh.getCurrentOscillationRate();
        double growProportion = this.soh.getGrowProportion();
        int localNonImprovingIterations = this.sth.getLocalNoImproveIterations();
        double amplificationScore = this.soh.getAmplificationScore();
        int heuristicsCount = this.llhh.getHeuristicsCount();
        double currSubScore = 1.0 - (Math.exp(1.5707963267948966) - 1.0) / (growProportion * (double)localNonImprovingIterations + Math.exp(1.5707963267948966) - 1.0);
        ArrayList<HeuristicScore> scores = new ArrayList<HeuristicScore>(heuristicsCount);
        for (int i = 0; i < heuristicsCount; ++i) {
            Statement heuristic = this.llhh.getHeuristicAt(i);
            double currScore = this.hsh.getCurrentScore(heuristic);
            double currFinalScore = 1.0 + currScore * (currSubScore * (1.0 - oscillationRate) - 1.0);
            scores.add(new HeuristicScore(heuristic, currFinalScore));
        }
        List orderedScores = scores.stream().sorted((x, y) -> {
            if (x.getScore() < y.getScore()) {
                return -1;
            }
            if (x.getScore() > y.getScore()) {
                return 1;
            }
            return 0;
        }
        ).map(hs -> hs.heuristic).collect(Collectors.toList());
        //RouletteSelector rs = new RouletteSelector(orderedScores).setAmplificator(amplificationScore);
        //int selectedPos = rs.select();
        //this.hrh.setHeuristicToRun(((HeuristicScore)scores.get(selectedPos)).getHeuristic());
    }

    public void printIterationData() {
        OutputManager.println((String)"selHyp-so-data-sheet", (Object)("" + this.ih.getCurrentIteration()+ "\t" + this.soh.getPIMultiplier() + "\t" + this.soh.getCurrentOscillationRate() + "\t" + this.sth.getGlobalNoImproveIterations() + "\t" + this.sth.getLocalNoImproveIterations()));
    }

    public void printHeuristicScores() {
        PrintWriter pw = OutputManager.getCurrent().getOutput("selHyp-so-info");
        if (pw != null) {
            int count = this.llhh.getHeuristicsCount();
            StringBuilder sb = new StringBuilder("Low-level heuristic change impact scores:");
            for (int i = 0; i < count; ++i) {
                LowLevelHeuristicInfo llhInfo = this.llhh.getInfoAt(i);
                String name = (llhInfo == null ? new StringBuilder().append("Heuristic ").append(i).toString() : llhInfo.getName()) + ": ";
                sb.append("\n").append("- ").append(name).append(String.format("%.6f", this.hsh.getCurrentScore(this.llhh.getHeuristicAt(i))));
            }
            pw.println(sb.toString());
        }
    }

    public void printHeuristicStatsSheet() {
        PrintWriter pw = OutputManager.getCurrent().getOutput("hmod.solvers.hh.models.attr.heuristicsStatsSheet");
        if (pw != null && (this.printTimeStamp == -1 || System.currentTimeMillis() - this.printTimeStamp >= 100)) {
            this.printTimeStamp = System.currentTimeMillis();
            int count = this.llhh.getHeuristicsCount();
            String line = "";
            for (int i = 0; i < count; ++i) {
                line = line + String.format("%.6f\t", this.hsh.getCurrentScore(this.llhh.getHeuristicAt(i)));
            }
            pw.println(line);
        }
    }

    private static class HeuristicScore {
        private final Statement heuristic;
        private double score;

        public HeuristicScore(Statement heuristic, double initScore) {
            this.heuristic = heuristic;
            this.score = initScore;
        }

        public double getScore() {
            return this.score;
        }

        public Statement getHeuristic() {
            return this.heuristic;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

}
