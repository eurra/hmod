
package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public final class SelectedItemHandler
{
    private Item item;
    private final MKPProblemInstance instance;

    public SelectedItemHandler(MKPProblemInstance instance)
    {
        if(instance == null)
            throw new NullPointerException("Null instance");
        
        this.instance = instance;
    }

    public void selectItem(Item item)
    {
        if(item == null)
            throw new NullPointerException("Cannot set a null item");
        
        if(!instance.itemExists(item))
            throw new IllegalArgumentException("The item do not belongs to the instance");
        
        this.item = item;
    }

    public Item getSelected() throws IllegalStateException
    {
        if(item == null)
            throw new IllegalStateException("No item has been selected");
        
        return item;
    }

    public void clear()
    {
        item = null;
    }

    public boolean isItemSelected()
    {
        return item != null;
    }
}
