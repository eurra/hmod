
package hmod.solvers.hh.models.attr;

import hmod.solvers.common.IterationHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author Enrique Urra C.
 */
public class DiversityPool<T extends HHAttributiveSolution>
{
    private final LinkedList<SolutionDiversityData<T>> solutionList = new LinkedList<>();
    private final Map<Object, Integer> liveFrequencyTable = new HashMap<>();
    private final IterationHandler ih;
    private final TrackableSolutionHandler<T> tsh;
    private final int frequenciesUpdateDelay = 100;
    private Map<Object, Integer> snapshotFrequencyTable;
    private int lastFrequenciesUpdateIteration = -1000;
    private int maxSize;

    public DiversityPool(IterationHandler ih,
                         TrackableSolutionHandler<T> tsh,
                         int initialSize)
    {
        this.ih = ih;
        this.tsh = tsh;
        this.maxSize = initialSize;
    }
    
    public int getCurrentCount()
    {
        return solutionList.size();
    }

    public int getMaxSize()
    {
        return maxSize;
    }
    
    public boolean isFull()
    {
        return getCurrentCount() >= maxSize;
    }
    
    private void extractAttributesData(T target)
    {
        for(Object attr : target.getAttributesCollection())
        {
            Integer currFrequency = liveFrequencyTable.get(attr);
            
            if(currFrequency == null)
                currFrequency = 0;
            
            currFrequency++;
            liveFrequencyTable.put(attr, currFrequency);
        }
    }
    
    private void updateFrequenciesSnapshot()
    {
        this.snapshotFrequencyTable = new HashMap<>(liveFrequencyTable);
    }
    
    private boolean shouldUpdateFrequencies()
    {
        return ih.getCurrentIteration()- lastFrequenciesUpdateIteration >= frequenciesUpdateDelay;
    }
    
    private double calculateDiversityScore(T target, Map<Object, Integer> frequenciesTable)
    {
        double diversitySum = 0.0;
        
        for(Object attr : target.getAttributesCollection())
        {
            Integer currFrequency = frequenciesTable.get(attr);
            
            if(currFrequency == null)
                currFrequency = 0;
            
            double attrFrequencyRatio = lastFrequenciesUpdateIteration == 0 ? 0 : ((double) currFrequency) / lastFrequenciesUpdateIteration;
            diversitySum += (1 - attrFrequencyRatio);
        }
        
        return diversitySum / (double) target.getAttributesCollection().getAttributesCount();
    }
    
    public double calculateDiversityRatio(T solution)
    {
        return tsh.getRelativeEvaluation(solution) / (1 - calculateDiversityScore(solution, snapshotFrequencyTable));
    }
    
    private void updateCurrentSolutions()
    {
        for(SolutionDiversityData<T> entry : solutionList)
            entry.setDiversityRatio(calculateDiversityRatio(entry.getSolution()));
        
        Collections.sort(solutionList);
    }
    
    public void resize(int newSize)
    {
        if(newSize < 0)
            throw new IllegalArgumentException();
        
        maxSize = newSize;
        
        while(getCurrentCount() > maxSize)
            solutionList.removeFirst();
    }
    
    public boolean tryAdd(T solution)
    {
        extractAttributesData(solution);
        
        if(shouldUpdateFrequencies())
        {
            updateFrequenciesSnapshot();
            updateCurrentSolutions();
            lastFrequenciesUpdateIteration = ih.getCurrentIteration();
        }
        
        int size = getCurrentCount();
        SolutionDiversityData<T> newEntry = new SolutionDiversityData<>(solution, calculateDiversityRatio(solution));
        
        // If the current elite list is empty, the solution is always added
        if(size == 0)
        {
            solutionList.add(newEntry);
            return true;
        } 
        // If the list is full, we add the element only if the candidate is 
        // better than the worser solution in the list, which will remove
        // the last element
        else if(size >= maxSize)
        {
            SolutionDiversityData worst = solutionList.getFirst();

            if(worst.compareTo(newEntry) > 0)
                return false;
        }
        
        // Otherwise, the list is updated.
        // We check the list to find a proper position in which the solution
        // will be added            
        ListIterator<SolutionDiversityData<T>> iterator = solutionList.listIterator();

        while(iterator.hasNext())
        {
            SolutionDiversityData indexSolution = iterator.next();

            if(indexSolution.compareTo(newEntry) == 0)
            {
                return false;
            }            
            if(indexSolution.compareTo(newEntry) > 0)
            {
                iterator.previous();
                break;
            }
        }

        // At this point, we have found the position in which the 
        // new elite solution must be added
        iterator.add(newEntry);
        
        // If the max size is exceeded, we remove the worser element
        if(solutionList.size() >= maxSize)
            solutionList.removeFirst();
        
        return true;
    }
    
    public SolutionDiversityData<T> getSolutionAt(double perc) throws NoSuchElementException
    {
        if(solutionList.isEmpty())
            throw new NoSuchElementException();
        
        if(solutionList.size() == 1)
            return solutionList.getFirst();
        
        int range = Math.max(1, (int)(solutionList.size() * perc));
        return getSolutionAt(solutionList.size() - range);
    }
    
    public SolutionDiversityData<T> getSolutionAt(int pos) throws IndexOutOfBoundsException
    {
        return solutionList.get(pos);
    }
    
    public SolutionDiversityData<T> getWorser() throws NoSuchElementException
    {
        if(solutionList.isEmpty())
            throw new NoSuchElementException();
        
        return solutionList.getFirst();
    }
    
    public SolutionDiversityData<T> getBest() throws NoSuchElementException
    {
        if(solutionList.isEmpty())
            throw new NoSuchElementException();
        
        return solutionList.getLast();
    }
    
    public List<SolutionDiversityData<T>> getSolutionData()
    {
        return new ArrayList<>(solutionList);
    }
    
    public List<SolutionDiversityData<T>> getInvertedSolutionData()
    {
        LinkedList<SolutionDiversityData<T>> res = new LinkedList<>();
        
        for(SolutionDiversityData<T> data : solutionList)
            res.addFirst(data);
        
        return res;
    }
}
