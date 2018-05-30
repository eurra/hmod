
package optefx.util.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Defines a set of operations to manipulate objects by reflection.
 * @author Enrique Urra C.
 */
public final class ReflectionTools
{    
    private static final Object[][] primitiveInfo;
    
    static
    {
        try
        {
            primitiveInfo = new Object[][] {
                new Object[] { "int", int.class, Integer.class, Integer.class.getMethod("parseInt", new Class[] { String.class } ), 0, "I" },
                new Object[] { "long", long.class, Long.class, Long.class.getMethod("parseLong", new Class[] { String.class } ), 0, "J" },
                new Object[] { "double", double.class, Double.class, Double.class.getMethod("parseDouble", new Class[] { String.class } ), 0.0, "D" },
                new Object[] { "String", String.class, String.class, null, "", "java.lang.String" },
                new Object[] { "boolean", boolean.class, Boolean.class, Boolean.class.getMethod("parseBoolean", new Class[] { String.class } ), false, "Z" },
            };
        }
        catch(NoSuchMethodException | SecurityException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    
    public static String getPrimitiveArrayPrefix(String primitiveClassName)
    {
        for(int i = 0; i < primitiveInfo.length; i++)
        {
            if(primitiveInfo[i][0].equals(primitiveClassName))
                return (String)primitiveInfo[i][5];
        }
        
        return null;
    }
    
    public static Object getDefaultPrimitiveValue(Class primitiveClassobj)
    {
        for(int i = 0; i < primitiveInfo.length; i++)
        {
            if(primitiveInfo[i][1] == primitiveClassobj)
                return primitiveInfo[i][4];
        }
        
        return null;
    }
    
    /**
     * Gets the class object of a primitive, from its related wrapper class.
     * @param wrapperClass The wrapper class object.
     * @return The class object of the primitive, or null if the provided class
     *  is not a supported primitive wrapper.
     */
    public static Class getPrimitiveClassFromWrapperClass(Class wrapperClass)
    {
        for(int i = 0; i < primitiveInfo.length; i++)
        {
            if(primitiveInfo[i][2] == wrapperClass)
                return (Class)primitiveInfo[i][1];
        }
        
        return null;
    }
    
    /**
     * Gets the primitive class from a class name.
     * @param className The class name of the primitive ("int", "double", etc)
     * @return The class object, or null if the provided String is not related
     *  to a primitive class.
     */
    public static Class getPrimitiveClassFromName(String className)
    {
        for(int i = 0; i < primitiveInfo.length; i++)
        {
            if(primitiveInfo[i][0].equals(className))
                return (Class)primitiveInfo[i][1];
        }
        
        return null;
    }
    
    /**
     * Parses a primitive from a String value.
     * @param primitiveClassObj The class that represents the primitive.
     * @param val The String value to parse.
     * @return The object of the parsed primitive, or null if the provided class
     *  is not related to a primitive type.
     * @throws ReflectionException If an error occurs in the parsing.
     */
    public static Object parsePrimitive(Class primitiveClassObj, String val) throws ReflectionException
    {
        if(primitiveClassObj == String.class)
            return val;
        
        try
        {
            for(int i = 0; i < primitiveInfo.length; i++)
            {
                if(primitiveInfo[i][1] == primitiveClassObj)
                    return ((Method)primitiveInfo[i][3]).invoke(null, val);
            }
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            throw new ReflectionException(ex);
        }
        
        return null;
    }
    
    /**
     * Parses an integer valie
     * @param val The integer as string.
     * @return The parsed value.
     * @throws ReflectionException if the value is not int.
     */
    public static int parseInt(String val) throws ReflectionException
    {
        try
        {
            return Integer.parseInt(val);
        }
        catch(NumberFormatException ex)
        {
            throw new ReflectionException(ex);
        }
    }
    
    /**
     * Parses a boolean valie
     * @param val The boolean as string.
     * @return The parsed value.
     * @throws ReflectionException if the value is not boolean.
     */
    public static boolean parseBoolean(String val) throws ReflectionException
    {
        if(!val.equals("true") && !val.equals("false"))
            throw new ReflectionException("A boolean value (true/false) was espected");

        return Boolean.parseBoolean(val);
    }
    
    /**
     * Gets a Class object from a string that represents a class name.
     * @param className The name of the class.
     * @return The class object.
     * @throws NullPointerException if the provided class is null.
     * @throws ReflectionException if the provided class was not found.
     */
    public static Class getClassFromString(String className) throws ReflectionException
    {
        return getClassFromString(className, 0);
    }
    
    /**
     * Gets a Class object from a string that represents a class name.
     * @param className The name of the class.
     * @param arrayDepth A value that represents the depth of an array to 
     *  generate. Is this value is greater than 0, the class will be parsed as
     *  an array.
     * @return The class object.
     * @throws NullPointerException if the provided class is null.
     * @throws ReflectionException if the provided class was not found.
     */
    public static Class getClassFromString(String className, int arrayDepth) throws ReflectionException
    {
        if(className == null)
            throw new NullPointerException("The class name must be provided");
        
        String finalClassName;
        
        if(arrayDepth > 0)
        {
            String arrayClassName = getPrimitiveArrayPrefix(className);
            boolean isPrim = true;
            
            if(arrayClassName == null)
            {
                arrayClassName = "L" + className;
                isPrim = false;
            }
            
            for(int i = 0; i < arrayDepth; i++)
                arrayClassName = "[" + arrayClassName;
            
            if(!isPrim)
                arrayClassName += ";";
            
            finalClassName = arrayClassName;
        }
        else
        {
            Class primClass = getPrimitiveClassFromName(className);
            
            if(primClass != null)
                return primClass;
            else
                finalClassName = className;
        }
        
        try
        {
            return Class.forName(finalClassName);
        }
        catch(ClassNotFoundException ex)
        {
            throw new ReflectionException("The class name \"" + className + "\" was not found", ex);
        }
        catch(LinkageError ex)
        {
            throw new ReflectionException("Cannot load the class \"" + className + "\"", ex);
        }
    }
    
    /**
     * Gets a class object from some object instance. Can handle primitives 
     * properly.
     * @param object the object from which the class will be extracted.
     * @return The class object.
     * @throws NullPointerException if the provided object is null.
     */
    public static Class getClassFromObject(Object object)
    {
        if(object == null)
            throw new NullPointerException("The object must be provided");
        
        Class objectClass = object.getClass();
        Class primitiveClass = getPrimitiveClassFromWrapperClass(objectClass);
        
        return primitiveClass != null ? primitiveClass : objectClass;
    }
    
    /**
     * Calls a setter-like method by its name.
     * @param target The object on which the setter will be called.
     * @param setterName The name of the setter method.
     * @param arg The argument object.
     * @throws hModException 
     */
    public static void callSetter(Object target, String setterName, Object arg) throws ReflectionException
    {
        Method setter = extractSetter(target, setterName);
        callSetter(target, setter, arg);
    }
    
    /**
     * Calls a setter-like method by a related Method object.
     * @param target The object on which the setter will be called.
     * @param setter The Method object of the setter.
     * @param arg The argument object.
     * @throws ReflectionException if a reflection-related error occurs.
     * @throws NullPointerException if the provided Method object is null.
     */
    public static void callSetter(Object target, Method setter, Object arg) throws ReflectionException
    {
        if(target == null)
            throw new NullPointerException("The target object cannot be null");
        
        if(setter == null)
            throw new NullPointerException("The setter cannot be null");
        
        try
        {
            if(!setter.isAccessible())
                setter.setAccessible(true);
            
            setter.invoke(target, arg);
        }
        catch(IllegalAccessException ex)
        {
            throw new ReflectionException("The setter \"" + setter.getName() + "\" in the class \"" + target.getClass().getCanonicalName() + "\" is not accessible", ex);
        }
        catch(IllegalArgumentException ex)
        {
            throw new ReflectionException("The type \"" + arg.getClass().getCanonicalName() + "\" cannot be used as argument of the setter \"" + setter.getName() + "\" in the class \"" + target.getClass().getCanonicalName() + "\"", ex);
        }
        catch(InvocationTargetException ex)
        {
            throw new ReflectionException("The setter \"" + setter.getName() + "\" in the class \"" + target.getClass().getCanonicalName() + "\" has thrown an exception", ex);
        }
    }
    
    /**
     * Extracts a setter-like method from a object.
     * @param source The object from which the setter will be extracted.
     * @param setterName The name of the setter method.
     * @return The related Method object.
     * @throws ReflectionException if a reflection-related error occurs.
     * @throws NullPointerException if the provided setter name is null.
     */
    public static Method extractSetter(Object source, String setterName) throws ReflectionException
    {
        if(source == null)
            throw new NullPointerException("The source object from which the setter will be extracted must be provided");
        
        return extractSetter(source.getClass(), setterName);
    }
    
    /**
     * Extracts a setter-like method from a class. This will use the first 
     * setter method with the specified method name.
     * @param sourceClass The class object from which the setter will be 
     *  extracted.
     * @param setterName The name of the setter method.
     * @return The related Method object.
     * @throws ReflectionException if a reflection-related error occurs.
     * @throws NullPointerException if the provided setter name is null.
     */
    public static Method extractSetter(Class sourceClass, String setterName) throws ReflectionException
    {
        return extractSetter(sourceClass, setterName, null);
    }
    
    /**
     * Extracts a setter-like method from a class. If {@code argType} is not 
     * null, this will find the exact setter method for such type, otherwise
     * the first method with the provided name will be used.
     * @param sourceClass The class object from which the setter will be 
     *  extracted.
     * @param setterName The name of the setter method.
     * @param argType The type of the setter argument.
     * @return The related Method object.
     * @throws ReflectionException if a reflection-related error occurs.
     * @throws NullPointerException if the provided setter name is null.
     */
    public static Method extractSetter(Class sourceClass, String setterName, Class argType) throws ReflectionException
    {
        if(sourceClass == null)
            throw new NullPointerException("The source class object from which the setter will be extracted must be provided");
        
        Method method = null;
        
        if(argType != null)
        {
            try
            {
                method = sourceClass.getDeclaredMethod(setterName, argType);
            }
            catch(NoSuchMethodException ex)
            {
                throw new ReflectionException("The setter '" + setterName + "' is not implemented in the class '" + sourceClass.getCanonicalName() + "'", ex);
            }
            catch(SecurityException ex)
            {
                throw new ReflectionException(ex);
            }
        }
        else
        {
            Method[] allMethods = sourceClass.getDeclaredMethods();

            for(int i = 0; method == null && i < allMethods.length; i++)
            {
                if(allMethods[i].getName().equals(setterName) && allMethods[i].getParameterTypes().length == 1)
                    method = allMethods[i];
            }

            if(method == null)
                throw new ReflectionException("The setter '" + setterName + "' is not implemented in the class '" + sourceClass.getCanonicalName() + "'");
        }
        
        return method;
    }
    
    /**
     * Instantiates an object from a class usign a less-argument constructor.
     * @param className The class name of the object that will be generated.
     * @return The generated object.
     * @throws NullPointerException if the provided class name is null.
     * @throws ReflectionException if a reflection-related error occurs.
     */
    public static Object createObject(String className) throws ReflectionException
    {
        return createObject(className, null);
    }
    
    /**
     * Instantiates an object from a class usign a less-argument constructor.
     * @param classObj The class object of the object that will be generated.
     * @return The generated object.
     * @throws NullPointerException if the provided class name is null.
     * @throws ReflectionException if a reflection-related error occurs.
     */
    public static Object createObject(Class classObj) throws ReflectionException
    {
        return createObject(classObj, (String)null);
    }
    
   /**
     * Instantiates an object from a class usign a less-argument constructor. 
     * This overload supports primitives.
     * @param className The class name of the object that will be generated.
     * @param value The value to parse, for primitive types.
     * @return The generated object.
     * @throws NullPointerException if the provided class name is null.
     * @throws ReflectionException if a reflection-related error or a 
     *  string parsing error occurs. 
     */
    public static Object createObject(String className, String value) throws ReflectionException
    {
        if(className == null)
            throw new NullPointerException("The class name must be provided");
        
        Class classObj = getClassFromString(className);
        return createObject(classObj, value);
    }
    
    /**
     * Instantiates an object from a dynamic proxy class, using a particular
     *  invocation handler.
     * @param classObj The class object to use in the instantiation.
     * @param handler The invocation handler to use.
     * @return The generated object.
     * @throws NullPointerException if the provided class name is null.
     * @throws ReflectionException if a reflection-related error occurs.
     */
    public static Object createObject(Class classObj, InvocationHandler handler) throws ReflectionException
    {
        return createObject(classObj, new Class[] { InvocationHandler.class } , new Object[] { handler } );
    }
    
    /**
     * Instantiates an object from a class usign a less-argument constructor. 
     * This overload supports primitives.
     * @param classObj The class object to use in the instantiation.
     * @param value The value to parse, for primitive types.
     * @return The generated object.
     * @throws NullPointerException if the provided class name is null.
     * @throws ReflectionException if a reflection-related error or a 
     *  string parsing error occurs. 
     */
    public static Object createObject(Class classObj, String value) throws ReflectionException
    {
        if(classObj == null)
            throw new NullPointerException("The class object must be provided");
        
        if(value != null)
        {
            return parsePrimitive(classObj, value);
        }
        else
        {
            try
            {
                return classObj.newInstance();
            }
            catch(InstantiationException ex)
            {
                throw new ReflectionException("The class \"" + classObj.getCanonicalName() + "\" must be instantiable through a zero-argument constructor", ex);
            }
            catch(IllegalAccessException ex)
            {
                throw new ReflectionException("The constructor of the class \"" + classObj.getCanonicalName() + "\" is not accesible", ex);
            }
        }
    }
    
    private static Object[] tryCreateVarargsArray(Class varargType, Object[] inputArgs, int startIndex)
    {
        try
        {
            Object[] varArgs = (Object[])Array.newInstance(varargType, inputArgs.length - startIndex);
            System.arraycopy(inputArgs, startIndex, varArgs, 0, inputArgs.length - startIndex);
            
            return varArgs;
        }
        catch(ArrayStoreException ex)
        {
            return null;
        }
    }
    
    public static Object createObject(Class classObj, Object[] argsObjs) throws ReflectionException
    {
        if(classObj == null)
            throw new NullPointerException("The provided class to instantiate cannot be null");
        
        if(argsObjs == null)
            argsObjs = new Object[0];
        
        Constructor[] constructors = classObj.getConstructors();
        HashMap<Constructor, Object[]> findMap = new HashMap<>(constructors.length);
        
        for(int i = 0; i < constructors.length; i++)
        {
            Constructor currConst = constructors[i];
            Class[] constArgs = currConst.getParameterTypes();
            int constArgsCount = constArgs.length;
            
            if(currConst.isVarArgs())
            {
                Class varArgType = constArgs[constArgs.length - 1].getComponentType();
                Object[] varArgs = tryCreateVarargsArray(varArgType, argsObjs, constArgs.length - 1);
                
                if(varArgs == null)
                    continue;
                
                Object[] finalArgs = new Object[constArgsCount];
                System.arraycopy(argsObjs, 0, finalArgs, 0, constArgsCount - 1);
                finalArgs[constArgsCount - 1] = varArgs;
                findMap.put(currConst, finalArgs);
                
                /*if(constArgsCount == 1)
                {
                    findMap.put(currConst, argsObjs);
                }
                else if(constArgsCount > 1 && argsObjs.length >= constArgsCount)
                {
                    
                    
                    if(varArgType.isAssignableFrom(argsObjs[constArgs.length - 1].getClass()))
                    {
                        Object[] finalArgs = new Object[constArgsCount];
                        System.arraycopy(argsObjs, 0, finalArgs, 0, constArgsCount - 1);
                    
                        Object[] varArgs = (Object[])Array.newInstance(varArgType, argsObjs.length - constArgsCount + 1);
                        System.arraycopy(argsObjs, constArgsCount - 1, varArgs, 0, argsObjs.length - constArgsCount + 1);
                        finalArgs[constArgsCount - 1] = varArgs;

                        findMap.put(currConst, finalArgs);
                    }
                }*/
            }
            else if(constArgsCount == argsObjs.length)
            {
                boolean compatible = true;
                
                for(int j = 0; j < constArgsCount && compatible; j++)
                {
                    if(!constArgs[j].isAssignableFrom(argsObjs[j].getClass()))
                        compatible = false;
                }
                
                if(!compatible)
                    continue;
                
                findMap.put(currConst, argsObjs);
            }
        }
        
        Object created = null;
        
        for(Constructor constructor : findMap.keySet())
        {
            try
            {
                created = constructor.newInstance(findMap.get(constructor));
                break;
            }
            catch(InstantiationException | IllegalAccessException | IllegalArgumentException ex)
            {
            }
            catch(InvocationTargetException ex)
            {
                throw new ReflectionException(ex.getCause());
            }
        }
        
        if(created == null)
            throw new ReflectionException("The class '" + classObj.getCanonicalName() + "' do not have a constructor with such class arguments");
        
        return created;
    }
    
    /**
     * Creates a new object from a custom constructor.
     * @param classObj The class of the object to instantiate.
     * @param argsClassObjs An array of classes that represent the constructor 
     *  to use.
     * @param argsObjs An array of objects to use for calling the constructor.
     * @return The instantiated object.
     * @throws NullPointerException if the class to instantiate is null.
     * @throws ReflectionException if a reflection-related error occurs.
     */
    public static Object createObject(Class classObj, Class[] argsClassObjs, Object[] argsObjs) throws ReflectionException
    {
        if(classObj == null)
            throw new NullPointerException("The provided class to instantiate cannot be null");
        
        Constructor constructor = null;
        
        try
        {
            constructor = classObj.getConstructor(argsClassObjs);
        }
        catch(NoSuchMethodException ex)
        {
            throw new ReflectionException("The class '" + classObj.getCanonicalName() + "' do not have a constructor with such class arguments", ex);
        }
        catch(SecurityException ex)
        {
            throw new ReflectionException(ex);
        }
        
        try
        {
            return constructor.newInstance(argsObjs);
        }
        catch(InstantiationException ex)
        {
            throw new ReflectionException("The specified class is not instantiable", ex);
        }
        catch(IllegalAccessException ex)
        {
            throw new ReflectionException("The specified constructor is not accesible", ex);
        }
        catch(IllegalArgumentException ex)
        {
            throw new ReflectionException("The provided argument set is not compatible with the constructor provided", ex);
        }
        catch(InvocationTargetException ex)
        {
            throw new ReflectionException(ex.getCause());
        }
    }
    
    /**
     * Creates an array object.
     * @param className The class of the array.
     * @param length The array length.
     * @param arrayDepth The dimension depth of the array.
     * @return The array object.
     * @throws ReflectionException If the data to generate the array is incorrect. 
     */
    public static Object createArray(String className, int length, int arrayDepth) throws ReflectionException
    {
        if(className == null)
            throw new NullPointerException("The provided class to instantiate cannot be null");
        
        return createArray(getClassFromString(className, arrayDepth), length);
    }
    
    /**
     * Creates an array object.
     * @param classObj The class of the array.
     * @param length The array length.
     * @return The array object.
     * @throws ReflectionException If the data to generate the array is incorrect. 
     */
    public static Object createArray(Class classObj, int length) throws ReflectionException
    {
        if(classObj == null)
            throw new NullPointerException("The provided class cannot be null");
        
        try
        {
            return Array.newInstance(classObj, length);
        }
        catch(NegativeArraySizeException ex)
        {
            throw new ReflectionException(ex);
        }
    }
    
    /**
     * Gets the most specific class (the one that is the same or subclass of 
     * all) from a non-void set of classes.
     * @param classesToCheck The set of classes to check.
     * @return The most specific class object, or null there are zero elements 
     *  in the set, or at least one class is in a different class hierarchy 
     *  regarding from the others.
     */
    public static Class getMostSpecificClass(Class[] classesToCheck)
    {
        if(classesToCheck == null)
            return null;
            
        int classesToCheckCount = classesToCheck.length;
        
        if(classesToCheckCount == 0)
            return null;
        
        Class mostSpecificClass = null;
        
        for(int i = 0; i < classesToCheckCount; i++)
        {
            Class toCheckChildClass = classesToCheck[i];

            // If the current class to check is the same as the current
            // most specific, we don't need to check it
            if(toCheckChildClass == mostSpecificClass)
                continue;

            // We asume that the currently checked class is the most 
            // specific, until we proof the opposite
            boolean isMostSpecific = true;

            for(int j = 0; j < classesToCheckCount && isMostSpecific; j++)
            {
                if(i == j)
                    continue;

                Class parentClass = classesToCheck[j];

                if(parentClass == toCheckChildClass)
                    continue;

                // It is necessary only one case such that a class is not a 
                // parent type of the currently checked class to not consider
                // the latter as the most specific
                if(!parentClass.isAssignableFrom(toCheckChildClass))
                    isMostSpecific = false;
            }

            // We do the final check
            if(isMostSpecific)
                mostSpecificClass = toCheckChildClass;
        }
        
        return mostSpecificClass;
    }

    private ReflectionTools()
    {
    }
}