
package hmod.domains.darptw;

import java.util.BitSet;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class SolutionBuilder extends RouteSet
{
    private final ProblemInstance pi;
    private final BitSet clientsMask;

    public SolutionBuilder(ProblemInstance pi)
    {
        this.pi = pi;
        this.clientsMask = new BitSet(Objects.requireNonNull(pi, "null instance").getClientsCount());
    }

    @Override
    public void addRoute(Route route) throws DARPTWException
    {
        super.addRoute(route);
        clientsMask.or(route.getClientMask());
    }

    @Override
    public void removeRoute(Route route) throws DARPTWException
    {
        super.removeRoute(route);
        clientsMask.andNot(route.getClientMask());
    }
    
    public int getClientsCount()
    {
        return clientsMask.cardinality();
    }
    
    public BitSet getAvailableClients()
    {
        BitSet res = getIncludedClients();
        res.flip(0, res.cardinality());
        
        return res;
    }
    
    public BitSet getIncludedClients()
    {
        return (BitSet)clientsMask.clone();
    }

    @Override
    public void clear()
    {
        super.clear();
        clientsMask.clear();
    }
}
