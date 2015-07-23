
package hmod.launcher;

/**
 * Implements an exception related to errors in the parsing of algorithm 
 * definition inputs and their configuration.<p>
 * 
 * Alongside the traditional constructors, this exception type allows to 
 * configure a special 'require restart' flag, which indicate to the client if 
 * the parser from which the exception was generated requires a restart before 
 * any further parser usage. This can be used to manage inconsistent parsing
 * states. By default, such flag is configured as {@code false}.
 * 
 * @author Enrique Urra C.
 */
public class AlgorithmLoadException extends RuntimeException
{
    private boolean requireRestart;
    
    public AlgorithmLoadException(String string)
    {
        this(false, string);
    }
    
    public AlgorithmLoadException(Throwable thrwbl)
    {
        this(false, thrwbl);
    }

    public AlgorithmLoadException(String string, Throwable thrwbl)
    {
        this(false, string, thrwbl);
    }
    
    public AlgorithmLoadException(boolean requireRestart, String string)
    {
        super(string);
        this.requireRestart = requireRestart;
    }
    
    public AlgorithmLoadException(boolean requireRestart, Throwable thrwbl)
    {
        super(thrwbl);
        this.requireRestart = requireRestart;
    }

    public AlgorithmLoadException(boolean requireRestart, String string, Throwable thrwbl)
    {
        super(string, thrwbl);
        this.requireRestart = requireRestart;
    }

    public boolean requireRestart()
    {
        return requireRestart;
    }
    
    /**
     * Returns the root cause of the current exception instance. This method is
     * useful to gather build error or similar that are not at the higher levels
     * of the exception tree.
     * 
     * @return The root cause.
     */
    public Throwable getRootCause()
    {
        Throwable rootCheck = this;
        
        while(rootCheck.getCause() != null)
            rootCheck = rootCheck.getCause();
        
        return rootCheck;
    }
}