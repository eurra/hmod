
package hmod.domains.launcher.graph;

import flexbuilders.graph.NodeId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class NodeIdFinder
{
    private static class NodeEntry
    {
        private final String fullName;
        private final NodeId node;

        public NodeEntry(String fullName, NodeId node)
        {
            this.fullName = fullName;
            this.node = node;
        }

        public String getFullName()
        {
            return fullName;
        }

        public NodeId getNode()
        {
            return node;
        }
    }
    
    private final HashMap<String, NodeEntry[]> conflicts;
    private final HashMap<String, NodeEntry> mapping;

    public NodeIdFinder(ClassLoader loader)
    {
        Reflections refs = new Reflections(new ConfigurationBuilder().
            setUrls(ClasspathHelper.forPackage("hmod", loader)).
            setScanners(new SubTypesScanner()).
            addClassLoader(loader)
        );
        
        Set<Class<? extends Enum>> types = refs.getSubTypesOf(Enum.class);
        HashMap<String, ArrayList<NodeEntry>> founds = new HashMap<>(types.size());
        
        for(Class type : types)
        {
            if(!NodeId.class.isAssignableFrom(type))
                continue;
            
            String simpleEnumName = type.getSimpleName();
            String fullEnumName = type.getName();
            Object[] consts = type.getEnumConstants();
            
            for(int i = 0; i < consts.length; i++)
            {
                String constName = consts[i].toString();
                String entryName = simpleEnumName + "." + constName;
                
                ArrayList<NodeEntry> currEntries = founds.get(entryName);
                
                if(currEntries == null)
                {
                    currEntries = new ArrayList<>();
                    founds.put(entryName, currEntries);
                }
                
                currEntries.add(new NodeEntry(fullEnumName + "." + constName, (NodeId)consts[i]));
            }
        }
        
        conflicts = new HashMap<>();
        mapping = new HashMap<>();
        
        for(String entryName : founds.keySet())
        {
            ArrayList<NodeEntry> currEntries = founds.get(entryName);
            
            if(currEntries.size() > 1)
            {
                NodeEntry[] conflictEntries = currEntries.toArray(new NodeEntry[0]);
                conflicts.put(entryName, conflictEntries);
                
                for(NodeEntry entry : currEntries)
                    mapping.put(entry.getFullName(), entry);
            }
            else
            {
                mapping.put(entryName, currEntries.get(0));
            }
        }
    }
    
    public NodeId find(String input) throws NodeIdResolutionException
    {
        if(input == null)
            throw new NullPointerException("Null input node id");
        
        if(conflicts.containsKey(input))
        {
            NodeEntry[] entries = conflicts.get(input);
            String msg = "";
            
            for(int i = 0; i < entries.length; i++)
            {
                msg += entries[i].getFullName();
                
                if(i < entries.length - 1)
                    msg += ",  ";
            }
            
            throw new NodeIdResolutionException("The node id '" + input + "' is redundant regarding the following fields: " + msg);
        }        
        else if(!mapping.containsKey(input))
        {
            throw new NodeIdResolutionException("The node id '" + input + "' has not found");
        }
        else
        {
            return mapping.get(input).getNode();
        }
    }
}
