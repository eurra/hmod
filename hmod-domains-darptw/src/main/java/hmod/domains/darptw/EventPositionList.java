
package hmod.domains.darptw;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class EventPositionList
{
    private final List<EventPosition> positions;

    public EventPositionList()
    {
        positions = new ArrayList<>();
    }
    
    public EventPositionList(int size)
    {
        positions = new ArrayList<>(size);
    }
    
    public void addPosition(EventPosition pos)
    {
        positions.add(Objects.requireNonNull(pos, "null position"));
    }
    
    public EventPosition getPositionAt(int pos) throws IndexOutOfBoundsException
    {
        return positions.get(pos);
    }
    
    public void clear()
    {
        positions.clear();
    }
    
    public int count()
    {
        return positions.size();
    }
}
