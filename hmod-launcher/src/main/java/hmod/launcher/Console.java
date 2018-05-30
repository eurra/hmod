
package hmod.launcher;

import hmod.core.AlgorithmException;
import static hmod.core.AlgorithmFactory.*;
import hmod.core.Context;
import hmod.core.MethodBridges;
import static hmod.core.MethodBridges.eval;
import static hmod.core.MethodBridges.set;
import hmod.core.Procedure;
import hmod.core.SimpleVar;
import hmod.core.Statement;
import hmod.core.Var;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

public final class Console implements Statement
{    
    static final Parameter<String> SCRIPTS_PATH = new Parameter<>("Console.SCRIPTS_PATH");
    static final Parameter<String> OUTPUT_PATH = new Parameter<>("Console.OUTPUT_PATH");
    static final Parameter<Boolean> DEBUG = new Parameter<>("Console.DEBUG");  
    static final Parameter<Boolean> THREADING = new Parameter<>("Console.THREADING");  
    static final Parameter<String> RUNNER_ID = new Parameter<>("Console.RUNNER_ID"); 
    static final Parameter<String> INTERFACE_ID = new Parameter<>("Console.INTERFACE_ID"); 
    static final Parameter<Boolean> WINDOWS_AUTO_CLOSE = new Parameter<>("Console.WINDOWS_AUTO_CLOSE");
    static final Parameter<String> INPUT_SCRIPT = new Parameter<>("Console.INPUT_SCRIPT");
    static final Parameter<String[]> BUNDLES_PATHS = new Parameter<>("Console.BUNDLES_PATH");
    
    @LoadsComponent(Console.class)
    public static void load(ComponentRegister cr,
                            ParameterRegister pr)
    {
        cr.provide(new Console(pr));
    }
    
    /*private void loadPlugins() throws LauncherException
    {
        CorePluginLoader.load(this);
        
        HModConfig config = HModConfig.getFrom();
        String[] pluginsPaths = config.getEntry(PLUGINS_ENTRY, "plugins").split(";");
        URL[] urls = new URL[pluginsPaths.length];
        
        for(int i = 0; i < pluginsPaths.length; i++)
        {
            try
            {
                urls[i] = new File(pluginsPaths[i]).toURI().toURL();
            }
            catch(MalformedURLException ex)
            {
                throw new LauncherException("Cannot load the plugin path '" + pluginsPaths[i] + "'", ex);
            }
        }
        
        ClassLoader cl = URLClassLoader.newInstance(urls);
        
        Reflections refs = new Reflections(new ConfigurationBuilder().
            setUrls(ClasspathHelper.forPackage("hmod.launcher.graph", cl)).
            setScanners(new TypeAnnotationsScanner())
        );
        
        Set<Method> extensions = refs.getMethodsAnnotatedWith(LauncherPlugin.class);
        
        for(Method method : extensions)
        {
            try
            {
                method.invoke(null, this);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException ex)
            {
                throw new LauncherException("Cannot load the launcher plugin of method '" + method  + "' in class '" + method.getDeclaringClass() + "'", ex);
            }
        }
    }*/
    
    private final Statement main;
    private final LauncherControlVar lc = new LauncherControlVar("Launcher.launcherControl");
    private final LauncherInputVar li = new LauncherInputVar("Launcher.launcherInput");
    private final Var<RandomSeed> rs = new SimpleVar<>("Launcher.randomSeed");
    private final Var<CommandParser> cp = new SimpleVar<>("Launcher.commandParser");
    private final Var<VariableRegistry> vr = new SimpleVar<>("Launcher.variableRegistry");
    private final Var<OutputSettings> os = new SimpleVar<>("Launcher.outputSettings");
    private final Var<ScriptLauncher> sl = new SimpleVar<>("Launcher.scriptLauncher");
    private final Var<BundleLoader> bl = new SimpleVar<>("Launcher.bundleLoader");
    private final Var<WindowsSettings> ws = new SimpleVar<>("Launcher.windowsSettings");
    private final CommandRegistryVar cr = new CommandRegistryVar("Launcher.commandRegistry");
    private final TextProcessorRegistryVar tpr = new TextProcessorRegistryVar("Launcher.textProcessorRegistry");
    private final InterfaceRegistryVar ir = new InterfaceRegistryVar("Launcher.interfaceRegistry");
    private final RunnerRegistryVar rr = new RunnerRegistryVar("Launcher.runnerRegistry");

    private Console(ParameterRegister pr)
    {
        String is = pr.getValue(INPUT_SCRIPT);
        
        main = block("Launcher.main",
            initData(pr),
            initMainCommands(),
            initAdditionalCommands(),
            initTextProcessors(),
            initRunnerFactories(),
            initInterfaceFactories(),
            is == null ? getConsoleStart() : getScriptStart(is)
        );
    }
    
    private Statement initData(ParameterRegister pr)
    {
        return block("Launcher.initData",
            lc.setNew(
                refOf(pr.getRequiredValue(DEBUG)),
                refOf(pr.getRequiredValue(THREADING)),
                refOf(pr.getRequiredValue(SCRIPTS_PATH)), 
                refOf(pr.getRequiredValue(OUTPUT_PATH))
            ),
            set(rs, RandomSeed::new),
            set(vr, VariableRegistry::new),
            tpr.setNew(),
            cr.setNew(),
            set(cp, CommandParser::new, cr, vr, tpr),
            li.setNew(lc, cp, cr),
            set(os, OutputSettings::new),
            set(bl, BundleLoader::new, refOf(pr.getRequiredValue(BUNDLES_PATHS))),
            set(sl, ScriptLauncher::new, bl, lc, cp, vr),
            ir.setNew(refOf(pr.getRequiredValue(INTERFACE_ID))),
            rr.setNew(refOf(pr.getRequiredValue(RUNNER_ID))),
            set(ws, WindowsSettings::new, refOf(pr.getRequiredValue(WINDOWS_AUTO_CLOSE)))
        );
    }
    
    private Statement initAdditionalCommands()
    {
        return block("Launcher.initAdditionalCommands",
            cr.addCommands(
                eval(AddOutputCommand::new, os),
                eval(AddFileOutputCommand::new, os),
                eval(ClearOutputsCommand::new, os),
                eval(RunScriptCommand::new, lc, sl),
                eval(ExitCommand::new, lc),
                eval(ReloadBundlesCommand::new, bl),
                eval(DebugCommand::new, lc),
                eval(EchoCommand::new),
                eval(SetCommand::new, vr),
                eval(ListVarsCommand::new, vr),
                eval(ClearVarsCommand::new, vr),
                eval(WindowsConfigCommand::new, ws),
                eval(ThreadingCommand::new, lc),
                eval(RandomSeedCommand::new, rs),
                eval(RequireSetCommand::new, vr),
                eval(CreatePropertiesCommand::new, vr),
                eval(SetPropertiesCommand::new)
            ).run()
        );
    }
    
    private Statement initMainCommands()
    {
        return block("Launcher.initMainCommands",
            cr.addCommands(
                eval(HelpCommand::new, cr),
                eval(RunCommand::new, os, ir, rr, rs),
                eval(SetRunnerCommand::new, rr),
                eval(SetInterfaceCommand::new, ir)
            ).run()
        );
    }
    
    private Statement initTextProcessors()
    {
        return block("Launcher.initTextProcessors",
            tpr.addProcessors(
                eval(DateComponentProcessor::new),
                eval(DefaultFoldersProcessor::new, lc),
                eval(RandomProcessor::new, rs)
            ).run()
        );
    }
    
    private Statement initRunnerFactories()
    {
        return block("Launcher.initRunnerFactories",
            rr.addFactories(
                eval(DefaultAlgorithmRunnerFactory::new)
            ).run()
        );
    }
    
    private Statement initInterfaceFactories()
    {
        return block("Launcher.initInterfaceFactories",
            ir.addFactories(
                eval(DefaultAlgorithmInterfaceFactory::new),
                eval(WindowedAlgorithmInterfaceFactory::new, lc, ws)
            ).run()
        );
    }
    
    private Statement getConsoleStart()
    {
        LauncherProcessingOps lpOps = LauncherProcessingOps.getInstance();
        
        return block("Launcher.consoleStart",
            MethodBridges.run(() -> OutputManager.println(Launcher.OUT_COMMON, "Launching console...")),
            lpOps.initLauncher(lc, rs),
            lpOps.showInterface(),
            repeat(
                li.readConsoleCommand()
            ).until(NOT(lc.isRunning())),
            lpOps.finishLauncher()
        );
    }
    
    private Statement getScriptStart(String bf)
    {
        LauncherProcessingOps lpOps = LauncherProcessingOps.getInstance();
        String cmdString = "runScript " + bf;

        return block("Launcher.scriptStart",
            MethodBridges.run(() -> OutputManager.println(Launcher.OUT_COMMON, "Running launcher script...")),
            lpOps.initLauncher(lc, rs),
            lc.enableDebugging(),
            li.readScriptCommand(refOf("setInterface default")),
            li.readScriptCommand(refOf(cmdString))
        );
    }

    @Override
    public Procedure assemble(Context ctx) throws AlgorithmException
    {
        return main.assemble(ctx);
    }
}