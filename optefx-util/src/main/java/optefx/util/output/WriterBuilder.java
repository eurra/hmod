
package optefx.util.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Enrique Urra C.
 */
public interface WriterBuilder
{
    Writer build() throws IOException;
}
