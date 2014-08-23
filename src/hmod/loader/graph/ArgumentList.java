
package hmod.loader.graph;

import java.util.NoSuchElementException;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArgumentList
{
    static ArgumentList from(Object... args)
    {
        return new DefaultArgumentsList(args);
    }
    
    static ArgumentList empty()
    {
        return new DefaultArgumentsList(new Object[]{});
    }
    
    <T> T next(Class<T> type) throws NoSuchElementException;
    <T> T next(Class<T> type, T defaultVal);
    <T> T getAt(Class<T> type, int index) throws IndexOutOfBoundsException;
}
