
package optefx.util.bundlelib;

/**
 *
 * @author Enrique Urra C.
 */
public class BundleException extends RuntimeException
{
    public BundleException(String string)
    {
        super(string);
    }

    public BundleException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    public BundleException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}
