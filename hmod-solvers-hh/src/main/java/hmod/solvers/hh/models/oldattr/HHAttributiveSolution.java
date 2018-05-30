
package hmod.solvers.hh.models.oldattr;

import hmod.solvers.hh.HHSolution;

public interface HHAttributiveSolution<K, T extends HHAttributiveSolution<K, T>>
extends HHSolution<T> {
    public AttributesCollection<K> getAttributesCollection();
}