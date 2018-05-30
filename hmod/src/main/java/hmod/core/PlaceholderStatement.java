
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class PlaceholderStatement<T extends Statement> extends ComposableStatement
{
    private T st;
    
    public void set(T st)
    {
        this.st = st;
    }

    @Override
    protected T get()
    {
        return st;
    }
}
