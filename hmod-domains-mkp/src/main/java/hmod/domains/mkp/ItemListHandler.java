
package hmod.domains.mkp;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Enrique Urra C.
 */
public final class ItemListHandler
{
    private final MKPProblemInstance problemInstance;
    private final ArrayList<Item> list;
    private final HashSet<Item> set;

    public ItemListHandler(MKPProblemInstance problemInstance)
    {
        if(problemInstance == null)
            throw new NullPointerException("Null problem instance");
        
        this.problemInstance = problemInstance;
        this.list = new ArrayList<>();
        this.set = new HashSet<>();
    }

    private void checkIndex(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= list.size())
            throw new IndexOutOfBoundsException("Wrong index: " + index);
    }

    public void addItem(Item item)
    {
        if(item == null)
            throw new NullPointerException("Null item");
        
        problemInstance.checkItem(item);        
        list.add(item);
        set.add(item);
    }

    public void removeItem(Item item) throws IllegalArgumentException
    {
        if(!set.contains(item))
            throw new IllegalArgumentException("The specified item do not belongs to this list");
        
        list.remove(item);
        set.remove(item);
    }

    public Item getItemAt(int pos) throws IndexOutOfBoundsException
    {
        checkIndex(pos);
        return list.get(pos);
    }

    public void clear()
    {
        list.clear();
        set.clear();
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    public int getItemCount()
    {
        return list.size();
    }
}
