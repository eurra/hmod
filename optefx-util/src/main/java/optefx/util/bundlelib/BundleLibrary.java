
package optefx.util.bundlelib;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
public final class BundleLibrary
{    
    public static BundleLibrary getFromPaths(String... paths) throws BundleException
    {
        return getFromPaths(true, paths);
    }
    
    public static BundleLibrary getFromPaths(boolean searchRecursively, String... paths) throws BundleException
    {
        Set<URL> filesURLs = new HashSet<>();
        
        for (String path : paths)
            loadBundlesFromPath(path, filesURLs, new HashSet<>(), searchRecursively);

        ClassLoader loader = URLClassLoader.newInstance(filesURLs.toArray(new URL[0]));
        return new BundleLibrary(loader);
    }
    
    private static void loadBundlesFromPath(String path, Set<URL> filesURLs, Set<String> checkedPaths, boolean searchRecursively)
    {
        File folder = new File(path);
        
        if(checkedPaths.contains(path))
            return;
        
        if(!folder.exists() || !folder.isDirectory())
           folder.mkdirs();
        
        File[] filesInFolder = folder.listFiles();
        List<String> subFolders = new ArrayList<>();
        
        for (File toCheck : filesInFolder)
        {
            if(fileIsJar(toCheck))
            {   
                try
                {
                    filesURLs.add(toCheck.toURI().toURL());
                }
                catch(MalformedURLException ex)
                {
                    throw new BundleException("Cannot load script bundles from jar: " + ex.getLocalizedMessage(), ex);
                }
            }
            else if(toCheck.isDirectory())
            {
                subFolders.add(toCheck.getPath());
            }
        }
        
        checkedPaths.add(path);
        
        if(searchRecursively)
        {
            int subFoldersCount = subFolders.size();
        
            for(int i = 0; i < subFoldersCount; i++)
                loadBundlesFromPath(subFolders.get(i), filesURLs, checkedPaths, true);
        }
    }
    
    private static boolean fileIsJar(File file)
    {
        if(!file.isFile())
            return false;
        
        String name = file.getName().toLowerCase();        
        return name.endsWith(".jar") || name.endsWith(".zip");
    }
    
    private final ClassLoader loader;

    private BundleLibrary(ClassLoader loader)
    {        
        this.loader = loader;
    }

    public ClassLoader getLoader()
    {
        return loader;
    }
    
    public final Class loadClass(String className) throws BundleException
    {        
        try
        {
            return (Class)loader.loadClass(className);
        }
        catch(ClassCastException ex)
        {
            throw new BundleException("The class '" + className + "' is not a valid script type", ex);
        }
        catch(ClassNotFoundException ex)
        {
            throw new BundleException("The script class '" + className + "' was not found", ex);
        }
    }
}