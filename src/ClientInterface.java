/**
 * An interface outlining two methods a client must implement -- receive() and send()
 */
public interface ClientInterface {
	/**
	 * A method clients should implement to deal with incoming ServerEvents
	 * @param event The incoming ServerEvent
	 */
    public void receive(ServerEvent event);
    /**
     * A method clients should implement to send outgoing ClientEvents to the Server
     * @param event The ClientEvent to be sent
     */
    public void send(ClientEvent event);
}
