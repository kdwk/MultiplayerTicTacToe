import java.io.Serializable;

enum ServerEventType {Assign, RefuseConnection, BeginGame, Put, Win}

public class ServerEvent implements Serializable {
    public ServerEventType eventType;
    public Player player;
    public Player responseTarget = Player.None;
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
    public static ServerEvent newWin(Player player) {
        return new ServerEvent(ServerEventType.Win, player, Player.Both);
    }
    public static ServerEvent newPut(Player player, String cell) {
        return new ServerEvent(ServerEventType.Put, player, cell, Player.Both);
    }
    public static ServerEvent newAssign(Player player) {
        return new ServerEvent(ServerEventType.Assign, player, player);
    }
    public static ServerEvent newRefuseConnection(Player player) {
        return new ServerEvent(ServerEventType.RefuseConnection, player);
    }
    public static ServerEvent newBeginGame() {
        return new ServerEvent(ServerEventType.BeginGame, Player.Both);
    }
}