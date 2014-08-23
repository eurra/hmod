
package hmod.loader.sax;

import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
class VirtualBeanMap
{
    private HashMap<String, Object> valuesMap;

    public VirtualBeanMap()
    {
        valuesMap = new HashMap<String, Object>();
    }
    
    public void set(String attr, Object val)
    {
        valuesMap.put(attr, val);
    }
    
    public Object get(String attr)
    {
        return valuesMap.get(attr);
    }
}
