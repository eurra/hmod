
package hmod.core;

/**
 * Defines an step, which must determine the next step to be executed within an
 * algorithmic structure. A step can be considered a modular process of a flow 
 * diagram that implements the related algorithm.
 * @author Enrique Urra C.
 */
public interface Step
{
    Step resolveNext() throws AlgorithmException;
}
