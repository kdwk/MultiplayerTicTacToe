import java.io.Serializable;

enum ClientEventType {Connect, Put}

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
    }

    public static ClientEvent newPut(Player player, String cell) {
        return new ClientEvent(ClientEventType.Put, player, cell);
    }
    public static ClientEvent newConnect() {
        return new ClientEvent(ClientEventType.Connect);
    }
}
