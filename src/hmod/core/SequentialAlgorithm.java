
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class SequentialAlgorithm extends AbstractAlgorithm
{
    private final Step[] subSteps;

    public SequentialAlgorithm(Step... steps)
    {
        this.subSteps = new Step[steps.length];
        System.arraycopy(steps, 0, subSteps, 0, steps.length);
    }

    @Override
    protected void launch()
    {
        for(int i = 0; i < subSteps.length; i++)
        {
            if(subSteps[i] != null)
                runStep(subSteps[i]);
        }
    }
}
