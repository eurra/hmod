
package hmod.launcher;

import flexbuilders.scripting.BuildScriptLibrary;
import flexbuilders.scripting.ScriptBundleInfo;
import flexbuilders.scripting.ScriptLibraryException;
import hmod.parser.AlgorithmParser;
import hmod.parser.AlgorithmParserException;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.parser.AlgorithmParserFactory;
import hmod.parser.builders.TreeBuilderParser;
import hmod.parser.builders.TreeBuilderParserFactory;
import hmod.core.Config;
import optefx.util.output.OutputManager;
import java.util.jar.Attributes;
import optefx.util.output.DefaultOutputConfigBuilder;

public class Launcher
{
    public static final String OUT_COMMON = "launcher-output-common";
    public static final String OUT_ERROR = "launcher-output-error";
    
    public static final String PARSER_NAME = "launcher-parser-name";
    public static final String EXTENSIONS_ENTRY = "hmod.launcher.extensions.path";
    
    public static void main(String[] args) throws AlgorithmException, AlgorithmParserException, LauncherException
    {
        OutputManager.getCurrent().setOutputsFromConfig(
            new DefaultOutputConfigBuilder().addSystemOutput(OUT_COMMON).makePermanent(OUT_COMMON).
                addSystemErrorOutput(OUT_ERROR).makePermanent(OUT_ERROR).
                build()
        );
        
        OutputManager.println(OUT_COMMON, "Initializing parser...");
        TreeBuilderParser parser = AlgorithmParserFactory.getFactory(TreeBuilderParserFactory.class).getParser(PARSER_NAME);
        
        OutputManager.println(OUT_COMMON, "Loading launcher core...");
        loadLauncherCore(parser);
        
        OutputManager.println(OUT_COMMON, "Loading launcher extensions...");
        loadLauncherExtensions(parser);
        
        OutputManager.println(OUT_COMMON, "Launching console...");
        Algorithm launcher = parser.getAlgorithm();
        parser.restart();
        launcher.start();
    }
    
    private static String getBundleMainScript(ScriptBundleInfo bundle) throws LauncherException
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
    }
    
    private static void loadLauncherCore(AlgorithmParser parser) throws AlgorithmParserException, LauncherException
    {
        parser.load("hmod.launcher.scripts.MainScript");
        parser.load("hmod.launcher.scripts.MainDataScript");
        parser.load("hmod.launcher.scripts.ConfigScript");
        parser.load("hmod.parser.builders.SetStartIdScript", "consoleLauncher_main");
        BuildScriptLibrary classPathLib;
        
        try
        {
            classPathLib = BuildScriptLibrary.getFromClassLoader(Launcher.class.getClassLoader());
        }
        catch(ScriptLibraryException ex)
        {
            throw new LauncherException(ex);
        }
        
        ScriptBundleInfo[] bundles = classPathLib.getBundles();
        
        for(int i = 0; i < bundles.length; i++)
        {
            ScriptBundleInfo bundle = bundles[i];
            String mainScript = getBundleMainScript(bundle);
            
            if(mainScript != null)
                parser.load(mainScript); 
        }
    }
    
    private static void loadLauncherExtensions(AlgorithmParser parser) throws AlgorithmParserException, LauncherException
    {
        Config config = Config.getInstance();
        String scriptsFolder = config.getEntry(EXTENSIONS_ENTRY);

        if(scriptsFolder == null)
            throw new LauncherException("The entry '" + EXTENSIONS_ENTRY + "' was not found in the 'hmod.properties' file");

        BuildScriptLibrary extsLib;

        try
        {
            extsLib = BuildScriptLibrary.getFromPaths(false, scriptsFolder);
        }
        catch(ScriptLibraryException ex)
        {
            throw new LauncherException("Cannot load the launcher extensions' path (" + scriptsFolder + "): " + ex.getLocalizedMessage(), ex);
        }
        
        ScriptBundleInfo[] bundles = extsLib.getBundles();
        
        for(int i = 0; i < bundles.length; i++)
        {
            ScriptBundleInfo bundle = bundles[i];
            String mainScript = getBundleMainScript(bundle);
            
            if(mainScript != null)
                parser.load(mainScript); 
        }
    }
}