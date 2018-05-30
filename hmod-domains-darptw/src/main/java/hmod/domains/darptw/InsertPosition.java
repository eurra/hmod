
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class InsertPosition
{
    private final int pickupPosition;
    private final int deliveryPosition;

    public InsertPosition(int pickupPosition, int deliveryPosition)
    {
        this.pickupPosition = pickupPosition;
        this.deliveryPosition = deliveryPosition;
    }

    public int getPickupPosition()
    {
        return pickupPosition;
    }

    public int getDeliveryPosition()
    {
        return deliveryPosition;
    }
}
