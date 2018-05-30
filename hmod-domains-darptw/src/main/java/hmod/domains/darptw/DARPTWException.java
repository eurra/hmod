
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWException extends RuntimeException
{
    public DARPTWException(String string)
    {
        super(string);
    }

    public DARPTWException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    public DARPTWException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}
