
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public class EventPositionHandler
{
    private EventPosition position;

    EventPositionHandler()
    {
    }
        
    public void selectEventPosition(EventPosition pos)
    {
        if(pos == null)
            throw new NullPointerException("Null position");

        this.position = pos;
    }

    public EventPosition getSelectedPosition() throws IllegalStateException
    {
        if(position == null)
            throw new IllegalStateException("No event position has been selected");

        return position;
    }
}
