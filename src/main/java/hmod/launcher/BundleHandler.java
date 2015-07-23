
package hmod.launcher;

import optefx.util.bundlelib.BundleException;
import optefx.util.bundlelib.BundleLibrary;

/**
 *
 * @author Enrique Urra C.
 */
public final class BundleHandler
{
    private BundleLibrary library;
    private final String[] paths;
    
    BundleHandler(String... paths)
    {
        this.paths = new String[paths.length];
        
        for(int i = 0; i < paths.length; i++)
        {
            if(paths[i] == null)
                throw new NullPointerException("Null path at position " + i);
            
            this.paths[i] = paths[i];
        }
        
        reload();
    }
    
    private BundleLibrary getLibrary() throws BundleException
    {
        if(library == null)
            library = BundleLibrary.getFromPaths(paths);
        
        return library;
    }

    public Class loadClass(String name) throws BundleException
    {
        BundleLibrary lib = getLibrary();
        return lib.loadClass(name);
    }

    public ClassLoader getLoader()
    {
        BundleLibrary lib = getLibrary();
        return lib.getLoader();
    }
    
    public final void reload()
    {
        library = null;
    }
}
