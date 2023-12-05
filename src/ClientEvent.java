import java.io.Serializable;

enum ClientEventType {Connect, Put, RestartGame, Disconnect}

public class ClientEvent implements Serializable {
    ClientEventType eventType;
    Player player;
    String name;
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

    public static ClientEvent newPut(Player player, String cell) {
        return new ClientEvent(ClientEventType.Put, player, cell);
    }
    public static ClientEvent newConnect() {
        return new ClientEvent(ClientEventType.Connect);
    }
    public static ClientEvent newRestartGame() {
        return new ClientEvent(ClientEventType.RestartGame);
    }
    public static ClientEvent newDisconnect(Player player) {
        return new ClientEvent(ClientEventType.Disconnect, player);
    }
}
