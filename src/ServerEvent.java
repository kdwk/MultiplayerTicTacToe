import java.io.Serializable;

enum ServerEventType {Assign, Put, Win}

public class ServerEvent implements Serializable {
    public ServerEventType eventType;
    public Player player;
    public String cell;
    private ServerEvent(ServerEventType eventType, Player player) {
        this.eventType = eventType;
        this.player = player;
    }
    private ServerEvent(ServerEventType eventType, Player player, String cell) {
        this.eventType = eventType;
        this.player = player;
        this.cell = cell;
    }
    public static ServerEvent newWin(Player player) {
        return new ServerEvent(ServerEventType.Win, player);
    }
    public static ServerEvent newPut(Player player, String cell) {
        return new ServerEvent(ServerEventType.Put, player, cell);
    }
    public static ServerEvent newAssign(Player player) {
        return new ServerEvent(ServerEventType.Assign, player);
    }
}