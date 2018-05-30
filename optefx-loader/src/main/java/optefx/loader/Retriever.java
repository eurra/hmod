
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public interface Retriever
{
    <T> T get(Class<T> requiredType) throws ModuleLoadException;
    boolean contains(Class type);
}
