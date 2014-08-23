
package hmod.domains.launcher.core.ac;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.BuilderGraph;
import hmod.core.Step;
import static hmod.loader.graph.AlgorithmBuilders.algorithmBlock;

/**
 *
 * @author Enrique Urra C.
 */
class MainLoader
{    
    public static NestedBuilder<Step> load(BuilderGraph graph) throws BuildException
    {
        NestedBuilder launcherData = graph.loadNode(LauncherIds.LAUNCHER_DATA);
        NestedBuilder randomData = graph.loadNode(LauncherIds.RANDOM_DATA);
        NestedBuilder cmdParserData = graph.loadNode(LauncherIds.COMMANDS_PARSER_DATA);
        LauncherOperatorFactory launcherOps = new LauncherOperatorFactory();
        
        return algorithmBlock().
            run(launcherOps::initLauncher, launcherData, randomData).
            repeat().
                If(launcherOps::readNextCommand, cmdParserData).
                    run(launcherOps::executeCurrentCommand, launcherData, cmdParserData).
                end().
            until(launcherOps::checkLauncherFinished, launcherData).
            run(launcherOps::finishLauncher);
    }
}
