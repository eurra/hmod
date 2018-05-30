
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
@FunctionalInterface
public interface PostProcessor
{
    void process(Retriever ret, ParameterRegister pr);
}
