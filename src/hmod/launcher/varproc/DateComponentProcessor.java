
package hmod.launcher.varproc;

import hmod.launcher.LauncherException;
import hmod.launcher.VariableProcessor;
import java.util.Calendar;

/**
 *
 * @author Enrique Urra C.
 */
public class DateComponentProcessor implements VariableProcessor
{
    @Override
    public String process(String input) throws LauncherException
    {
        Calendar rightNow = Calendar.getInstance();
        String res = input.replace("D(y)", Integer.toString(rightNow.get(Calendar.YEAR)))
            .replace("D(M)", String.format("%02d", rightNow.get(Calendar.MONTH)))
            .replace("D(d)", String.format("%02d", rightNow.get(Calendar.DAY_OF_MONTH)))
            .replace("D(H)", String.format("%02d", rightNow.get(Calendar.HOUR_OF_DAY)))
            .replace("D(m)", String.format("%02d", rightNow.get(Calendar.MINUTE)))
            .replace("D(s)", String.format("%02d", rightNow.get(Calendar.SECOND)))
            .replace("D(S)", Integer.toString(rightNow.get(Calendar.MILLISECOND)))
            .replace("D(MS)", Long.toString(System.currentTimeMillis()))
            .replace("D(NS)", Long.toString(System.nanoTime()));
        
        return res;
    }
}
