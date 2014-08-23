
package hmod.domains.launcher.graph;

import flexbuilders.core.BuildException;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmScript
{
    private AlgorithmLayer[] layers;
    private ClassLoader contextClassLoader;

    public AlgorithmScript(AlgorithmLayer[] layers)
    {
        if(layers == null)
            throw new NullPointerException("Null layer array");
        
        this.layers = new AlgorithmLayer[layers.length];
        
        for(int i = 0; i < layers.length; i++)
        {
            if(layers[i] == null)
                throw new NullPointerException("Null graph at position " + i);
            
            this.layers[i] = layers[i];
        }
    }

    void setContextClassLoader(ClassLoader contextClassLoader)
    {
        this.contextClassLoader = contextClassLoader;
    }
    
    public final ClassLoader getContextLoader()
    {
        return contextClassLoader;
    }
    
    public final int getLayerCount()
    {
        return layers.length;
    }
    
    public final AlgorithmLayer getLayer(int pos) throws IndexOutOfBoundsException
    {
        if(pos < 0 || pos >= layers.length)
            throw new IndexOutOfBoundsException("Illegal graph index: " + pos);
        
        return layers[pos];
    }
    
    public abstract void process() throws BuildException;
}
