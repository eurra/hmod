
package hmod.loader.graph;

import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.BuilderInputException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;
import hmod.core.DataHandlingException;
import hmod.core.Evaluator;
import hmod.core.Operator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultDataHandlerAssembler<T extends DataHandler> extends AbstractBuilder<T> implements DataHandlerAssembler<T>
{
    private static class Handler implements InvocationHandler
    {
        private Object defaultImplementor;
        private HashMap<Method, Operator> operatorDelegates;
        private HashMap<Method, Evaluator> evaluatorDelegates;

        public void setDefaultImplementor(Object defaultImplementor)
        {
            this.defaultImplementor = defaultImplementor;
        }

        public void setOperatorDelegates(HashMap<Method, Operator> operatorDelegates)
        {
            this.operatorDelegates = operatorDelegates;
        }

        public void setEvaluatorDelegates(HashMap<Method, Evaluator> evaluatorDelegates)
        {
            this.evaluatorDelegates = evaluatorDelegates;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            if(operatorDelegates.containsKey(method))
            {
                operatorDelegates.get(method).execute();
                return null;
            }
            else if(evaluatorDelegates.containsKey(method))
            {
                return evaluatorDelegates.get(method).evaluate();
            }
            else if(defaultImplementor != null)
            {
                return method.invoke(defaultImplementor, args);
            }
            else if(method.getDeclaringClass().equals(Object.class))
            {
                return method.invoke(proxy, args);
            }
            else
            {
                throw new UnsupportedOperationException("No proper operator or evaluator delegate was defined for the method '" + method + "'");
            }
        }
    }
    
    private final Class<T> handlerType;
    private final HashSet<String> remainingOperatorMethods;
    private final HashSet<String> remainingEvaluatorMethods;
    private final HashMap<String, NestedBuilder<? extends Operator>> operatorDelegates;
    private final HashMap<String, NestedBuilder<? extends Evaluator>> evaluatorDelegates;
    private NestedBuilder<? extends T> defaultImplementor;

    public DefaultDataHandlerAssembler(Class<T> handlerType)
    {
        this(handlerType, null);
    }
    
    public DefaultDataHandlerAssembler(Class<T> handlerType, NestedBuilder<? extends T> defaultImplementor)
    {
        if(handlerType == null)
            throw new NullPointerException("Null handler type");
        
        if(!handlerType.isInterface())
            throw new IllegalArgumentException("The provided handler type must be an interface");
        
        this.handlerType = handlerType;
        this.remainingOperatorMethods = new HashSet<>();
        this.remainingEvaluatorMethods = new HashSet<>();
        boolean requiresImplementor = scanHandlerType();
        
        if(requiresImplementor && defaultImplementor == null)
            throw new NullPointerException("The handler type has methods that not complains with operator/evaluator standard signature, therefore a default implementor is required");
        
        this.defaultImplementor = defaultImplementor;
        this.operatorDelegates = new HashMap<>(remainingOperatorMethods.size());
        this.evaluatorDelegates = new HashMap<>(remainingOperatorMethods.size());
    }
    
    private boolean scanHandlerType()
    {
        Method[] methods = handlerType.getDeclaredMethods();
        boolean requiresImplementor = false;
        
        for(int i = 0; i < methods.length; i++)
        {
            if(methods[i].getDeclaringClass().equals(Object.class))
                continue;
            
            if(isOperatorMethod(methods[i]))
                remainingOperatorMethods.add(methods[i].getName());
            else if(isEvaluatorMethod(methods[i]))
                remainingEvaluatorMethods.add(methods[i].getName());
            else
                requiresImplementor = true;
        }
        
        return requiresImplementor;
    }
    
    private boolean isOperatorMethod(Method method)
    {
        return method.getParameterCount() == 0 && method.getReturnType().equals(Void.TYPE);
    }
    
    private boolean isEvaluatorMethod(Method method)
    {
        return method.getParameterCount() == 0 && method.getReturnType().equals(Boolean.TYPE);
    }
    
    private boolean isValidOperatorMethodName(String name)
    {
        return remainingOperatorMethods.contains(name) || operatorDelegates.containsKey(name);
    }
    
    private boolean isValidEvaluatorMethodName(String name)
    {
        return remainingEvaluatorMethods.contains(name) || evaluatorDelegates.containsKey(name);
    }

    @Override
    public DataHandlerAssembler<T> addOperatorDelegate(String methodName, NestedBuilder<? extends Operator> delegate) throws DataHandlingException
    {
        if(methodName == null)
            throw new NullPointerException("Null method name");
        
        if(delegate == null)
            throw new NullPointerException("Null delegate builder");
        
        if(!isValidOperatorMethodName(methodName))
            throw new DataHandlingException("The method '" + methodName + "' in type '" + handlerType + "' has not a valid operator signature");
        
        operatorDelegates.put(methodName, delegate);
        
        if(remainingOperatorMethods.contains(methodName))
            remainingOperatorMethods.remove(methodName);
        
        return this;
    }

    @Override
    public DataHandlerAssembler<T> addEvaluatorDelegate(String methodName, NestedBuilder<? extends Evaluator<Boolean>> delegate) throws DataHandlingException
    {
        if(methodName == null)
            throw new NullPointerException("Null method name");
        
        if(delegate == null)
            throw new NullPointerException("Null delegate builder");
        
        if(!isValidEvaluatorMethodName(methodName))
            throw new DataHandlingException("The method '" + methodName + "' in type '" + handlerType + "' has not a valid evaluator signature");
        
        evaluatorDelegates.put(methodName, delegate);
        
        if(remainingEvaluatorMethods.contains(methodName))
            remainingEvaluatorMethods.remove(methodName);
        
        return this;
    }
    
    private String getRemainingMethods(HashSet<String> input)
    {
        String res = "";
        
        for(String name : input)
            res += name + " ";
        
        return res;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        if(!remainingOperatorMethods.isEmpty() && defaultImplementor == null)
            throw new BuilderInputException("There are operator methods for whose no delegate has been assigned: " + getRemainingMethods(remainingOperatorMethods));
        
        if(!remainingEvaluatorMethods.isEmpty() && defaultImplementor == null)
            throw new BuilderInputException("There are evaluator methods for whose no delegate has been assigned: " + getRemainingMethods(remainingEvaluatorMethods));
        
        Handler handler = new Handler();
        
        T result = (T)Proxy.newProxyInstance(
            handlerType.getClassLoader(),
            new Class[] {handlerType}, 
            handler
        );
        
        session.registerResult(this, result);
        
        HashMap<Method, Operator> finalOperatorMap = new HashMap<>(operatorDelegates.size());
        HashMap<Method, Evaluator> finalEvaluatorMap = new HashMap<>(evaluatorDelegates.size());
        
        try
        {
            for(String methodName : operatorDelegates.keySet())
            {
                Method method = handlerType.getMethod(methodName);
                Operator op = operatorDelegates.get(methodName).build(session);
                
                finalOperatorMap.put(method, op);
            }
            
            for(String methodName : evaluatorDelegates.keySet())
            {
                Method method = handlerType.getMethod(methodName);
                Evaluator eval = evaluatorDelegates.get(methodName).build(session);
                
                finalEvaluatorMap.put(method, eval);
            }
        }
        catch(NoSuchMethodException | SecurityException ex)
        {
            throw new BuildException(ex);
        }
        
        if(defaultImplementor != null)
            handler.setDefaultImplementor(defaultImplementor.build(session));
        
        handler.setOperatorDelegates(finalOperatorMap);
        handler.setEvaluatorDelegates(finalEvaluatorMap);
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("DataHandlerAssembler").
            addStateData("type", handlerType.getSimpleName());
    }
}
