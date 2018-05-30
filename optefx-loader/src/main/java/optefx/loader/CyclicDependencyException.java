
package optefx.loader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public class CyclicDependencyException extends ModuleLoadException
{
    private final List<String> stack = new ArrayList<>();

    public CyclicDependencyException()
    {
    }
    
    public CyclicDependencyException(String message)
    {
        super(message);
    }

    public CyclicDependencyException(Throwable cause)
    {
        super(cause);
    }

    public CyclicDependencyException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public void addToStack(String element)
    {
        if(element != null)
            stack.add(element);
    }

    @Override
    public String getMessage()
    {
        String msg = super.getMessage();
        
        if(!stack.isEmpty())
        {
            msg += ".\nStack:\n";
            int cant = stack.size();
            
            for(int i = cant - 1; i >= 0; i--)
                msg += "> " + stack.get(i) + "\n";
        }
        
        return msg;
    }
}
