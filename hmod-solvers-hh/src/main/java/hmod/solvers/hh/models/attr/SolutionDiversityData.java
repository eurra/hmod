
package hmod.solvers.hh.models.attr;

/**
 *
 * @author Enrique Urra C.
 */
public class SolutionDiversityData<T extends HHAttributiveSolution> implements Comparable<SolutionDiversityData>
{
    private final T solution;
    private double diversityRatio;

    SolutionDiversityData(T solution, double diversityRatio)
    {
        this.solution = solution;
        this.diversityRatio = diversityRatio;
    }

    public T getSolution()
    {
        return solution;
    }

    public double getDiversityRatio()
    {
        return diversityRatio;
    }

    void setDiversityRatio(double diversityRatio)
    {
        this.diversityRatio = diversityRatio;
    }

    @Override
    public int compareTo(SolutionDiversityData o)
    {
        if(this.diversityRatio < o.diversityRatio)
            return -1;
        else if(this.diversityRatio > o.diversityRatio)
            return 1;
        else
            return 0;
    }
}
