
package hmod.loader.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import hmod.core.DataHandler;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import optefx.util.tools.ReflectionTools;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultDataProxyBuilder extends AbstractBuilder<DataHandler> implements DataProxyBuilder
{
    private static class InnerProxy implements InvocationHandler
    {
        Map<Method, Object> proxyMap;

        public InnerProxy(Map<Method, Object> proxyMap)
        {
            this.proxyMap = proxyMap;
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
        {
            Object handler = proxyMap.get(method);
            
            if(handler == null)
                throw new BuildException("The method '" + method + "' is not executable in this proxy");
            
            try
            {
                return method.invoke(handler, args);
            }
            catch(IllegalAccessException | IllegalArgumentException ex)
            {
                throw new BuildException("Cannot access the method '" + method + "' in this proxy", ex);
            }
            catch(InvocationTargetException ex)
            {
                throw new BuildException("Error when executing the method '" + method + "' in this proxy", ex.getTargetException());
            }
        }
    }
    
    private class HandlerEntry
    {
        private final List<Class> coveredHandlerTypes;
        private final Object factory;
        private final NestedBuilder[] creationArgs;

        public HandlerEntry(List<Class> coveredHandlerTypes, Object factory, NestedBuilder[] creationArgs)
        {
            this.coveredHandlerTypes = coveredHandlerTypes;
            this.factory = factory;
            this.creationArgs = creationArgs;
        }

        public List<Class> getCoveredHandlerTypes()
        {
            return coveredHandlerTypes;
        }
        
        public Object getFactory()
        {
            return factory;
        }

        public NestedBuilder[] getCreationArgs()
        {
            return creationArgs;
        }
    }
    
    private final Class[] handlerTypes;
    private final Set<Class> coveredHandlers;
    private final List<HandlerEntry> entries;
    private int remainingToCover;

    public DefaultDataProxyBuilder(Class<? extends DataHandler>... handlerTypes)
    {
        for(int i = 0; i < handlerTypes.length; i++)
        {
            if(handlerTypes[i] == null)
                throw new BuilderInputException("Null handler type at position " + i);
        }
        
        this.handlerTypes = handlerTypes;
        this.coveredHandlers = new HashSet<>(handlerTypes.length);
        this.entries = new ArrayList<>(handlerTypes.length);
        this.remainingToCover = this.handlerTypes.length;
    }

    @Override
    public DataProxyBuilder addFactory(Object handlerFactory, NestedBuilder... creationArgs)
    {
        if(handlerFactory == null)
            throw new BuilderInputException("Null factory");
        
        if(remainingToCover == 0)
            return this;
        
        if(creationArgs == null)
            creationArgs = new NestedBuilder[0];
        
        Method[] checkMethods = handlerFactory.getClass().getMethods();
        List<Class> coveredByThisFactory = new ArrayList<>(remainingToCover);
        
        for(int i = 0; i < checkMethods.length && remainingToCover > 0; i++)
        {
            Class methodReturnType = checkMethods[i].getReturnType();
            
            if(!checkMethods[i].getName().endsWith("Handler") || !DataHandler.class.isAssignableFrom(methodReturnType))
                continue;
            
            for(int j = 0; j < handlerTypes.length && remainingToCover > 0; j++)
            {
                if(coveredHandlers.contains(handlerTypes[j]))
                    continue;
                
                if(methodReturnType.isAssignableFrom(handlerTypes[j]))
                {
                    coveredHandlers.add(handlerTypes[j]);
                    coveredByThisFactory.add(handlerTypes[j]);
                    remainingToCover--;
                }
            }
        }
        
        if(coveredByThisFactory.isEmpty())
            throw new BuilderInputException("The factory type '" + handlerFactory.getClass() + "' do not cover any defined data handler");
        
        entries.add(new HandlerEntry(coveredByThisFactory, handlerFactory, creationArgs));        
        return this;
    }
    
    private Method findCompatibleMethod(Class[] methodArgs, Class targeClass)
    {
        Method[] allMethods = targeClass.getMethods();
        
        for(Method toCheck : allMethods)
        {
            if(!toCheck.getName().endsWith("Handler"))
                continue;
            
            Class[] toCheckArgs = toCheck.getParameterTypes();
            
            if(toCheckArgs.length != methodArgs.length)
                continue;
            
            boolean argsCompatible = true;
            
            for(int i = 0; argsCompatible && i < methodArgs.length; i++)
            {
                if((methodArgs[i] == null && toCheckArgs[i].isPrimitive()) ||
                    (methodArgs[i] != null && !toCheckArgs[i].isAssignableFrom(methodArgs[i])))
                    argsCompatible = false;
            }
            
            if(!argsCompatible)
                continue;
            
            return toCheck;
        }
        
        return null;
    }
    
    private DataHandler createHandlerInstance(HandlerEntry entry, BuildSession session)
    {
        Object factory = entry.getFactory();
        NestedBuilder[] creationArgsBuilders = entry.getCreationArgs();
        Object[] creationArgs = new Object[creationArgsBuilders.length];
        Class[] creationArgsTypes = new Class[creationArgsBuilders.length];

        for(int i = 0; i < creationArgsBuilders.length; i++)
        {
            creationArgs[i] = creationArgsBuilders[i].build(session);
            creationArgsTypes[i] = creationArgs[i] == null ? null : ReflectionTools.getClassFromObject(creationArgs[i]);
        }
        
        Method factoryMethod = findCompatibleMethod(creationArgsTypes, factory.getClass());
        
        if(factoryMethod == null)
            throw new BuildException("Cannot retrieve a compatible data handler factory method from type '" + factory.getClass() + "'");

        DataHandler handlerInstance = null;

        try
        {
            handlerInstance = (DataHandler)factoryMethod.invoke(factory, creationArgs);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            throw new BuildException("Cannot invoke the data handler factory method '" + factoryMethod + "' of type '" + factory.getClass() + "'");
        }
        
        return handlerInstance;
    }
    
    private DataHandler buildSingleHandler(BuildSession session)
    {
        return createHandlerInstance(entries.get(0), session);
    }
    
    private DataHandler buildMultipleHandler(BuildSession session)
    {
        Map<Method, Object> proxyMap = new HashMap<>();
        
        for(HandlerEntry entry : entries)
        {
            DataHandler handlerInstance = createHandlerInstance(entry, session);
            List<Class> currentCoveredHandlers = entry.getCoveredHandlerTypes();
            
            for(Class handlerType : currentCoveredHandlers)
            {            
                Method[] handlerTypeMethods = handlerType.getMethods();

                for(Method handlerMethod : handlerTypeMethods)
                    proxyMap.put(handlerMethod, handlerInstance);
            }
        }
        
        try
        {
            return (DataHandler)Proxy.newProxyInstance(
                handlerTypes[0].getClassLoader(),
                handlerTypes,
                new InnerProxy(proxyMap)
            );
        }
        catch(IllegalArgumentException ex)
        {
            throw new BuildException("Cannot create the data proxy", ex);
        }
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        if(remainingToCover > 0)
        {
            StringBuilder sb = new StringBuilder("There are data handlers not covered by factories:");
            
            for(int i = 0; i < handlerTypes.length; i++)
            {
                if(!coveredHandlers.contains(handlerTypes[i]))
                    sb.append(" ").append(handlerTypes[i].getName());
            }
            
            throw new BuildException(sb.toString());
        }
        
        if(handlerTypes.length > 1)
            session.registerResult(this, buildMultipleHandler(session));
        else
            session.registerResult(this, buildSingleHandler(session));
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().setName("DataProxy");
    }
}
