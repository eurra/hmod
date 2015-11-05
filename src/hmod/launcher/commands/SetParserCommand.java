
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import hmod.parser.AlgorithmParserException;
import hmod.parser.AlgorithmParserFactory;
import hmod.parser.AlgorithmParserModuleInfo;
import optefx.util.output.OutputManager;

/**
 * Implements the 'set_interface' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="set_parser",
    usage="set_parser <num>",
    description="Configures the parser to use in algorithm loading.\n"
        + "<num> The num of the parser to set. Run this command without this"
        + " argument to see all the available numbers."
)
public class SetParserCommand extends Command
{
    private LauncherData launcherData;

    public void setLauncherData(LauncherData data)
    {
        this.launcherData = data;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        Class[] factoryTypes = launcherData.getParserFactoryTypes();
        
        if(args.length < 1)
        {
            for(int i = 0; i < factoryTypes.length; i++)
            {
                try
                {
                    AlgorithmParserModuleInfo info = AlgorithmParserFactory.getModuleInfo(factoryTypes[i]);
                    
                    OutputManager.println(Launcher.OUT_COMMON, (i + 1) + ") '" + info.name() + "' (" + factoryTypes[i].getName() + "):");
                    OutputManager.println(Launcher.OUT_COMMON, info.description());
                    
                    if(i == factoryTypes.length - 1)
                        OutputManager.println(Launcher.OUT_COMMON);
                }
                catch(AlgorithmParserException ex)
                {
                    throw new LauncherException(ex);
                }
            }
        }
        else
        {
            int num = -1;
            
            try
            {
                num = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException ex)
            {
                throw new LauncherException("Invalid parser number (" + num + ")");
            }
            
            num--;
            
            if(num < 0 || num >= factoryTypes.length)
                throw new LauncherException("Such parser number is not registered");
            try
            {
                launcherData.setParser(AlgorithmParserFactory.getFactory(factoryTypes[num]).getParser(Launcher.PARSER_NAME));
            }
            catch(AlgorithmParserException ex)
            {
                throw new LauncherException(ex);
            }
            
            OutputManager.println(Launcher.OUT_COMMON, "Parser selected (" + factoryTypes[num].getName() + ").");
        }
    }
}