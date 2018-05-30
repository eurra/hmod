
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public enum Factor
{
    TRANSIT_TIME ("Transit time"),
    ROUTE_DURATION ("Route duration"),
    SLACK_TIME ("Slack time"),
    RIDE_TIME ("Ride time"),
    EXCESS_RIDE_TIME ("Excess ride time"),
    WAIT_TIME ("Wait time"),
    TIME_WINDOWS_VIOLATION ("Time windows violation"),
    MAXIMUM_ROUTE_DURATION_VIOLATION ("Maximum route duration violation"),
    MAXIMUM_RIDE_TIME_VIOLATION ("Maximum ride time violation");
    
    private final String name;

    private Factor(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
