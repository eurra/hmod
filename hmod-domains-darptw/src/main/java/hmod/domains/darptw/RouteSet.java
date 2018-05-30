
package hmod.domains.darptw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
public class RouteSet implements Iterable<Route>
{
    private final ArrayList<Route> list;
    private final HashSet<Route> set;
    private final int maxSize;

    public RouteSet()
    {
        this.maxSize = -1;
        this.list = new ArrayList<>();
        this.set = new HashSet<>();
    }
    
    public RouteSet(int maxSize) throws IllegalArgumentException
    {
        if(maxSize < 0)
            throw new IllegalArgumentException("Invalid max. size: " + maxSize);
        
        this.maxSize = maxSize;
        this.list = new ArrayList<>(maxSize);
        this.set = new HashSet<>(maxSize);
    }
    
    public final void importRoutes(Route[] routes) throws DARPTWException
    {
        clear();
        
        for(Route route : routes)
            addRoute(route);
    }

    public void addRoute(Route route) throws DARPTWException
    {
        if(set.contains(Objects.requireNonNull(route, "null route")))
            throw new DARPTWException("The route is already added");
        
        if(set.size() == maxSize)
            throw new DARPTWException("Cannot add: maximum capacity reached (" + maxSize + ")");

        list.add(route);
        set.add(route);
    }
    
    public void removeRoute(Route route) throws DARPTWException
    {
        if(!set.contains(Objects.requireNonNull(route, "null route")))
            throw new DARPTWException("The route has not been added");
        
        list.remove(route);
        set.remove(route);
    }
    
    public int indexOf(Route route)
    {
        return list.indexOf(route);
    }

    public void clear()
    {
        list.clear();
        set.clear();
    }

    public final Route getRouteAt(int index) throws IndexOutOfBoundsException, IllegalStateException
    {
        if(set.isEmpty())
            throw new IllegalStateException("No routes have been added");

        if(index < 0 || index >= list.size())
            throw new IndexOutOfBoundsException("Wrong index: " + index);

        return list.get(index);
    }

    public final int getRoutesCount()
    {
        return set.size();
    }
    
    public List<Route> getRoutesList()
    {
        return list;
    }
    
    public Set<Route> getRoutesSet()
    {
        return set;
    }
    
    public final boolean isRouteIncluded(Route route)
    {
        return set.contains(route);
    }

    @Override
    public final Iterator<Route> iterator()
    {
        return list.iterator();
    }
}
