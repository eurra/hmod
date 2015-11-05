
package hmod.core;

import optefx.util.reflection.ReflectionTool;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public final class DataObjectProxy
{
    public static DataInterface createFor(Class<? extends DataInterface>... dataInterfaceTypes)
    {
        if(dataInterfaceTypes == null || dataInterfaceTypes.length == 0)
            throw new IllegalArgumentException("At least one valid data interface must be provided");
        
        return (DataInterface)Proxy.newProxyInstance(
            dataInterfaceTypes[0].getClassLoader(),
            dataInterfaceTypes,
            new DataObjectProxyHandler()
        );
    }
    
    private enum MethodType
    {
        SETTER, GETTER, INVALID;
    }
    
    private static class DataObjectProxyHandler implements InvocationHandler
    {
        private Map<String, Object> propertyMap;

        public DataObjectProxyHandler()
        {
            this.propertyMap = new HashMap<>();
        }
        
        private String extractPropertyName(String methodName)
        {
            return methodName.substring(3);
        }
        
        private MethodType checkMethodType(Method method)
        {
            String methodName = method.getName();
            Class methodReturnType = method.getReturnType();
            Class[] methodArgsTypes = method.getParameterTypes();
            
            if(methodName.startsWith("set"))
            {
                if(!methodReturnType.equals(Void.TYPE))
                    return MethodType.INVALID;
                
                if(methodArgsTypes.length != 1)
                    return MethodType.INVALID;
                
                return MethodType.SETTER;
            }
            else if(methodName.startsWith("get"))
            {
                if(methodReturnType.equals(Void.TYPE))
                    return MethodType.INVALID;
                
                if(methodArgsTypes.length != 0)
                    return MethodType.INVALID;
                
                return MethodType.GETTER;
            }
            else
            {
                return MethodType.INVALID;
            }
        }
        
        private void handlerSetter(Method method, Object[] os)
        {
            propertyMap.put(extractPropertyName(method.getName()), os[0]);
        }
        
        private Object handlerGetter(Method method)
        {
            String property = extractPropertyName(method.getName());
            
            if(!propertyMap.containsKey(property))
            {
                Class returnType = method.getReturnType();
                Object defaultValue;
                
                if(returnType.isPrimitive())
                    defaultValue = ReflectionTool.getDefaultPrimitiveValue(returnType);
                else
                    defaultValue = null;
                
                propertyMap.put(property, defaultValue);
            }
            
            return propertyMap.get(property);
        }

        @Override
        public Object invoke(Object o, Method method, Object[] os) throws Throwable
        {
            MethodType type = checkMethodType(method);
            
            switch(type)
            {
                case GETTER: return handlerGetter(method);
                case SETTER: handlerSetter(method, os); return null;
                default:
                case INVALID: throw new IllegalStateException("Cannot invoke the non-getter/setter method '" + method + "' on a data object proxy");
            }
        }
    }
}
