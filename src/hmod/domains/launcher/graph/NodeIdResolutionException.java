
package hmod.domains.launcher.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public class NodeIdResolutionException extends BuildException
{
    public NodeIdResolutionException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }

    public NodeIdResolutionException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    public NodeIdResolutionException(String message)
    {
        super(message);
    }
}
