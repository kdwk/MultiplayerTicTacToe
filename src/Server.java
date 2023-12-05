import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Builders.ArrayListBuilder;

class Session {
    private HashMap<String, Player> cells = new HashMap<>();
    private int connected = 0;

    Session() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                cells.put(i + "-" + j, Player.None);
            }
        }
    }

    private Player tripletBelongsTo(ArrayList<String> triplet) {
        Player player1 = this.cells.get(triplet.get(0));
        Player player2 = this.cells.get(triplet.get(1));
        Player player3 = this.cells.get(triplet.get(2));
        if (player1 == player2 && player2 == player3 && player3 == Player.X) {
            return Player.X;
        } else if (player1 == player2 && player2 == player3 && player3 == Player.O) {
            return Player.O;
        } else {
            return Player.None;
        }
    }

    private boolean isCellOccupied(String id) {
        switch (this.cells.get(id)) {
            case None:
                return false;

            default:
                return true;
        }
    }

    private boolean isFull() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (!this.isCellOccupied(i + "-" + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Player checkWin() {
        ArrayList<ArrayList<String>> triplets = new ArrayListBuilder<ArrayList<String>>(
                new ArrayListBuilder<String>("1-1", "2-2", "3-3").build(),
                new ArrayListBuilder<String>("1-3", "2-2", "3-1").build(),
                new ArrayListBuilder<String>("1-1", "1-2", "1-3").build(),
                new ArrayListBuilder<String>("2-1", "2-2", "2-3").build(),
                new ArrayListBuilder<String>("3-1", "3-2", "3-3").build(),
                new ArrayListBuilder<String>("1-1", "2-1", "3-1").build(),
                new ArrayListBuilder<String>("1-2", "2-2", "3-2").build(),
                new ArrayListBuilder<String>("1-3", "2-3", "3-3").build()).build();
        if (triplets.stream().map(triplet -> this.tripletBelongsTo(triplet)).anyMatch(player -> player == Player.X)) {
            System.out.println("X won!");
            return Player.X;
        } else if (triplets.stream().map(triplet -> this.tripletBelongsTo(triplet))
                .anyMatch(player -> player == Player.O)) {
            System.out.println("O won!");
            return Player.O;
        } else if (this.isFull()) {
            System.out.println("It's a tie!");
            return Player.None;
        } else {
            return null;
        }
    }

    public Player get(String id) {
        return this.cells.get(id);
    }

    public Player put(Player player, String cell) {
        this.cells.put(cell, player);
        Player winner = this.checkWin();
        return winner;
    }

    public boolean isReadyToBeginGame() {
        if (this.connected == 2) {
            return true;
        } else {
            return false;
        }
    }

    public Player connect() {
        Player player = Player.None;

        switch (this.connected) {
            case 0:
                player = Player.X;
                break;

            case 1:
                player = Player.O;
                break;

            default:
                player = Player.None;
                break;
        }

        this.connected += 1;
        switch (player) {
            case None:
                System.out.println("Connection refused.");
                break;

            default:
                System.out.println("Client is connected. Designator " + player.toString());
                break;
        }
        return player;
    }
}

public class Server {
    ServerSocket serverSocket;
    Session session;
    ArrayList<AcceptClientInputThread> acceptClientInputThreads = new ArrayList<>();
    int resetSessionConsentCount = 0;

    void broadcastReply(ServerEvent reply) {
        for (AcceptClientInputThread thread : this.acceptClientInputThreads) {
            thread.sendReply(reply);
        }
    }

    class AcceptClientInputThread implements Runnable {
        Socket socket;
        ObjectOutputStream outputStream;

        AcceptClientInputThread(Socket socket) {
            acceptClientInputThreads.add(this);
            this.socket = socket;
            try {
                this.socket.setKeepAlive(true);
            } catch (SocketException e) {e.printStackTrace();}
            Thread serverThread = new Thread(this);
            serverThread.start();
        }

        ServerEvent process(ClientEvent event) {
            ServerEvent response = null;
            switch (event.eventType) {
                case Connect:
                    System.out.println("A client has requested to connect");
                    Player player = session.connect();
                    switch (player) {
                        case None:
                            response = ServerEvent.newRefuseConnection(player);
                            break;
                        default:
                            response = ServerEvent.newAssign(player);
                            break;
                    }
                    if (session.isReadyToBeginGame()) {
                        broadcastReply(ServerEvent.newBeginGame());
                    }
                    break;

                case Disconnect:
                    System.out.println("A player has left!");
                    switch (event.player) {
                        case X:
                            acceptClientInputThreads.get(1).sendReply(ServerEvent.newOpponentLeft());
                            break;
                        case O:
                            acceptClientInputThreads.get(0).sendReply(ServerEvent.newOpponentLeft());
                            break;
                        default:
                            break;
                    }
                    new Timer().schedule(new TimerTask() {
                        public void run() {System.exit(0);}
                    }, 250);
                    break;

                case Put:
                    Player winner = session.put(event.player, event.cell);
                    System.out.println(event.cell + ": " + event.player.toString());
                    if (winner != null) {
                        System.out.println(winner + " won!");
                        broadcastReply(ServerEvent.newWin(winner));
                    }
                    response = ServerEvent.newPut(event.player, event.cell);
                    break;
                
                case RestartGame:
                    resetSessionConsentCount += 1;
                    if (resetSessionConsentCount == 2) {
                        resetSessionConsentCount = 0;
                        session = new Session();
                        broadcastReply(ServerEvent.newClearBoard());
                        response = ServerEvent.newBeginGame();
                    }
                    break;

                default:
                    break;
            }
            return response;
        }

        public void sendReply(ServerEvent reply) {
            try {
                this.outputStream.writeObject(reply);
                this.outputStream.flush();
                System.out.println(reply.eventType.toString() + " event sent");
            } catch (Exception e) {e.printStackTrace();}
        }

        public void run() {
            try {
                BufferedOutputStream outputStream = new BufferedOutputStream(this.socket.getOutputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                this.outputStream = objectOutputStream;
                BufferedInputStream inputStream = new BufferedInputStream(this.socket.getInputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                while (true) {
                    try {
                        ClientEvent event = (ClientEvent) (objectInputStream.readObject());
                        System.out.println("Event received");
                        if (event != null) {
                            System.out.println("It's a " + event.eventType.toString() + " event");
                            ServerEvent response = this.process(event);
                            if (response.responseTarget == Player.Both) {
                                broadcastReply(response);
                            } else {
                                this.sendReply(response);
                            }
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception f) {
                        }
                        continue;
                    }
                }
            } catch (Exception e) {e.printStackTrace();}
        }
    }

    Server() {
        System.out.println("Server up and running");
        this.session = new Session();
        System.out.println("Session created");
        try {
            this.serverSocket = new ServerSocket(5001);
            System.out.println("Listening to port 5001");
            while (true) {
                Socket socket = serverSocket.accept();
                new AcceptClientInputThread(socket);
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
