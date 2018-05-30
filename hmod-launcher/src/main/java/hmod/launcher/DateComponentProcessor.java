
package hmod.launcher;

import java.util.Calendar;

/**
 *
 * @author Enrique Urra C.
 */
public class DateComponentProcessor implements TextVariableProcessor
{
    @Override
    public String process(String input) throws LauncherException
    {
        Calendar rightNow = Calendar.getInstance();
        
        if(input.equals("D(y)"))
            return Integer.toString(rightNow.get(Calendar.YEAR));
        
        if(input.equals("D(M)"))
            return Integer.toString(rightNow.get(Calendar.MONTH));
        
        if(input.equals("D(d)"))
            return Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH));
        
        if(input.equals("D(H)"))
            return Integer.toString(rightNow.get(Calendar.HOUR_OF_DAY));
        
        if(input.equals("D(m)"))
            return Integer.toString(rightNow.get(Calendar.MINUTE));
        
        if(input.equals("D(s)"))
            return Integer.toString(rightNow.get(Calendar.SECOND));
        
        if(input.equals("D(S)"))
            return Integer.toString(rightNow.get(Calendar.MILLISECOND));
        
        if(input.equals("D(MS)"))
            return Long.toString(System.currentTimeMillis());
        
        if(input.equals("D(NS)"))
            return Long.toString(System.nanoTime());
        
        return null;
    }
}
