
package hmod.solvers.hh.models.attr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public class AttributivePool<T extends HHAttributiveSolution> implements SolutionPool<T>, Iterable<T>
{
    private final int maxSolutions;
    private final LinkedList<T> solList;

    public AttributivePool(int maxSolutions) throws IllegalArgumentException
    {
        if(maxSolutions <= 0)
            throw new IllegalArgumentException("Max. solutions must be greather than 0");
        
        this.maxSolutions = maxSolutions;
        this.solList = new LinkedList<>();
    }
    
    public AttributesSnapshot getAttributesSnapshot()
    {
        return new AttributesSnapshot(solList);
    }
    
    @Override
    public int getCurrentCount()
    {
        return solList.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return solList.iterator();
    }

    public int getMaxSolutions()
    {
        return maxSolutions;
    }
    
    public boolean isFull()
    {
        return getCurrentCount() >= getMaxSolutions();
    }
    
    public double getAverageEvaluationWithin(double perc)
    {
        double sum = 0.0;
        int count = 0;
        int total = (int)(solList.size() * perc);
        Iterator<T> it = solList.iterator();
        
        while(it.hasNext() && count < total)
        {
            double curr = it.next().getEvaluation();
            sum += curr;
            count++;
        }
        
        return sum / total;
    }
    
    @Override
    public T getSolutionAt(int pos) throws IndexOutOfBoundsException
    {
        return solList.get(pos);
    }
    
    public boolean tryReplaceWithQuality(T candidate)
    {
        // If the list is full, we add the element only if the candidate is 
        // better than the worser solution in the elite list
        if(solList.size() >= maxSolutions)
        {
            T worstBestSolution = solList.getLast();

            if(worstBestSolution.compareTo(candidate) > 0)
                return false;
        }
        
        if(tryAdd(candidate) && solList.size() > maxSolutions)
        {
            purgeWorst();
            return true;
        }
        
        return false;
    }
    
    public void purgeWorst()
    {
        if(solList.size() >= maxSolutions)
            solList.removeLast();
    }
    
    public void purgeWorserIn(double perc)
    {
        int size = solList.size();
        
        if(size >= maxSolutions)
        {
            int range = Math.max((int)(size * perc), 1);
            int pos = RandomTool.getInt(range);
            solList.remove((size - range) + pos);
        }
    }
    
    private boolean tryAdd(T candidate)
    {
        int currElites = solList.size();
        
        // If the current elite list is empty, the solution is always added
        if(currElites == 0)
        {
            solList.add(candidate);
            return true;
        } 
        
        // Otherwise, the list is updated.
        // We check the list to find a proper position in which the solution
        // will be added            
        ListIterator<T> iterator = solList.listIterator(currElites);

        while(iterator.hasPrevious())
        {
            T indexSolution = iterator.previous();
            int compResult = indexSolution.compareTo(candidate);

            // If while searching the list, we found an identical 
            // solution to the one to be added, the process is cancelled
            if(compResult == 0)
            {
                return false;
            }
            else if(compResult > 0)
            {
                iterator.next();
                break;
            }
        }

        // At this point, we have found the position in which the 
        // new elite solution must be added
        iterator.add(candidate);
        return true;
    }
}
