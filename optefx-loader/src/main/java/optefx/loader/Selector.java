
package optefx.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class Selector<K, V>
{
    protected final Map<K, V> map = new HashMap<>();

    public <P extends V> P add(K key, P value)
    {
        Objects.requireNonNull(key, "null key");
        Objects.requireNonNull(value, "null value");
        
        map.put(key, value);
        return value;
    }

    public V get(K key) throws NoSuchElementException
    {
        if(!map.containsKey(key))
            throw new NoSuchElementException();
        
        return map.get(key);
    }
}
