
package hmod.solvers.hh.models.basicops;

import hmod.core.Statement;

/**
 *
 * @author Enrique Urra C.
 */
class HeuristicPermutationHandler
{
    private Statement[] permutation;

    public HeuristicPermutationHandler()
    {
    }
    
    public void storePermutation(Statement[] permutation)
    {
        this.permutation = permutation;
    }

    public int getSizeOfPermutation()
    {
        if(permutation == null)
            return 0;
        
        return permutation.length;
    }

    public Statement getHeuristicFromPermutation(int pos)
    {
        if(pos < 0 || pos >= permutation.length)
            throw new ArrayIndexOutOfBoundsException(pos);
        
        return permutation[pos];
    }

    public void deletePermutation()
    {
        permutation = null;
    }

    public boolean isPermutationDeleted()
    {
        return permutation == null;
    }
}
