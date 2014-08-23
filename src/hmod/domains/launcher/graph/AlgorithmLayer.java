
package hmod.domains.launcher.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.DisposableGraphHandler;
import static flexbuilders.graph.GraphFactory.graph;
import flexbuilders.graph.GraphHandler;
import flexbuilders.graph.NodeBuilder;
import flexbuilders.graph.NodeId;

/**
 *
 * @author Enrique Urra C.
 */
public final class AlgorithmLayer implements GraphHandler
{ 
    private final NodeIdFinder nodeIdFinder;
    private DisposableGraphHandler innerHandler;

    AlgorithmLayer(NodeIdFinder nodeFinder)
    {
        this.innerHandler = graph();
        this.nodeIdFinder = nodeFinder;
    }

    @Override
    public NodeBuilder node(NodeId id)
    {
        return innerHandler.node(id);
    }

    public NodeBuilder node(String id) throws NodeIdResolutionException
    {
        return node(nodeIdFinder.find(id));
    }

    @Override
    public NodeBuilder node()
    {
        return innerHandler.node();
    }

    @Override
    public boolean hasNode(NodeId id)
    {
        return innerHandler.hasNode(id);
    }

    public boolean hasNode(String id) throws NodeIdResolutionException
    {
        return hasNode(nodeIdFinder.find(id));
    }

    @Override
    public NestedBuilder ref(NodeId destId)
    {
        return innerHandler.ref(destId);
    }

    public NestedBuilder ref(String destId) throws NodeIdResolutionException
    {
        return ref(nodeIdFinder.find(destId));
    }

    void dispose()
    {
        innerHandler.dispose();
        innerHandler = null;
    }
}
