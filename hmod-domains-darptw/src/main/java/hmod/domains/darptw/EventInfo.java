
package hmod.domains.darptw;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Enrique Urra C.
 */
public class EventInfo
{
    private ServiceRequest request;
    private EventType evType;
    private double at;
    private double slackTime;
    private double drt;
    private int acumLoad;

    public EventInfo(ServiceRequest request, EventType evType, double at, double slackTime, double drt, int acumLoad)
    {
        if(request == null)
            throw new NullPointerException("The provided service request cannot be null");
        
        this.request = request;
        this.evType = evType;
        this.at = at;
        this.slackTime = slackTime;
        this.drt = drt;
        this.acumLoad = acumLoad;
    }

    public ServiceRequest getRequest()
    {
        return request;
    }

    public EventType getEvType()
    {
        return evType;
    }
    
    public double getAT()
    {
        return at;
    }

    public double getSlackTime()
    {
        return slackTime;
    }

    public double getDRT()
    {
        return drt;
    }

    public int getAcumLoad()
    {
        return acumLoad;
    }

    @Override
    public String toString()
    {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
        
        return (slackTime > 0 ? "///st:" + df.format(slackTime) + "/// " : "") +
                "<" + request.getId() + (at > request.getLT() ? "!" : "") + "> " + (drt > 0 ? "(drt:" + df.format(drt) + "|st:" + df.format(request.getServiceTime()) + ") " : "") +
                "[et:" + df.format(request.getET()) + "|at:" + df.format(at) + "|lt:" + df.format(request.getLT()) + "] (l:" + acumLoad + ")";
    }
}
