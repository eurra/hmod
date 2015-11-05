
package hmod.launcher.components;

import hmod.core.DataInterface;
import hmod.parser.AlgorithmParser;
import optefx.util.output.DefaultOutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface LauncherData extends DataInterface
{    
    void setParserFactoryTypes(Class[] types);
    Class[] getParserFactoryTypes();
    
    void setParser(AlgorithmParser parser);
    AlgorithmParser getParser();
    
    void setRunning(boolean running);
    boolean getRunning();
    
    void setOutputConfigBuilder(DefaultOutputConfigBuilder builder);
    DefaultOutputConfigBuilder getOutputConfigBuilder();
    
    void setDebug(boolean debug);
    boolean getDebug();
    
    //void setStartId(String id);
    //String getStartId();
    
    void setBatchBaseFolder(String folder);
    String getBatchBaseFolder();
    
    void setOutputBaseFolder(String folder);
    String getOutputBaseFolder();
}