
package hmod.solvers.hh.models.adapter;

/**
 *
 * @author Enrique Urra C.
 */
class AdapterSolutionHandler<T> 
{
    private T decodedSolution;
    private T toEncondeSolution;

    AdapterSolutionHandler()
    {
    }
    
    public void storeDecodedSolution(T solution)
    {
        decodedSolution = solution;
    }

    public void storeSolutionToEncode(T solution)
    {
        toEncondeSolution = solution;
    }

    public T retrieveDecodedSolution()
    {
        return decodedSolution;
    }

    public T retrieveSolutionToEncode()
    {
        return toEncondeSolution;
    }
}
