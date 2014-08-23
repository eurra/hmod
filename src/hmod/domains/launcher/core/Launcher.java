
package hmod.domains.launcher.core;

import flexbuilders.graph.GraphHandler;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.domains.launcher.graph.AlgorithmScriptLoader;
import hmod.core.HModConfig;
import hmod.core.Step;
import hmod.domains.launcher.core.ac.LauncherIds;
import hmod.domains.launcher.graph.AlgorithmLayer;
import hmod.domains.launcher.graph.AlgorithmScript;
import java.util.Set;
import optefx.util.output.OutputManager;
import optefx.util.output.OutputConfigBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class Launcher
{
    public static final String OUT_COMMON = "launcher-output-common";
    public static final String OUT_ERROR = "launcher-output-error";
    
    public static final String EXTENSIONS_ENTRY = "hmod.launcher.extensionsPath";
    public static final String ENTRY_BATCH_PATH = "hmod.launcher.batchPath";
    public static final String ENTRY_OUTPUT_PATH = "hmod.launcher.outputPath";
    public static final String ENTRY_DEBUG = "hmod.launcher.debug";
    public static final String ENTRY_THREADING = "hmod.launcher.threading";
    public static final String ENTRY_RUNNER_ID = "hmod.launcher.runnerId";
    public static final String ENTRY_INTERFACE_ID = "hmod.launcher.interfaceId";
    public static final String ENTRY_WINDOWS_AUTOCLOSE = "hmod.launcher.windowsAutoclose";
    
    public static void main(String[] args) throws AlgorithmException, AlgorithmLoadException, LauncherException
    {
        OutputManager.getCurrent().setOutputsFromConfig(
            new OutputConfigBuilder().
            addSystemOutputId(OUT_COMMON).makePersistent(OUT_COMMON).
            addSystemErrorOutputId(OUT_ERROR).makePersistent(OUT_ERROR).
            build()
        );
        
        HModConfig config = HModConfig.getInstance();
        String[] extensionsPath = config.getEntry(EXTENSIONS_ENTRY, "extensions").split(";");
        
        OutputManager.println(OUT_COMMON, "Initializing loader...");
        AlgorithmScriptLoader loader = new AlgorithmScriptLoader(extensionsPath);
        AlgorithmLayer mainLayer = loader.createLayer();
        
        OutputManager.println(OUT_COMMON, "Loading launcher core...");
        loadLauncherCore(loader, mainLayer);
        
        OutputManager.println(OUT_COMMON, "Loading launcher extensions...");
        loadLauncherExtensions(loader, mainLayer);
        
        OutputManager.println(OUT_COMMON, "Launching console...");
        
        Algorithm launcher = Algorithm.create(mainLayer.node(LauncherIds.MAIN_START).buildAs(Step.class));        
        launcher.start();
    }
    
    /*private static String getBundleMainScript(BundleInfo bundle) throws LauncherException
    {
        Attributes attrs = bundle.getManifestData();
        String isLauncherExt = attrs.getValue("Launcher-Extension");
        
        if(isLauncherExt != null && Boolean.parseBoolean(isLauncherExt))
        {
            String mainScript = attrs.getValue("Launcher-Extension-Main-Script");
            
            if(mainScript == null)
                throw new LauncherException("The launcher extension '" + bundle.getTitle() + "' at '" + bundle.getPackage() + "' does not define the main script entry");
            
            return mainScript;
        }
        
        return null;
    }*/
    
    private static void loadLauncherCore(AlgorithmLoader loader, GraphHandler mainLayer) throws AlgorithmLoadException, LauncherException
    {
        loader.
            load("hmod.domains.launcher.core.ac.MainDataScript", mainLayer).
            load("hmod.domains.launcher.core.ac.ConfigScript", mainLayer).
            load("hmod.domains.launcher.core.ac.MainScript", mainLayer);
    }
    
    private static void loadLauncherExtensions(AlgorithmScriptLoader loader, GraphHandler mainLayer) throws AlgorithmLoadException, LauncherException
    {
        loader.
            load("hmod.domains.launcher.core.ac.CoreExtensionScript", mainLayer).
            load("hmod.domains.launcher.graph.ac.GraphExtensionScript", mainLayer);
        
        ClassLoader cl = loader.getClassLoader();
        
        Reflections refs = new Reflections(new ConfigurationBuilder().
            setUrls(ClasspathHelper.forPackage("hmod.launcher.graph", cl)).
            setScanners(new TypeAnnotationsScanner())
        );
        
        Set<Class<?>> extensionsScripts = refs.getTypesAnnotatedWith(LauncherExtensionLoader.class);
        
        for(Class scriptType : extensionsScripts)
        {
            if(!AlgorithmScript.class.isAssignableFrom(scriptType))
                continue;
            
            loader.load(scriptType.getName(), mainLayer);
        }
    }
}