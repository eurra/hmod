
package hmod.domains.launcher.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.GraphHandler;
import flexbuilders.graph.NodeBuilder;
import flexbuilders.graph.NodeId;


/**
 * 
 * @author Enrique Urra C.
 */
public abstract class SingleLayerScript extends AlgorithmScript implements GraphHandler
{
    public SingleLayerScript(AlgorithmLayer layer)
    {
        super(layer);
    }
    
    @Override
    public final NodeBuilder node(NodeId id)
    {
        return getLayer(0).node(id);
    }

    @Override
    public final NodeBuilder node()
    {
        return getLayer(0).node();
    }

    @Override
    public final NestedBuilder ref(NodeId id)
    {
        return getLayer(0).ref(id);
    }

    @Override
    public boolean hasNode(NodeId id)
    {
        return getLayer(0).hasNode(id);
    }
}