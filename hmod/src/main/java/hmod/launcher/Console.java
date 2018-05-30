
package hmod.launcher;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
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
    
    @LoadsComponent({
        LauncherHandler.class,
        BundleHandler.class,
        VariableHandler.class,
        CommandRegistry.class,
        TextProcessorRegistry.class,
        CommandParseHandler.class,
        ScriptEngineHandler.class,
        WindowsHandler.class,
        RandomHandler.class,
        RunnerRegistry.class,
        InterfaceRegistry.class,
        OutputConfigHandler.class
    })
    public static void loadData(ComponentRegister cr, ParameterRegister pr)
    {
        OutputManager.println(Launcher.OUT_COMMON, "Initializing hMod launcher components...");
        
        LauncherHandler lh = cr.provide(new LauncherHandler(
            pr.getRequiredValue(DEBUG),
            pr.getRequiredValue(THREADING),
            pr.getRequiredValue(SCRIPTS_PATH),
            pr.getRequiredValue(OUTPUT_PATH)
        ));
        
        BundleHandler bh = cr.provide(new BundleHandler(pr.getRequiredValue(BUNDLES_PATHS)));
        VariableHandler vh = cr.provide(new VariableHandler());
        CommandHandler ch = cr.provide(new CommandHandler(), CommandRegistry.class);
        TextProcessorHandler tph = cr.provide(new TextProcessorHandler(), TextProcessorRegistry.class);
        CommandParseHandler cph = cr.provide(new CommandParseHandler(ch, vh, tph));
        cr.provide(new ScriptEngineHandler(bh, lh, cph, vh));
        cr.provide(new WindowsHandler(pr.getRequiredValue(WINDOWS_AUTO_CLOSE)));
        RandomHandler randH = cr.provide(new RandomHandler());
        RunnerHandler rh = cr.provide(new RunnerHandler(pr.getRequiredValue(RUNNER_ID)), RunnerRegistry.class);
        InterfaceHandler ih = cr.provide(new InterfaceHandler(pr.getRequiredValue(INTERFACE_ID)), InterfaceRegistry.class);
        OutputConfigHandler och = cr.provide(new OutputConfigHandler());
        
        ch.addCommands(
            new HelpCommand(ch),
            new RunCommand(och, ih, rh, randH),
            new SetRunnerCommand(rh),
            new SetInterfaceCommand(ih)
        );
    }
    
    @LoadsComponent(Console.class)
    public static void loadMainStart(ComponentRegister cr, 
                                     ParameterRegister pr,
                                     LauncherHandler lh,
                                     RandomHandler rh,
                                     CommandParseHandler cph)
    {
        cr.provide(new Console(lh, rh, cph, pr.getValue(INPUT_SCRIPT)));
    }
    
    @LoadsComponent
    public static void loadMainPlugin(OutputConfigHandler och,
                                      LauncherHandler lh,
                                      ScriptEngineHandler seh,
                                      CommandRegistry ch,
                                      BundleHandler bh,
                                      VariableHandler vh,
                                      WindowsHandler wh,
                                      RunnerRegistry rr,
                                      InterfaceRegistry ir,
                                      RandomHandler rh,
                                      TextProcessorRegistry tpr)
    {
        /**********************************************************************
         * SECTION I. Commands' definitions
         **********************************************************************/
        
        ch.addCommands(
            new AddOutputCommand(och),
            new AddFileOutputCommand(och),
            new ClearOutputsCommand(och),
            new RunScriptCommand(lh, seh),
            new ExitCommand(lh),
            new ReloadBundlesCommand(bh),
            new DebugCommand(lh),
            new EchoCommand(),
            new SetCommand(vh),
            new ListVarsCommand(vh),
            new ClearVarsCommand(vh),
            new WindowsConfigCommand(wh),
            new ThreadingCommand(lh),
            new RandomSeedCommand(rh),
            new RequireSetCommand(vh),
            new CreatePropertiesCommand(vh),
            new SetPropertiesCommand()
        );
        
        /**********************************************************************
         * SECTION II. Variable processors' definitions
         **********************************************************************/
        
        tpr.addProcessors(
            new DateComponentProcessor(),
            new DefaultFoldersProcessor(lh),
            new RandomProcessor(rh)
        );
        
        /**********************************************************************
         * SECTION III. Runner factories' definitions
         **********************************************************************/
        
        rr.addFactory(new DefaultAlgorithmRunnerFactory());
        
        /**********************************************************************
         * SECTION IV. Interface factories' definitions
         **********************************************************************/
        
        ir.addFactories(
            new DefaultAlgorithmInterfaceFactory(),
            new WindowedAlgorithmInterfaceFactory(lh, wh)
        );
    }
    
    /*private void loadPlugins() throws LauncherException
    {
        CorePluginLoader.load(this);
        
        HModConfig config = HModConfig.getInstance();
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

    private Console(LauncherHandler lh,
                    RandomHandler rh,
                    CommandParseHandler cph,
                    String is)
    {
        this.main = is == null ? getConsoleStart(lh, rh, cph) : getScriptStart(is, lh, rh, cph);
    }
    
    private static Statement getConsoleStart(LauncherHandler lh,
                                             RandomHandler rh,
                                             CommandParseHandler cph)
    {
        LauncherProcesses lp = new LauncherProcesses(lh, rh, cph);
        
        return block(
            () -> OutputManager.println(Launcher.OUT_COMMON, "Launching console..."),
            lp::initLauncher,
            lp::showInterface,
            repeat(
                lp::readConsoleCommand
            ).until(NOT(lh::isRunning)),
            lp::finishLauncher
        );
    }
    
    private static Statement getScriptStart(String bf,
                                            LauncherHandler lh,
                                            RandomHandler rh,
                                            CommandParseHandler cph)
    {
        LauncherProcesses lp = new LauncherProcesses(lh, rh, cph);
        String cmdString = "runScript " + bf;

        return block(
            () -> OutputManager.println(Launcher.OUT_COMMON, "Running launcher script..."),
            lp::initLauncher,
            lh::enableDebugging,
            lp.readScriptCommand("setInterface default"),
            lp.readScriptCommand(cmdString)
        );
    }

    @Override
    public void run()
    {
        main.run();
    }
}