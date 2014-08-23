
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class SimpleAlgorithm extends AbstractAlgorithm
{
    private final Step startStep;

    public SimpleAlgorithm(Step startStep)
    {
        this.startStep = startStep;
    }

    @Override
    protected void launch()
    {
        runStep(startStep);
    }
}
