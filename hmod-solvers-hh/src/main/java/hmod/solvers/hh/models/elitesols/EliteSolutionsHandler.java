package hmod.solvers.hh.models.elitesols;

import hmod.solvers.hh.HHSolution;
import java.util.LinkedList;
import java.util.ListIterator;

public class EliteSolutionsHandler<T extends HHSolution>
implements EliteSolutionsSet<T> {
    private final int maxElites;
    private final LinkedList<T> eliteList;

    public EliteSolutionsHandler(int maxElites) throws IllegalArgumentException {
        if (maxElites <= 0) {
            throw new IllegalArgumentException("Max. elites must be greather than 0");
        }
        this.maxElites = maxElites;
        this.eliteList = new LinkedList();
    }

    @Override
    public int getCurrentCount() {
        return this.eliteList.size();
    }

    public int getMaxElites() {
        return this.maxElites;
    }

    public boolean isFull() {
        return this.getCurrentCount() >= this.getMaxElites();
    }

    @Override
    public T getEliteAt(int pos) throws IndexOutOfBoundsException {
        return (T)((HHSolution)this.eliteList.get(pos));
    }

    public boolean tryAddElite(T candidate) {
        int currElites = this.eliteList.size();
        if (currElites == 0) {
            this.eliteList.add(candidate);
            return true;
        }
        HHSolution worstBestSolution = (HHSolution)this.eliteList.getLast();
        if (worstBestSolution.compareTo(candidate) > 0) {
            return false;
        }
        ListIterator<T> iterator = this.eliteList.listIterator(currElites);
        while (iterator.hasPrevious()) {
            HHSolution indexSolution = (HHSolution)iterator.previous();
            int compResult = indexSolution.compareTo(candidate);
            if (compResult == 0) {
                return false;
            }
            if (compResult <= 0) continue;
            iterator.next();
            break;
        }
        iterator.add(candidate);
        if (++currElites > this.maxElites) {
            this.eliteList.removeLast();
        }
        return true;
    }
}
