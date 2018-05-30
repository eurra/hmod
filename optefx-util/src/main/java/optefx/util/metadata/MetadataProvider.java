
package optefx.util.metadata;

import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public interface MetadataProvider<M extends Metadata>
{
    <T extends M> T getData(Class<T> dataType);
    <T extends M> List<T> getAllData(Class<T> dataType);
    boolean hasData(Class<? extends M> dataType);
}
