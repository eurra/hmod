
package hmod.launcher.components;

import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.parser.AlgorithmParserException;
import hmod.launcher.LauncherException;
import hmod.launcher.Launcher;
import hmod.parser.AlgorithmParserFactory;
import optefx.util.output.BasicManagerType;
import optefx.util.output.DefaultOutputConfigBuilder;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public class StartLauncher implements Operator
{
    private LauncherData launcherData;
    private RunData runData;
    private RandomData randomData;
    private RunPlugins runPluginsOperator = new RunPlugins();

    public void setLauncherData(LauncherData launcherData)
    {
        this.launcherData = launcherData;
    }
    
    public void setRunData(RunData data)
    {
        this.runData = data;
    }

    public void setRandomData(RandomData data)
    {
        this.randomData = data;
    }
    
    public void setPluginData(PluginData data)
    {
        this.runPluginsOperator.setPluginData(data);
    }
    
    @Override
    public void execute() throws AlgorithmException
    {
        try
        {
            launcherData.setParserFactoryTypes(AlgorithmParserFactory.getFactoryTypes());
            launcherData.setParser(AlgorithmParserFactory.getDefaultFactory().getParser(Launcher.PARSER_NAME));
        }
        catch(AlgorithmParserException ex)
        {
            throw new LauncherException("Cannot initialize the algorithm parsers");
        }
        
        launcherData.setRunning(true);
        launcherData.setOutputConfigBuilder(new DefaultOutputConfigBuilder());

        if(runData.getThreading())
        {
            RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
            OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
        }
        else
        {
            RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
            OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
        }
        
        randomData.setSeed(RandomTool.getInstance().createRandomSeed());
        
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON, "**** hMod launcher console ***");
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON);
        
        // On start launcher-plugins are executed
        runPluginsOperator.execute();
        
        OutputManager.println(Launcher.OUT_COMMON, "\nType 'help' for view available commands");
    }
}