
package hmod.domains.binary;

import static hmod.core.FlowchartFactory.run;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Enrique Urra C.
 */
public class BinaryDomainTest
{
    public BinaryDomainTest()
    {
    }
    
    @BeforeClass
    public static void init()
    {
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
                addSystemOutputId("binExample-out-info").
                addSystemOutputId("binExample-result-info")
        ); 
    }
    
    @Test
    public void doTest()
    {
        BinaryDomain bd = new ModuleLoader().
            load(BinaryDomain.class).
            getInstance(BinaryDomain.class);
        
        run(
            bd.initSolution(),
            bd.printBestSolution(),
            
            bd.heuristic(BinaryDomain.FLIP_ALL),
            bd.printBestSolution(),
            
            bd.heuristic(BinaryDomain.RANDOM_MUTATE),
            bd.printBestSolution(),
            
            bd.heuristic(BinaryDomain.RANDOMIZE),
            bd.printBestSolution()
        );
    }
}
