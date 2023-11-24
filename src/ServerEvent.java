import java.io.Serializable;

enum ServerEventType {Assign, Put, Win}

public class ServerEvent implements Serializable {
    public ServerEventType eventType;
    public Player player;
    public String name;
    public String cell;
    private ServerEvent(Player player, String cell) {
        this.eventType = ServerEventType.Assign;
        this.player = player;
        this.cell = cell;
    }
    private ServerEvent(Player player) {
        this.eventType = ServerEventType.Win;
        this.player = player;
    }
    private ServerEvent(ServerEventType eventType, String name) {
        this.name = name;
    }
    public static ServerEvent newAssign(Player player) {
        return new ServerEvent(player);
    }
    public static ServerEvent newPut(Player player, String cell) {
        return new ServerEvent(player, cell);
    }
    public static ServerEvent newWin(Player player) {
        return new ServerEvent(player);
    }
}