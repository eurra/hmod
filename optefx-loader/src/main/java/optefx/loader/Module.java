
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public final class Module
{
    private final ModuleState state;

    Module(ModuleState state)
    {
        this.state = state;
    }
    
    public <T> T getInstance(Class<T> compType) throws ModuleLoadException
    {
        return state.get(compType);
    }
    
    public boolean hasInstance(Class compType)
    {
        return state.hasComponent(compType);
    }
    
    public <T> Module setParameter(Trigger<T> param, T value) throws ModuleLoadException
    {
        state.setOnline(param, value);
        return this;
    }
    
    public String getLoadLog()
    {
        String[] entries = state.getLoadLog();
        StringBuilder sb = new StringBuilder();
        
        for(String entry : entries)
            sb.append(entry).append("\n");
        
        return sb.toString();
    }
}
