
package hmod.domains.binary;

import hmod.core.AlgorithmException;
import hmod.core.Statement;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class BinaryOperators
{
    public interface Converter
    {
        void convert(BinarySolution toConvert) throws AlgorithmException;
    }
    
    public static void flipAllConverter(BinarySolution toConvert)    
    {
        int length = toConvert.getLength();
        
        for(int i = 0; i < length; i++)
        {
            boolean currVal = toConvert.getVal(i);
            toConvert.setVal(i, !currVal);
        }
    }
    
    public static void randomMutateConverter(BinarySolution toConvert)
    {
        int length = toConvert.getLength();
        int randIndex = RandomTool.getInt(length);
        boolean currVal = toConvert.getVal(randIndex);
        toConvert.setVal(randIndex, !currVal);
    }
    
    public static void randomizeConverter(BinarySolution toConvert)
    {
        toConvert.randomize();
    }
    
    private final BinarySolutionHandler solutionHandler;

    BinaryOperators(BinarySolutionHandler solutionHandler)
    {
        this.solutionHandler = solutionHandler;
    }
    
    public void initSolution()
    {
        BinarySolution newSol = new BinarySolution(100, true);
        solutionHandler.setSolution(newSol);
    }
    
    public void printBestSolution()
    {
        OutputManager.println(OutputIds.RESULT_INFO, "Best solution: " + solutionHandler.getBestSolution());
    }
    
    public Statement convertSolution(Converter converter)
    {
        return () -> {
            BinarySolution currSol = solutionHandler.getSolution();
            BinarySolution newSol = new BinarySolution(currSol);
            converter.convert(newSol);
            OutputManager.println(OutputIds.OUT_INFO, "Generated solution: " + newSol);
            solutionHandler.setSolution(newSol);
        };
    }
}
