
package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.block;
import hmod.core.Statement;
import optefx.loader.LoadsComponent;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class DummyDARPTW
{
    private static Statement createMessage(String msg)
    {
        return () -> OutputManager.println(DARPTWOutputIds.RESULT_DETAIL, msg);
    }
    
    @LoadsComponent
    public static void load(DARPTWDomain domain)
    {
        domain.solutionCheck().
            apply((st) -> block(createMessage("This is the main code!"), st)).
            append(createMessage("Appended just after the main code.")).
            appendBefore(createMessage("Appended at the end of the code before the main code.")).
            appendAfter(createMessage("Appended at the end of the code after the main code.")).            
            prepend(createMessage("Prepended just before the main code.")).
            prependBefore(createMessage("Prepended at the start of the code before the main code.")).
            prependAfter(createMessage("Prepended at the start of the code after the main code."));
    }
}
