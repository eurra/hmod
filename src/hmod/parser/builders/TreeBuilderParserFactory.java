
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import hmod.parser.AlgorithmParserException;
import hmod.parser.AlgorithmParserFactory;

/**
 * The factory implementation for the {@link TreeBuilderParser} type.<br>
 * 
 * This factory creates a new {@link BuildHandler} instance to initialize 
 * the parser, and registers on it an {@link OLD_AlgorithmBuilder} instance, which 
 * offers specialized building semantics related to algorithm constructs.
 * 
 * @author Enrique Urra C.
 */
public final class TreeBuilderParserFactory extends AlgorithmParserFactory<TreeBuilderParser>
{
    @Override
    public TreeBuilderParser createParser() throws AlgorithmParserException
    {
        try
        {
            return new TreeBuilderParser();
        }
        catch(BuildException ex)
        {
            throw new AlgorithmParserException(ex);
        }
    }
}
