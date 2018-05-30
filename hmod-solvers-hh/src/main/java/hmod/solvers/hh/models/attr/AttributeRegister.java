
package hmod.solvers.hh.models.attr;

import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public interface AttributeRegister<T>
{
    void addAll(List<T> attributes);
    void addAttribute(T attrId);
}
