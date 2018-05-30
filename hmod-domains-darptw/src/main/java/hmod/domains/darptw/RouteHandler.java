
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class RouteHandler
{
    private Route currRoute;

    public void selectRoute(Route route)
    {
        if(route == null)
            throw new NullPointerException("Null route");

        this.currRoute = route;
    }

    public void unselectCurrentRoute() throws IllegalStateException
    {
        if(currRoute == null)
            throw new IllegalStateException("No route has been selected");

        this.currRoute = null;
    }

    public Route getCurrentRoute() throws IllegalStateException
    {
        if(currRoute == null)
            throw new IllegalStateException("No route has been selected");

        return currRoute;
    }
    
    public boolean isCurrentRouteModifiable() throws IllegalStateException
    {
        return getCurrentRoute().isModifiable();
    }

    public boolean isRouteSelected()
    {
        return currRoute != null;
    }
}
