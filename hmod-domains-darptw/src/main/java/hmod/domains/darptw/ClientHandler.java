
package hmod.domains.darptw;

/**
 *
 * @author Enrique Urra C.
 */
public final class ClientHandler
{
    private Client client;
        
    public void selectCurrentClient(Client client) throws IllegalArgumentException
    {
        if(client == null)
            throw new NullPointerException("Null client");

        this.client = client;
    }

    public Client getCurrentClient() throws IllegalStateException
    {
        if(client == null)
            throw new NullPointerException("No client has been selected");

        return client;
    }
}
