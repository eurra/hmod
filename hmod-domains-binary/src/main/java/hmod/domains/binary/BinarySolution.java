
package hmod.domains.binary;

import optefx.util.random.RandomTool;

public final class BinarySolution
{
    private boolean[] data;

    public BinarySolution(int length, boolean random)
    {
        data = new boolean[length];
        
        if(random)
            randomize();
    }
    
    public BinarySolution(BinarySolution toCopy)
    {
        data = toCopy.data.clone();
    }
    
    public void randomize()
    {
        for(int i = 0; i < data.length; i++)
            data[i] = RandomTool.getBoolean();
    }
    
    public double fitness()
    {
        int count = 0;
        
        for(int i = 0; i < data.length; i++)
        {
            if(data[i])
                count++;
        }
        
        return count;
    }
    
    public int getLength()
    {
        return data.length;
    }
    
    public void setVal(int index, boolean val)
    {
        data[index] = val;
    }
    
    public boolean getVal(int index)
    {
        return data[index];
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("");
        int count = 0;
        
        for(int i = 0; i < data.length; i++)
        {
            if(data[i])
            {
                sb.append("1");
                count++;
            }
            else
            {
                sb.append("0");
            }
        }
        
        return "(" + count + ") " + sb.toString();
    }
}
