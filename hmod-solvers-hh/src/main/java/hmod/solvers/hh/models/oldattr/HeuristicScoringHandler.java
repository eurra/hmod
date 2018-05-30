
package hmod.solvers.hh.models.oldattr;

import hmod.core.Statement;
import hmod.solvers.hh.models.oldattr.AttributesCollection;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import java.util.HashMap;
import java.util.Iterator;

public class HeuristicScoringHandler<T> {
    private HashMap<Statement, HeuristicScoringHandler<T>> stats;

    HeuristicScoringHandler(LLHeuristicsHandler hh) {
        if (hh == null) {
            throw new NullPointerException("Null heuristic handler");
        }
        this.stats = new HashMap(hh.getHeuristicsCount());
        int count = hh.getHeuristicsCount();
        for (int i = 0; i < count; ++i) {
            this.stats.put(hh.getHeuristicAt(i), (HeuristicScoringHandler<T>)((Object)new HeuristicStats()));
        }
    }

    public void addReadingFor(Statement heuristic, AttributesCollection<T> beforeAttrs, AttributesCollection<T> afterAttrs) {
        int attrsOnBoth = 0;
        Iterator<T> iterator = beforeAttrs.getAttributesIterator();
        while (iterator.hasNext()) {
            if (!afterAttrs.hasAttribute(iterator.next())) continue;
            ++attrsOnBoth;
        }
        double resRate = 1.0 - (double)attrsOnBoth / (double)(afterAttrs.getAttributesCount() + beforeAttrs.getAttributesCount() - attrsOnBoth);
        this.addReadingFor(heuristic, resRate);
    }

    public void addReadingFor(Statement heuristic, double reading) {
        if (!this.stats.containsKey((Object)heuristic)) {
            throw new IllegalArgumentException("The provided heuristic do no belongs to this collection");
        }
        HeuristicStats stat = (HeuristicStats)((Object)this.stats.get((Object)heuristic));
        stat.changeImpactTotal += reading;
        ++stat.readingCount;
    }

    public double getCurrentScore(Statement heuristic) {
        if (!this.stats.containsKey((Object)heuristic)) {
            throw new IllegalArgumentException("The provided heuristic do no belongs to this collection");
        }
        HeuristicStats stat = (HeuristicStats)((Object)this.stats.get((Object)heuristic));
        if ((double)stat.readingCount == 0.0) {
            return 0.5;
        }
        return stat.changeImpactTotal / (double)stat.readingCount;
    }

    public boolean hasScore(Statement heuristic) {
        return this.stats.containsKey((Object)heuristic);
    }

    private class HeuristicStats {
        public int readingCount;
        public double changeImpactTotal;

        private HeuristicStats() {
        }
    }

}
