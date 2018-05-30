
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public class TaggedEntity
{
    private final String name;

    public TaggedEntity()
    {
        this.name = null;
    }
    
    public TaggedEntity(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return name == null ? super.toString() : name;
    }
}
