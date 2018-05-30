
package optefx.util.metadata;

/**
 *
 * @author Enrique Urra C.
 */
public interface MutableMetadataProvider<T extends Metadata> extends MetadataProvider<T>
{
    MutableMetadataProvider addData(T... data);
}
