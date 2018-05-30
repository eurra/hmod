package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public class Item
{
    private final int id;
    private final int profit;

    public Item(int id, int profit)
    {
        if(id < 0)
            throw new IllegalArgumentException("Negative id");
        
        if(profit < 0)
            throw new IllegalArgumentException("Negative profit");
        
        this.id = id;
        this.profit = profit;
    }

    public int getId()
    {
        return id;
    }

    public int getProfit()
    {
        return profit;
    }

    @Override
    public String toString()
    {
        return "ITEM id=" + id + " profit=" + profit;
    }
}
