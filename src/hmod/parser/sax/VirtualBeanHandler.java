
package hmod.parser.sax;

import mobs.ObjectBuildException;
import hmod.util.ReflectionTool;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Enrique Urra C.
 */
class VirtualBeanHandler implements InvocationHandler
{
    private VirtualBeanMap valuesMap;

    public VirtualBeanHandler()
    {
        valuesMap = new VirtualBeanMap();
    }

    @Override
    public Object invoke(Object o, Method method, Object[] os) throws ObjectBuildException
    {
        String field = null;
        
        if((field = extractFieldFromSetter(method, os)) != null)
        {
            valuesMap.set(field, os[0]);
            return null;
        }
        else if((field = extractFieldFromGetter(method, os)) != null)
        {
            Object retVal = valuesMap.get(field);
            
            if(retVal == null)
                retVal = ReflectionTool.getDefaultPrimitiveValue(method.getReturnType());
            
            return retVal;
        }
        else
        {
            throw new ObjectBuildException("A virtual bean only can handle getter and setter methods");
        }
    }
    
    private String extractFieldFromSetter(Method method, Object[] args)
    {
        // A setter must have only one argument
        if(args == null || args.length > 1)
            return null;
        
        // The return type of a setter must be void
        if(!method.getReturnType().equals(Void.TYPE))
            return null;
        
        // The name must start with "set"
        String origName = method.getName();
                
        if(!origName.startsWith("set"))
            return null;
        
        // After "set", some name must be there
        String baseName = origName.replaceAll("set", "");

        if(baseName.isEmpty())
            return null;
        
        // At this point, the setter is valid, we extract the field name
        return extractField(baseName);
    }
    
    private String extractFieldFromGetter(Method method, Object[] args)
    {
        // A setter must have zero arguments
        if(args != null)
            return null;
        
        // The return type of a getter cannot be void
        if(method.getReturnType().equals(Void.TYPE))
            return null;
        
        // The name must start with "get"
        String origName = method.getName();
                
        if(!origName.startsWith("get"))
            return null;
        
        // After "get", some name must be there
        String baseName = origName.replaceAll("get", "");

        if(baseName.isEmpty())
            return null;
        
        // At this point, the setter is valid, we extract the field name
        return extractField(baseName);
    }
    
    private String extractField(String baseName)
    {
        String firstChar = baseName.substring(0, 1);
        String baseWithoutFirst = baseName.substring(1, baseName.length());
        firstChar = firstChar.toLowerCase();

        return firstChar + baseWithoutFirst;
    }
}
