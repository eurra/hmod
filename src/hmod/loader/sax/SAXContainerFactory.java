
package hmod.loader.sax;

import mobs.ObjectBuildFactory;
import mobs.core.ObjectTreeBuilder;
import hmod.parser.AlgorithmParser;
import hmod.parser.AlgorithmParserFactory;
import mobs.ObjectBuildException;

/**
 *
 * @author Enrique Urra C.
 */
public class SAXContainerFactory extends AlgorithmParserFactory
{
    private SAXContainer lastContainer;
    
    @Override
    public AlgorithmParser newParser() throws ObjectBuildException
    { 
        ObjectTreeBuilder builder = ObjectBuildFactory.newInstance().newBuilder();
        
        SAXParseHandler handler = new SAXParseHandler();
        AlgorithmContextManager contextManager = new AlgorithmContextManager();
        handler.registerTagParser(new DefaultTagParser(builder));
        handler.registerTagParser(new StepBasedTagParser(builder, contextManager));
        handler.registerTagParser(new VirtualBeanTagParser(builder, contextManager));
        handler.registerTagParser(new ContextTagParser(contextManager));        
        
        SAXContainer container = new SAXContainer(builder, handler, contextManager);
        
        // TODO: additional parsers autoload
        
        lastContainer = container;
        return container;
    }

    @Override
    public AlgorithmParser getCurrentParser()
    {
        return lastContainer;
    }
}
