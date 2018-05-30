
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public interface ComponentRegister
{
    <T> T provide(T implementor) throws ModuleLoadException;
    <T> T provide(T implementor, Class<? super T>... providedTypes) throws ModuleLoadException;
}
