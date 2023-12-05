import java.io.Serializable;

/**
 * An enum class to store all possible ClientEventTypes of a ClientEvent
 */
enum ClientEventType {Connect, Put, RestartGame, Disconnect}

/**
 * A class to store the content information of a ClientEvent
 */
public class ClientEvent implements Serializable {
	/**
	 * Type of the event
	 */
    ClientEventType eventType;
    /**
     * Player the event is sent by
     */
    Player player;
    /**
     * (Whenever makes sense) The cell where the event happened
     */
    String cell;
    
    private ClientEvent(ClientEventType eventType, Player player, String cell) {
        this.eventType = eventType;
        this.player = player;
        this.cell = cell;
    }
    private ClientEvent(ClientEventType eventType, Player player) {
        this.eventType = eventType;
        this.player = player;
    }
    private ClientEvent(ClientEventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Construct a new ClientEvent of ClientEventType Put
     * @param player The Player who made the move
     * @param cell The cell on the board
     * @return The newly created ClientEvent
     */
    public static ClientEvent newPut(Player player, String cell) {
        return new ClientEvent(ClientEventType.Put, player, cell);
    }
    /**
     * Construct a new ClientEvent of ClientEventType Connect
     * @return The newly created ClientEvent
     */
    public static ClientEvent newConnect() {
        return new ClientEvent(ClientEventType.Connect);
    }
    /**
     * Construct a new ClientEvent of ClientEventType RestartGame
     * @return The newly created ClientEvent
     */
    public static ClientEvent newRestartGame() {
        return new ClientEvent(ClientEventType.RestartGame);
    }
    /**
     * Construct a new ClientEvent of ClientEventType Disconnect
     * @param player The Player who disconnected
     * @return The newly created ClientEvent
     */
    public static ClientEvent newDisconnect(Player player) {
        return new ClientEvent(ClientEventType.Disconnect, player);
    }
}
