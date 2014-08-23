
package hmod.loader.graph;

import java.util.NoSuchElementException;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultArgumentsList implements ArgumentList
{
    private final Object[] args;
    private int current;

    public DefaultArgumentsList(Object[] args)
    {
        if(args == null)
            throw new NullPointerException("Null args");
        
        this.args = args;
    }
    
    @Override
    public <T> T next(Class<T> type, T defaultVal)
    {
        if(current >= args.length)
            return defaultVal;
        
        int origPos = current++;
        
        if(!type.isAssignableFrom(args[origPos].getClass()))
            return defaultVal;
        
        return type.cast(args[origPos]);
    }
    
    @Override
    public <T> T next(Class<T> type) throws NoSuchElementException
    {
        return getAt(type, current++);
    }

    @Override
    public <T> T getAt(Class<T> type, int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= args.length)
            throw new IndexOutOfBoundsException("Wrong argument position: " + index);
            
        if(!type.isAssignableFrom(args[index].getClass()))
            throw new NoSuchElementException("Current argument is not compatible with '" + type + "' ('" + args[index].getClass() + "' was found)");
                
        return type.cast(args[index]);
    }
}
