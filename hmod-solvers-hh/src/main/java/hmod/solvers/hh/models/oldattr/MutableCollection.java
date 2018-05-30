
package hmod.solvers.hh.models.oldattr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

class MutableCollection<T>
implements AttributesCollection<T>,
AttributeRegister<T> {
    private final HashSet<T> attrs = new HashSet();

    @Override
    public void addAttribute(T attrId) {
        this.attrs.add(attrId);
    }

    @Override
    public void addAll(List<T> attributes) {
        this.attrs.addAll(attributes);
    }

    @Override
    public boolean hasAttribute(T attrId) {
        return this.attrs.contains(attrId);
    }

    @Override
    public Iterator<T> getAttributesIterator() {
        return this.attrs.iterator();
    }

    @Override
    public int getAttributesCount() {
        return this.attrs.size();
    }
}