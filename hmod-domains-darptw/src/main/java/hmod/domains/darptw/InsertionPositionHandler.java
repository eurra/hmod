
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class InsertionPositionHandler
{
    private InsertPosition position;
    
    public void selectPosition(InsertPosition pos)
    {
        if(pos == null)
            throw new NullPointerException("Null position");

        this.position = pos;
    }

    public InsertPosition getSelectedPosition() throws IllegalStateException
    {
        if(position == null)
            throw new NullPointerException("No pickup position has been selected");

        return position;
    }
}
