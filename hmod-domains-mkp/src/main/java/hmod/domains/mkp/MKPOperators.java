
package hmod.domains.mkp;

import hmod.core.AlgorithmException;
import hmod.core.Condition;
import hmod.core.Statement;
import hmod.solvers.common.MutableIterationHandler;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPOperators
{
    public static Statement selectRandomItemInList(ItemListHandler itemListHandler, SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            int count = itemListHandler.getItemCount();
            Item selected = itemListHandler.getItemAt(RandomTool.getInt(count));
            selectedItemHandler.selectItem(selected);
        }; 
    }
    
    public static Statement selectMostProfitableItemInList(ItemListHandler itemListHandler, SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            int itemCount = itemListHandler.getItemCount();
            Item selected = null;

            for(int i = 0; i < itemCount; i++)
            {
                Item currItem = itemListHandler.getItemAt(i);

                if(selected == null || selected.getProfit() < currItem.getProfit())
                    selected = currItem;
            }

            selectedItemHandler.selectItem(selected);
        };
    }
    
    public static Statement selectWorstProfitableItemInList(ItemListHandler itemListHandler, SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            int itemCount = itemListHandler.getItemCount();
            Item selected = null;

            for(int i = 0; i < itemCount; i++)
            {
                Item currItem = itemListHandler.getItemAt(i);

                if(selected == null || selected.getProfit() > currItem.getProfit())
                    selected = currItem;
            }

            selectedItemHandler.selectItem(selected);
        };
    }
    
    public static Statement removeSelectedItemFromList(SelectedItemHandler selectedItemHandler, ItemListHandler itemListHandler)
    {
        return () -> {
            Item item = selectedItemHandler.getSelected();
            itemListHandler.removeItem(item);
        };
    }
    
    private final MutableSolutionHandler solutionHandler;
    private final SolutionBuilderHandler solutionBuilderHandler;

    MKPOperators(MutableSolutionHandler solutionHandler, SolutionBuilderHandler solutionBuilderHandler)
    {
        this.solutionHandler = solutionHandler;
        this.solutionBuilderHandler = solutionBuilderHandler;
    }
    
    public void saveBuildedSolutionForRetrieving() throws AlgorithmException
    {
        if(solutionBuilderHandler.getIncludedCount() == 0)
            throw new AlgorithmException("Cannot build an empty solution!");
        
        MKPSolution solution = solutionBuilderHandler.build();
        solutionHandler.setSolutionForRetrieving(solution);
    }
    
    public void loadProvidedSolutionInBuilder()
    {
        MKPSolution solution = solutionHandler.getProvidedSolution();
        solutionBuilderHandler.importSolution(solution);
    }
    
    public Statement storeAvailableItemsInList(ItemListHandler itemListHandler)
    {
        return () -> {
            Item[] itemsArray = solutionBuilderHandler.getAvailableItems();

            if(itemsArray.length == 0)
                throw new IllegalStateException("No available items exist for storing");

            for(int i = 0; i < itemsArray.length; i++)
                itemListHandler.addItem(itemsArray[i]);
        };
    }
    
    public Statement storeCurrentItemsInList(ItemListHandler itemListHandler)
    {
        return () -> {
            Item[] itemsArray = solutionBuilderHandler.getIncludedItems();

            for(int i = 0; i < itemsArray.length; i++)
                itemListHandler.addItem(itemsArray[i]);
        };
    }
    
    public Statement selectRandomAvailableItemInBuild(SelectedItemHandler selectedItemHandler) throws AlgorithmException
    {
        return () -> {
            Item[] available = solutionBuilderHandler.getAvailableItems();

            if(available.length == 0)
                throw new AlgorithmException("No available items in build");

            Item selected = available[RandomTool.getInt(available.length)];
            selectedItemHandler.selectItem(selected);
        };
    }
    
    public Statement selectRandomIncludedItemInBuild(SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            Item[] included = solutionBuilderHandler.getIncludedItems();

            if(included.length == 0)
                throw new AlgorithmException("No included items in build");

            Item selected = included[RandomTool.getInt(included.length)];
            selectedItemHandler.selectItem(selected);
        };
    }
    
    public Statement addSelectedItemToBuild(SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            Item selected = selectedItemHandler.getSelected();
            solutionBuilderHandler.includeItem(selected);
        };
    }
    
    public Statement removeSelectedItemFromBuild(SelectedItemHandler selectedItemHandler)
    {
        return () -> {
            Item toRemove = selectedItemHandler.getSelected();
            solutionBuilderHandler.excludeItem(toRemove);
        };  
    }
    
    public Statement initRandomIteratorFromCurrentItems(MutableIterationHandler iterationHandler, double removePerc, boolean random) throws AlgorithmException
    {
        return () -> {
            int currentCount = solutionBuilderHandler.getIncludedCount();

            if(currentCount == 0)
                throw new AlgorithmException("No included items for initializing an iterator");

            int toRemoveCount;
            
            if(random)
                toRemoveCount = Math.max(1, RandomTool.getInt(Math.max(1, (int) (currentCount * removePerc))));
            else
                toRemoveCount = Math.max(1, (int) (currentCount * removePerc));
            
            iterationHandler.setMaxIterations(toRemoveCount);
        };
    }
    
    public Condition checkFeasibleAdd(SelectedItemHandler itemHandler)
    {
        return () -> {
            Item item = itemHandler.getSelected();
            return solutionBuilderHandler.isIncludeFeasible(item);
        };            
    }
    
    public void reportSolution()
    {
        MKPSolution finalSolution = solutionHandler.getBestSolution();
        OutputManager.println(MKPOutputIds.FINAL_SOLUTION_INFO, "***********************\n\n" + finalSolution + "\n");
    }
}
