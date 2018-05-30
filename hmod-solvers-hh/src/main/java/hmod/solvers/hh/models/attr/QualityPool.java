
package hmod.solvers.hh.models.attr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Enrique Urra C.
 */
public class QualityPool<T extends HHAttributiveSolution> implements SolutionPool<T>, Iterable<T>
{
    private int size;
    private final LinkedList<T> solList;

    public QualityPool(int initSize)
    {
        this.size = initSize;
        this.solList = new LinkedList<>();
    }

    public int getSize()
    {
        return size;
    }
    
    public boolean isFull()
    {
        return getCurrentCount() >= getSize();
    }
    
    public void resize(int newSize)
    {
        if(newSize < 0)
            throw new IllegalArgumentException();
        
        if(newSize == size)
            return;
        
        size = newSize;
        
        while(solList.size() > size)
            solList.removeLast();
    }
    
    public boolean tryAdd(T solution)
    {
        int currElites = solList.size();
        
        // If the current elite list is empty, the solution is always added
        if(currElites == 0)
        {
            solList.add(solution);
            return true;
        } 
        // If the list is full, we add the element only if the candidate is 
        // better than the worser solution in the elite list, which will remove
        // the last element
        else if(currElites >= size)
        {
            T worstBestSolution = solList.getLast();

            if(worstBestSolution.compareTo(solution) > 0)
                return false;
        }
        
        // Otherwise, the list is updated.
        // We check the list to find a proper position in which the solution
        // will be added            
        ListIterator<T> iterator = solList.listIterator(currElites);

        while(iterator.hasPrevious())
        {
            T indexSolution = iterator.previous();
            int compResult = indexSolution.compareTo(solution);

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
        iterator.add(solution);
        
        // If the max size is exceeded, we remove the worser element
        if(solList.size() >= size)
            solList.removeLast();
        
        return true;
    }
    
    public T getSolutionAt(double perc) throws NoSuchElementException
    {
        if(solList.isEmpty())
            throw new NoSuchElementException();
        
        if(solList.size() == 1)
            return solList.getFirst();
        
        int range = Math.max(1, (int)(solList.size() * perc));
        return getSolutionAt(range);
    }
    
    public T getWorser() throws NoSuchElementException
    {
        if(solList.isEmpty())
            throw new NoSuchElementException();
        
        return solList.getLast();
    }
    
    public T getBest() throws NoSuchElementException
    {
        if(solList.isEmpty())
            throw new NoSuchElementException();
        
        return solList.getFirst();
    }

    @Override
    public int getCurrentCount()
    {
        return solList.size();
    }

    @Override
    public T getSolutionAt(int pos) throws IndexOutOfBoundsException
    {
        return solList.get(pos);
    }

    @Override
    public Iterator<T> iterator()
    {
        return solList.iterator();
    }
}
