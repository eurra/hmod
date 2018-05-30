
package optefx.loader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public interface Resolvable<T>
{
    public static <T, K, P extends Resolvable<T>> P proxy(Class<P> customType, Resolvable<? extends T> instance)
    {
        Objects.requireNonNull(customType, "null custom type");
        Objects.requireNonNull(instance, "null instance");
        
        InvocationHandler ih = (proxy, method, args) -> method.invoke(instance, args);
        return (P)Proxy.newProxyInstance(customType.getClassLoader(), new Class[] { customType }, ih);
    }
    
    public static <T, K> Resolvable<T> boundTo(Class<K> boundedType, Function<K, T> func)
    {
        Objects.requireNonNull(boundedType, "null bounded type");
        Objects.requireNonNull(func, "null function");
        
        return (ret) -> {
            K bounded = ret.get(boundedType);
            return Objects.requireNonNull(func.apply(bounded), "null result on bounded function");
        };
    }
    
    public static <T, K, P extends Resolvable<T>> P boundTo(Class<P> customType, Class<K> boundedType, Function<K, T> func)
    {
        Resolvable<T> res = boundTo(boundedType, func);
        return proxy(customType, res);
    }
    
    T resolve(Retriever ret) throws ModuleLoadException;
}
