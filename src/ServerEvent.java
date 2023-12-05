import java.io.Serializable;

/**
 * An enum class to store all possible event types of a ServerEvent
 * @see ServerEvent
 */
enum ServerEventType {Assign, RefuseConnection, BeginGame, Put, Win, ClearBoard, OpponentLeft}

/**
 * A class to store the content information of a server event
 */
public class ServerEvent implements Serializable {
	/**
	 * Type of the event
	 */
    public ServerEventType eventType;
    /**
     * Player the event is sent by
     */
    public Player player;
    /**
     * To which Player this ServerEvent should be sent to
     */
    public Player responseTarget = Player.None;
    /**
     * (Whenever makes sense) The cell where the event happened
     */
    public String cell;
    
    private ServerEvent(ServerEventType eventType, Player responseTarget) {
        this.responseTarget = responseTarget;
        this.eventType = eventType;
    }
    private ServerEvent(ServerEventType eventType, Player player, Player responseTarget) {
        this.responseTarget = responseTarget;
        this.eventType = eventType;
        this.player = player;
    }
    private ServerEvent(ServerEventType eventType, Player player, String cell, Player responseTarget) {
        this.responseTarget = responseTarget;
        this.eventType = eventType;
        this.player = player;
        this.cell = cell;
    }
    /**
     * Construct a new ServerEvent of ServerEventType Win
     * @param player The Player who won, None if tie
     * @return The newly created ServerEvent
     */
    public static ServerEvent newWin(Player player) {
        return new ServerEvent(ServerEventType.Win, player, Player.Both);
    }
    /**
     * Construct a new ServerEvent of ServerEventType Put
     * @param player The Player who made the move
     * @param cell The cell on the board
     * @return The newly created ServerEvent
     */
    public static ServerEvent newPut(Player player, String cell) {
        return new ServerEvent(ServerEventType.Put, player, cell, Player.Both);
    }
    /**
     * Construct a new ServerEvent of ServerEventType Assign
     * @param player The designator of the newly connected player
     * @return The newly created ServerEvent
     */
    public static ServerEvent newAssign(Player player) {
        return new ServerEvent(ServerEventType.Assign, player, player);
    }
    /**
     * Construct a new ServerEvent of ServerEventType Assign
     * @param player The response target of this ServerEvent
     * @return The newly created ServerEvent
     */
    public static ServerEvent newRefuseConnection(Player player) {
        return new ServerEvent(ServerEventType.RefuseConnection, player);
    }
    /**
     * Construct a new ServerEvent of ServerEventType BeginGame
     * @return The newly created ServerEvent
     */
    public static ServerEvent newBeginGame() {
        return new ServerEvent(ServerEventType.BeginGame, Player.Both);
    }
    /**
     * Construct a new ServerEvent of ServerEventType ClearBoard
     * @return The newly created ServerEvent
     */
    public static ServerEvent newClearBoard() {
        return new ServerEvent(ServerEventType.ClearBoard, Player.Both);
    }
    /**
     * Construct a new ServerEvent of ServerEventType OpponentLeft
     * @return The newly created ServerEvent
     */
    public static ServerEvent newOpponentLeft() {
        return new ServerEvent(ServerEventType.OpponentLeft, Player.Both);
    }
}