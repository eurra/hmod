
package hmod.parser.sax;

import mobs.core.SetterFactory;
import mobs.core.ObjectTreeBuilder;
import hmod.core.Operator;
import hmod.core.Step;
import hmod.core.StepFactory;
import mobs.ObjectBuildException;
import hMod.exception.ReflectionException;
import hmod.util.ReflectionTool;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Implements a tag parser based on step entities.
 * @author Enrique Urra C.
 */
class StepBasedTagParser implements SAXTagParser
{
    /**
     * The factory used to create steps.
     */
    private StepFactory stepFactory;
    
    /**
     * An internal algorithm context manager;
     */
    private AlgorithmContextManager contextManager;
    /**
     * The internal builder.
     */
    private ObjectTreeBuilder builder;
    
    /**
     * Constructor.
     * @param contextManager The context manager to use.
     */
    public StepBasedTagParser(ObjectTreeBuilder builder, AlgorithmContextManager contextManager)
    {
        if(contextManager == null)
            throw new NullPointerException("The provided context manager cannot be null");
        
        if(builder == null)
            throw new NullPointerException("The builder cannot be null");
        
        this.builder = builder;
        this.contextManager = contextManager;
        this.stepFactory = new StepFactory();
    }
    
    @Override
    public void startDefinition() throws ObjectBuildException
    {
    }
    
    @Override
    public void parseTagStart(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("directStep"))
            parseDirectStep(attributes.get("id"), attributes.get("desc"), attributes.get("nextRef"), attributes.get("opDef"));        
        else if(tagName.equals("subStep"))
            parseSubStep(attributes.get("id"), attributes.get("desc"), attributes.get("subRef"), attributes.get("nextRef"));
        else if(tagName.equals("booleanStep"))
            parseBooleanStep(attributes.get("id"), attributes.get("desc"), attributes.get("nextTrueRef"), attributes.get("nextFalseRef"), attributes.get("opDef"));        
        else if(tagName.equals("step"))
            parseCustomStep(attributes.get("id"), attributes.get("desc"), attributes.get("class"));
        else if(tagName.equals("operator"))
            parseOperator(attributes.get("id"), attributes.get("desc"), attributes.get("def"), attributes.get("class"));
        else if(tagName.equals("diSetter"))
            parseDataInterfaceSetter(attributes.get("def"), attributes.get("dest"));
    }
    
    private void parseDirectStep(String id, String desc, String nextRef, String opDef) throws ObjectBuildException
    {
        builder.startEntity(id, stepFactory.createDirectOperatorStep(), desc);
        
        if(nextRef != null)
        {
            builder.addSetter("nextRef", SetterFactory.createBeanSetter(StepFactory.DIRECT_NEXT_SETTER)).
                    addReference(nextRef, null);
        }
        
        builder.addSetter("operator", SetterFactory.createBeanSetter(StepFactory.DIRECT_OPERATOR_SETTER));
        
        if(opDef != null)
            parseOperator(null, null, opDef, null);
    }
    
    private void parseSubStep(String id, String desc, String subRef, String nextRef) throws ObjectBuildException
    {
        builder.startEntity(id, stepFactory.createSubStep(), desc);
        
        if(subRef != null)
        {
            builder.addSetter("subRef", SetterFactory.createBeanSetter(StepFactory.SUB_STEP_SETTER)).
                    addReference(subRef, null);
        }
        
        if(nextRef != null)
        {
            builder.addSetter("nextRef", SetterFactory.createBeanSetter(StepFactory.SUB_NEXT_SETTER)).
                    addReference(nextRef, null);
        } 
        
        builder.endEntity();
    }
    
    private void parseBooleanStep(String id, String desc, String nextTrueRef, String nextFalseRef, String opDef) throws ObjectBuildException
    {
        builder.startEntity(id, stepFactory.createBooleanOperatorStep(), desc);
        
        if(nextTrueRef != null)
        {
            builder.addSetter("nextTrueRef", SetterFactory.createBeanSetter(StepFactory.BOOLEAN_NEXT_TRUE_SETTER)).
                    addReference(nextTrueRef, null);
        }
        
        if(nextFalseRef != null)
        {
            builder.addSetter("nextFalseRef", SetterFactory.createBeanSetter(StepFactory.BOOLEAN_NEXT_FALSE_SETTER)).
                    addReference(nextFalseRef, null);
        }
        
        builder.addSetter("operator", SetterFactory.createBeanSetter(StepFactory.BOOLEAN_OPERATOR_SETTER));
        
        if(opDef != null)
            parseOperator(null, null, opDef, null);
    }
    
    protected void parseCustomStep(String id, String desc, String className) throws ObjectBuildException
    {
        if(className == null)
            throw new ObjectBuildException("A class name was expected");
             
        Object obj = null;
        
        try
        {
            obj = ReflectionTool.createObject(className);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        if(!Step.class.isAssignableFrom(obj.getClass()))
            throw new ObjectBuildException("The provided custom step class must be compatible with " + Step.class.getCanonicalName());
                
        builder.startEntity(id, obj, desc);
    }
    
    protected void parseOperator(String id, String desc, String def, String className) throws ObjectBuildException
    {
        if(def == null && className == null)
            throw new ObjectBuildException("A operator definition or class name was expected");
        
        Object obj = null;
        
        try
        {
            if(def != null)
            {
                Class opClass = contextManager.getDefinition(def);

                if(opClass == null)
                    throw new ObjectBuildException("The operation definition id does not exists in the provided context");

                obj = ReflectionTool.createObject(opClass);
            }
            else
            {
               obj = ReflectionTool.createObject(className); 
            }
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        if(!Operator.class.isAssignableFrom(obj.getClass()))
            throw new ObjectBuildException("The provided operator class must be compatible with " + Operator.class.getCanonicalName());
                
        builder.startEntity(id, obj, desc);
    }
    
    protected void parseDataInterfaceSetter(String def, String dest) throws ObjectBuildException
    {
        if(def == null)
            throw new ObjectBuildException("A definition id of a data interface setter was expected");
        
        if(dest == null)
            throw new ObjectBuildException("A destination id was expected");

        Object currentEntity = builder.getCurrentObject();
        
        if(currentEntity == null)
            throw new ObjectBuildException("No entity is being processed to configure a data interface");
        
        Class diSetterClass = contextManager.getDefinition(def);
        Method diSetter = null;
        
        try
        {
            diSetter = diSetterClass.getMethods()[0];
        }
        catch(SecurityException ex)
        {
            throw new ObjectBuildException(ex);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            throw new ObjectBuildException("The specified data interface setter class does not implements a proper method");
        }

        builder.addSetter(null, SetterFactory.createBeanSetter(diSetter)).
                addReference(dest, null);
    }

    @Override
    public void parseTagEnd(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("directStep") || tagName.equals("booleanStep") || tagName.equals("step") || tagName.equals("operator"))
            builder.endEntity();
        
        if((tagName.equals("directStep") || tagName.equals("booleanStep")) && attributes.containsKey("opDef"))
            builder.endEntity();
    }
    
    @Override
    public void endDefinition() throws ObjectBuildException
    {
    }
    
    @Override
    public String[] getParseableTags()
    {
        return new String[] {
            "directStep",
            "subStep",
            "booleanStep",
            "step",
            "operator",
            "diSetter"
        };
    }
}