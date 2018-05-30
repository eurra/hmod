package hmod.solvers.hh.models.elitesols;

import hmod.solvers.hh.HHSolution;

public interface EliteSolutionsSet<T extends HHSolution> {
    public int getCurrentCount();

    public T getEliteAt(int var1) throws IndexOutOfBoundsException;
}