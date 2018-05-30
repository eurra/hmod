
package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public class Resource
{
    private int id;
    private int capacity;

    public Resource(int id, int capacity)
    {
        if(id < 0)
            throw new IllegalArgumentException("Negative id");
        
        if(capacity < 0)
            throw new IllegalArgumentException("Negative capacity");
        
        this.id = id;
        this.capacity = capacity;
    }

    public int getId()
    {
        return id;
    }

    public int getCapacity()
    {
        return capacity;
    }
}
