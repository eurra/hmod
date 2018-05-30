
package hmod.solvers.hh.models.attr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Implements a default attribute reading collection.
 * @author Enrique Urra C.
 */
class MutableCollection<T> implements AttributesCollection<T>, AttributeRegister<T>
{
    private final HashSet<T> attrs;

    public MutableCollection()
    {
        attrs = new HashSet<>();
    }

    @Override
    public void addAttribute(T attrId)
    {
        attrs.add(attrId);
    }
    
    @Override
    public void addAll(List<T> attributes)
    {
        attrs.addAll(attributes);
    }

    @Override
    public boolean hasAttribute(T attrId)
    {
        return attrs.contains(attrId);
    }

    @Override
    public Iterator<T> iterator()
    {
        return attrs.iterator();
    }

    @Override
    public int getAttributesCount()
    {
        return attrs.size();
    }
}