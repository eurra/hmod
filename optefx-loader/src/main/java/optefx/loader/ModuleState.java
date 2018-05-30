
package optefx.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author Enrique Urra C.
 */
class ModuleState implements ParameterRegister, Retriever
{
    @FunctionalInterface
    private interface ProcessHandler
    {
        void process(Retriever ret);
    }
    
    public static Set<Class> fillHierarchy(Class topOfHierarchy) throws AmbiguousImplementationException
    {
        return fillHierarchy(topOfHierarchy, topOfHierarchy, new HashSet<>());
    }
    
    public static Set<Class> fillHierarchy(Class original, Class current, Set<Class> hierarchy) throws AmbiguousImplementationException
    {
        hierarchy.add(current);
        Class[] interfaces = current.getInterfaces();

        for(int i = 0; i < interfaces.length; i++)
            fillHierarchy(original, interfaces[i], hierarchy);

        if(current.getSuperclass() != null && !current.getSuperclass().equals(Object.class))
            fillHierarchy(original, current.getSuperclass(), hierarchy);

        return hierarchy;
    }
    
    private final Map<Class, List> components = new HashMap<>();
    private final Set<ProcessHandler> processables = new HashSet<>();
    private final Map<Trigger, List> parameterTriggers = new HashMap<>();
    private final Map<Resolvable, List> bounds = new HashMap<>();
    private final Map<Parameter, Object> parameterValues;
    private final List<String> loadLog = new ArrayList<>();
    
    public ModuleState(Map<Parameter, Object> parameterValues)
    {
        this.parameterValues = parameterValues;
    }
    
    public <T> T addComponent(Class<? super T> publicType, T instance) throws ModuleLoadException
    {
        Objects.requireNonNull(publicType, "null public type");
        Objects.requireNonNull(instance, "null instance");
        
        Set<Class> providedTypes = fillHierarchy(publicType);
        ProcessHandler toProcess = scanProcessable(instance);
        
        for(Class providedType : providedTypes)
        {
            if(!components.containsKey(providedType))
                components.put(providedType, new ArrayList<>());
            
            components.get(providedType).add(instance);
        }
        
        if(toProcess != null)
            processables.add(toProcess);
        
        return instance;
    }
    
    private ProcessHandler scanProcessable(Object toScan) throws ModuleLoadException
    {
        Method[] methods = toScan.getClass().getDeclaredMethods();
        
        for(Method method : methods)
        {
            if(!method.isAnnotationPresent(Processable.class))
                continue;
            
            if(Modifier.isStatic(method.getModifiers()))
                throw new ModuleLoadException("The processor method '" + method + "' must be an instance method for processing");
            
            if(!method.getReturnType().equals(void.class) || method.getParameterCount() > 1 || 
                (method.getParameterCount() == 1 && !method.getParameterTypes()[0].equals(Retriever.class)))
            {
                throw new ModuleLoadException("The processor method '" + method + "' has a bad signature");
            }   
            
            try
            {
                if(!method.isAccessible())
                    method.setAccessible(true);
                
                return (ret) -> {
                    try
                    {
                        if(method.getParameterCount() == 0)
                            method.invoke(toScan);
                        else
                            method.invoke(toScan, ret);
                    }
                    catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                    {
                        Throwable cause = ex instanceof InvocationTargetException ? ex.getCause() : ex;
                        throw new ModuleLoadException("Cannot execute the processor method '" + method + "': " + cause.getMessage(), ex);
                    }
                };
            }
            catch(SecurityException ex)
            {
                throw new ModuleLoadException("Cannot access to the processor method '" + method + "': " + ex.getLocalizedMessage(), ex);
            }
        }
        
        return null;
    }
    
    public boolean hasComponent(Class compType)
    {
        return components.containsKey(compType);
    }
    
    @Override
    public <T> T get(Class<T> requiredType) throws ModuleLoadException
    {
        if(!contains(requiredType))
            throw new MissingComponentException(requiredType);
        
        List list = components.get(requiredType);
        
        if(list.size() > 1)
            throw new AmbiguousImplementationException(requiredType, list.get(0), list.get(1));
        
        return (T)list.get(0);
    }

    @Override
    public boolean contains(Class type)
    {
        return components.containsKey(Objects.requireNonNull(type, "null type"));
    }
    
    @Override
    public <T> void addListener(Trigger<T> param, Consumer<T> listener)
    {
        Objects.requireNonNull(param, "null parameter");
        Objects.requireNonNull(listener, "null listener");
        
        if(!parameterTriggers.containsKey(param))
            parameterTriggers.put(param, new ArrayList());
        
        Consumer<T> finalListener = (val) -> {
            if(val != null)
                listener.accept(val);
        };
        
        parameterTriggers.get(param).add(finalListener);
    }

    @Override
    public <T> void addBoundHandler(Resolvable<T> value, Consumer<T> handler)
    {
        Objects.requireNonNull(value, "null value");
        Objects.requireNonNull(handler, "null handler");
        
        if(!bounds.containsKey(value))
        {
            bounds.put(value, new ArrayList());
            processables.add((ret) -> {
                T finalValue = value.resolve(ret);
                List<Consumer<T>> handlers = bounds.get(value);
                
                for(Consumer<T> finalHandler : handlers)
                    finalHandler.accept(finalValue);
            });
        }
        
        bounds.get(value).add(handler);
    }
    
    public <T> void setOnline(Trigger<T> param, T value) throws ModuleException
    {
        if(!parameterTriggers.containsKey(param))
            throw new ModuleException("No handlers were registered for the trigger parameter '" + param + "'");
        
        List<Consumer<T>> toRun = parameterTriggers.get(param);
        
        for(Consumer<T> handler : toRun)
            handler.accept(value);
    }

    @Override
    public <T> T getValue(Parameter<T> param)
    {
        return (T)parameterValues.get(param);
    }

    @Override
    public <T> T getRequiredValue(Parameter<T> param) throws ModuleLoadException
    {
        T val = getValue(param);
        
        if(val == null)
            throw new MissingParameterException(param);
        
        return val;
    }

    @Override
    public boolean isValueSet(Parameter param)
    {
        return parameterValues.containsKey(param);
    }
    
    public void addToLoadLog(String entry)
    {
        loadLog.add(entry);
    }
    
    public String[] getLoadLog()
    {
        return loadLog.toArray(new String[0]);
    }
    
    public void finish() throws ModuleLoadException
    {
        for(ProcessHandler proc : processables)
            proc.process(this);
    }
}
