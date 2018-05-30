
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface TextProcessorRegistry
{
    TextProcessorRegistry addProcessor(TextVariableProcessor processor);
    TextProcessorRegistry addProcessors(TextVariableProcessor... processors);
}
