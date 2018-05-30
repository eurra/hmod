
package optefx.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
public final class ModuleLoader
{
    private static class ConstrainedProvider implements ComponentRegister
    {
        private final Method loaderEntity;
        private final ModuleState state;
        private final Set<Class> toProvide;
        private final Set<Class> actuallyProvided = new HashSet<>();
        
        public ConstrainedProvider(Method loaderEntity,
                                   ModuleState state,
                                   Set<Class> toProvide)
        {
            this.loaderEntity = loaderEntity;
            this.state = state;
            this.toProvide = new HashSet<>(toProvide);
            
            for(Class parType : loaderEntity.getParameterTypes())
            {
                if(this.toProvide.contains(parType))
                    this.toProvide.remove(parType);
            }
        }
        
        private void checkProvideRequest(Class provided) throws ModuleLoadException
        {
            if(!toProvide.contains(provided))
                throw new NonDeclaredProvidingException(provided);
        }

        @Override
        public <T> T provide(T implementor, Class<? super T>... providedTypes) throws ModuleLoadException
        {
            Objects.requireNonNull(implementor, "null implementor");
            
            for(int i = 0; i < providedTypes.length;i ++)
                provideInState(providedTypes[i], implementor);
            
            return implementor;
        }
        
        @Override
        public <T> T provide(T implementor) throws ModuleLoadException
        {
            return provideInState((Class<? super T>)implementor.getClass(), implementor);
        }

        private <T> T provideInState(Class<? super T> type, T implementor) throws ModuleLoadException
        {
            Objects.requireNonNull(implementor, "null implementor");
            Objects.requireNonNull(type, "null type");
            
            checkProvideRequest(type);
            T res = state.addComponent(type, implementor);
            actuallyProvided.add(type);
            return res;
        }
        
        public void validateProvided() throws ModuleLoadException
        {
            for(Class type : toProvide)
            {
                if(!actuallyProvided.contains(type))
                    throw new MissingProvidingException(type, loaderEntity);
            }
        }
    }
    
    private static class LoaderNode
    {
        private final Method loader;
        private final Set<Class> declaredTypes = new HashSet<>();
        private final Set<Class> providedTypes = new HashSet<>();
        private final Set<Class> dependencies;
        private final Map<Class, List<LoaderNode>> childs = new HashMap<>();

        public LoaderNode(Method loader, Class[] provided) throws ModuleLoadException
        {
            this.loader = loader;
            
            for(int i = 0; i < provided.length; i++)
            {
                declaredTypes.add(Objects.requireNonNull(provided[i], "null provided type at position " + i));
                Set<Class> currentProvidedTypes = ModuleState.fillHierarchy(provided[i]);
                providedTypes.addAll(currentProvidedTypes);
            }
            
            dependencies = new HashSet<>(loader.getParameterCount());
            Class[] args = loader.getParameterTypes();
            
            for(Class dependency : args)
            {
                if(!dependency.equals(ComponentRegister.class) && !dependency.equals(ParameterRegister.class))
                    dependencies.add(dependency);
            }
        }

        public Method getLoader()
        {
            return loader;
        }
        
        public Set<Class> getDeclaredTypes()
        {
            return declaredTypes;
        }

        public Set<Class> getProvidedTypes()
        {
            return providedTypes;
        }
        
        public Set<Class> getDependencies()
        {
            return dependencies;
        }
        
        public void addChild(Class dependency, LoaderNode node)
        {
            if(!childs.containsKey(dependency))
                childs.put(dependency, new ArrayList<>());
            
            childs.get(dependency).add(node);
        }

        public List<LoaderNode> getChilds(Class dependency)
        {
            if(!childs.containsKey(dependency))
                return Collections.EMPTY_LIST;
            
            return childs.get(dependency);
        }
        
        public void invoke(Object[] args)
        {
            try
            {
                loader.invoke(null, args);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                Throwable cause = ex;

                if(ex instanceof InvocationTargetException)
                    cause = ex.getCause();

                if(cause instanceof ModuleLoadException)
                    throw (ModuleLoadException)cause;

                throw new ModuleLoadException("Cannot execute the loader '" + loader + "': " + cause.getLocalizedMessage(), ex);
            }
        }

        @Override
        public String toString()
        {
            return loader.toString();
        }
    }
    
    private final Set<LoaderNode> startPoints = new HashSet<>();
    private final Map<Class, List<LoaderNode>> providers = new HashMap<>();
    private final Map<Class, List<LoaderNode>> requesters = new HashMap<>();
    //private final List<LoaderNode> addedLoaders = new ArrayList<>();
    private final Set<Method> addedLoaders = new HashSet<>();
    private final List<PostProcessor> postProcessors = new ArrayList<>();
    private final Map<Parameter, Object> parameterValues = new HashMap<>();
    
    private void registerLoader(Method loader, Class[] provided)
    {
        LoaderNode newNode = new LoaderNode(loader, provided);
        Set<Class> providedTypes = newNode.getProvidedTypes();
        
        for(Class providedType : providedTypes)
        {
            if(!providers.containsKey(providedType))
                providers.put(providedType, new ArrayList<>());
            
            providers.get(providedType).add(newNode);
            
            if(requesters.containsKey(providedType))
            {
                List<LoaderNode> reqList = requesters.get(providedType);
                
                for(LoaderNode req : reqList)
                    req.addChild(providedType, newNode);
            }
        }
        
        Set<Class> dependencies = newNode.getDependencies();
        
        if(dependencies.isEmpty())
        {
            startPoints.add(newNode);
        }
        else
        {        
            for(Class dependency : dependencies)
            {
                if(!requesters.containsKey(dependency))
                    requesters.put(dependency, new ArrayList<>());

                requesters.get(dependency).add(newNode);

                if(providers.containsKey(dependency))
                {
                    List<LoaderNode> provList = providers.get(dependency);

                    for(LoaderNode prov : provList)
                        newNode.addChild(dependency, prov);
                }
            }
        }
        
        addedLoaders.add(loader);
        //addedLoaders.add(newNode);
    }
    
    public ModuleLoader addPostProcessors(PostProcessor... pps)
    {
        for(PostProcessor pp : pps)
            postProcessors.add(Objects.requireNonNull(pp, "null post-processor"));
        
        return this;
    }
    
    public ModuleLoader loadAll(Class... loaderTypes) throws ModuleLoadException
    {
        for(Class loader : loaderTypes)
            load(loader);
        
        return this;
    }
    
    public ModuleLoader load(Class loaderType, String... methodNames) throws ModuleLoadException
    {
        Set<String> methodNamesSet = new HashSet<>(methodNames.length);
        
        for(int i = 0; i < methodNames.length; i++)
            methodNamesSet.add(Objects.requireNonNull(methodNames[i], "null name at position " + i));
        
        Method[] methods = Objects.requireNonNull(loaderType, "null loader type").getDeclaredMethods();
        int found = 0;

        for(Method method : methods)
        {
            if(!methodNamesSet.isEmpty() && !methodNamesSet.contains(method.getName()))
                continue;
            
            if(!method.isAnnotationPresent(LoadsComponent.class))
                continue;
            
            if(!addedLoaders.contains(method))
            {
                if(!Modifier.isStatic(method.getModifiers()))
                    throw new ModuleLoadException("The loader method '" + method + "' must be static");

                try
                {
                    if(!method.isAccessible())
                        method.setAccessible(true);
                }
                catch(SecurityException ex)
                {
                    throw new ModuleLoadException("Non accesible loader method: '" + method + "'", ex);
                }

                LoadsComponent info = method.getAnnotation(LoadsComponent.class);
                registerLoader(method, info.value());
            }

            found++;
        }
        
        if(found == 0)
            throw new ModuleLoadException("No valid loader methods were found in type '" + loaderType + "' with the provided names");
        
        return this;
    }
    
    public <T, K> ModuleLoader setParameter(Parameter<T> param, T value)
    {
        Objects.requireNonNull(param, "null parameter");
        Objects.requireNonNull(value, "null value");
        
        parameterValues.put(param, value);
        return this;
    }
    
    public ModuleLoader clearParameter(Parameter param)
    {
        parameterValues.remove(Objects.requireNonNull(param, "null parameter"));
        return this;
    }
    
    private void loadNode(LoaderNode toLoad, ModuleState state, Set<Method> activeLoaders, Set<Method> alreadyLoaded) throws ModuleLoadException
    {
        loadNode(toLoad, null, state, activeLoaders, alreadyLoaded);
    }
    
    private void loadNode(LoaderNode toLoad, Class requested, ModuleState state, Set<Method> activeLoaders, Set<Method> alreadyLoaded) throws ModuleLoadException
    {      
        Method loader = toLoad.getLoader();
        
        if(alreadyLoaded.contains(loader))
            return;
        
        if(activeLoaders.contains(toLoad.getLoader()))
            throw new CyclicDependencyException("Cyclic loader dependency (" + requested + ") required at '" + toLoad + "'");
        
        activeLoaders.add(toLoad.getLoader());
        
        try
        {
            Class[] requiredTypes = loader.getParameterTypes();
            Object[] finalArgs = new Object[requiredTypes.length];
            ConstrainedProvider cp = null;
            int currPos = 0;

            for(Class requiredType : requiredTypes)
            {
                if(toLoad.getDeclaredTypes().contains(requiredType) && state.contains(requiredType))
                {
                    finalArgs[currPos++] = state.get(requiredType);
                }
                else if(requiredType.equals(ComponentRegister.class))
                {
                    if(cp == null)
                        cp = new ConstrainedProvider(loader, state, toLoad.getDeclaredTypes());

                    finalArgs[currPos++] = cp;
                }
                else if(requiredType.equals(ParameterRegister.class))
                {
                    finalArgs[currPos++] = state;
                }
                else
                {
                    List<LoaderNode> providerNodes = toLoad.getChilds(requiredType);

                    if(providerNodes.isEmpty())
                        throw new UnmetDependencyException(requiredType, loader);

                    for(LoaderNode child : providerNodes)
                    {
                        if(alreadyLoaded.contains(child.getLoader()))
                            continue;
                        
                        loadNode(child, requiredType, state, activeLoaders, alreadyLoaded);
                    }

                    finalArgs[currPos++] = state.get(requiredType);
                } 
            }

            toLoad.invoke(finalArgs);

            if(cp != null)
                cp.validateProvided();
        }
        catch(CyclicDependencyException ex)
        {
            ex.addToStack("'" + requested + "' @ '" + toLoad + "'");
            throw ex;
        }
        finally
        {
            activeLoaders.remove(toLoad.getLoader());
        }  

        alreadyLoaded.add(loader);
        state.addToLoadLog(toLoad.toString());
        
        Set<Class> declaredTypes = toLoad.getDeclaredTypes();
        
        for(Class type : declaredTypes)
        {
            if(providers.containsKey(type))
            {
                List<LoaderNode> nodeList = providers.get(type);

                for(LoaderNode node : nodeList)
                {
                    if(alreadyLoaded.contains(node.getLoader()))
                        continue;

                    loadNode(node, type, state, activeLoaders, alreadyLoaded);
                }
            }
        }            
    }
    
    public Module getModule() throws ModuleLoadException
    {
        ModuleState state = new ModuleState(new HashMap<>(parameterValues));
        HashSet<Method> alreadyLoaded = new HashSet<>();
        
        for(LoaderNode node : startPoints)
            loadNode(node, state, new HashSet<>(), alreadyLoaded);
        
        for(Class requestedType : requesters.keySet())
        {
            List<LoaderNode> nodes = requesters.get(requestedType);
            
            for(LoaderNode node : nodes)
                loadNode(node, state, new HashSet<>(), alreadyLoaded);
        }
        
        for(PostProcessor pp : postProcessors)
            pp.process(state, state);
        
        state.finish();
        return new Module(state);
    }
    
    public <T> T getInstance(Class<T> componentType) throws ModuleLoadException
    {
        return getModule().getInstance(componentType);
    }
}
