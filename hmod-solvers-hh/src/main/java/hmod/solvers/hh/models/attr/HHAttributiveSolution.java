
package hmod.solvers.hh.models.attr;

import hmod.solvers.hh.HHSolution;

/**
 * Defines an extension of the common high-level solution which incorporates
 * attributive data.
 * @author Enrique Urra C.
 */
public interface HHAttributiveSolution<K, T extends HHAttributiveSolution<K, T>> extends HHSolution<T>
{
    AttributesCollection<K> getAttributesCollection();
}