
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWFactory
{
    private static DARPTWFactory instance;
    
    public static DARPTWFactory getInstance()
    {
        if(instance == null)
            instance = new DARPTWFactory();
        
        return instance;
    }
    
    private final ProblemInstanceParser parser;
    
    private DARPTWFactory()
    {
        parser = new ProblemInstanceParser();
    }
    
    public ProblemInstance createProblemInstance(String file) throws DARPTWException
    {
        if(file == null)
            throw new NullPointerException("The file path provided cannot be null");
        
        return parser.parse(file);
    }

    public Evaluator createEvaluator(ProblemInstance problemInstance) throws DARPTWException
    {
        return new Evaluator(problemInstance);
    }

    public Route createRoute(ProblemInstance problemInstance) throws DARPTWException
    {
        return new Route(problemInstance);
    }
}
