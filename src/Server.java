import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import Builders.ArrayListBuilder;

class Session {
    private HashMap<String, Player> cells = new HashMap<>();
    private int connected = 0;
    private Player current = Player.X;

    Session() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                cells.put(i + "-" + j, Player.None);
            }
        }
        this.renderCells();
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

    private void renderCells() {
        // for (int i=1; i<=3; i++) {
        // for (int j=1; j<=3; j++) {
        // String id = i+"-"+j;
        // switch (this.cells.get(id)) {
        // case X:
        // Components.<JButton>get(id).setText("X");
        // break;

        // case O:
        // Components.<JButton>get(id).setText("O");
        // break;

        // case None:
        // Components.<JButton>get(id).setText("");
        // break;

        // default:
        // break;
        // }
        // }
        // }
    }

    public Player get(String id) {
        return this.cells.get(id);
    }

    public Player put(Player player, String cell) {
        this.cells.put(cell, player);
        Player winner = this.checkWin();
        return winner;
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
            return Player.X;
            // this.actionListener.actionPerformed(new ActionEvent(this,
            // ActionEvent.ACTION_PERFORMED, "XWon"));
        } else if (triplets.stream().map(triplet -> this.tripletBelongsTo(triplet))
                .anyMatch(player -> player == Player.O)) {
            return Player.O;
            // this.actionListener.actionPerformed(new ActionEvent(this,
            // ActionEvent.ACTION_PERFORMED, "OWon"));
        } else if (this.isFull()) {
            return Player.None;
            // this.actionListener.actionPerformed(new ActionEvent(this,
            // ActionEvent.ACTION_PERFORMED, "Tie"));
        } else {
            return null;
        }
    }

    public Player connect() {
        System.out.println(this.connected);
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
    ConcurrentHashMap<Player, ObjectOutputStream> clients = new ConcurrentHashMap<>();

    class Reply implements Runnable {
        static Queue<ServerEvent> responseQueue = new ArrayDeque<ServerEvent>();

        Reply() {
            Thread replyThread = new Thread(this);
            replyThread.setName("reply");
            replyThread.start();
        }

        public void run() {
            System.out.println("Reply thread started");
            while (true) {
                ServerEvent response = Reply.responseQueue.poll();
                if (response != null) {
                    System.out.println("Polled " + response.eventType.toString() + " event.");
                    try {
                        switch (response.responseTarget) {
                            case X:
                            case O:
                                clients.get(response.player).writeObject(response);
                                clients.get(response.player).flush();
                                break;
                            case Both:
                                clients.get(Player.X).writeObject(response);
                                clients.get(response.player).flush();
                                clients.get(Player.O).writeObject(response);
                                clients.get(response.player).flush();
                                break;
                            default:
                                break;
                        }
                        System.out.println(response.eventType.toString() + " event sent");
                        Thread.sleep(50);
                    } catch (Exception e) {e.printStackTrace();}
                }
            }
        }
    }

    class AcceptClientInputThread implements Runnable {
        Socket socket;

        AcceptClientInputThread(Socket socket) {
            this.socket = socket;
            try {
                this.socket.setKeepAlive(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            Thread serverThread = new Thread(this);
            serverThread.start();
        }

        void decide(ClientEvent event, ObjectOutputStream objectOutputStream) {
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
                            clients.put(player, objectOutputStream);
                            break;
                    }
                    Reply.responseQueue.add(response);
                    break;

                case Put:
                    Player winner = session.put(event.player, event.cell);
                    System.out.println(event.cell + ": " + event.player.toString());
                    if (winner != null) {
                        System.out.println(winner + " won!");
                        response = ServerEvent.newWin(winner);
                    } else {
                        response = ServerEvent.newPut(event.player, event.cell);
                    }
                    Reply.responseQueue.add(response);
                    break;

                default:
                    break;
            }
        }

        public void run() {
            try {
                BufferedOutputStream outputStream = new BufferedOutputStream(this.socket.getOutputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                System.out.println("b");
                BufferedInputStream inputStream = new BufferedInputStream(this.socket.getInputStream());
                System.out.println("c1");
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                System.out.println("c2");
                while (true) {
                    try {
                        ClientEvent event = (ClientEvent) (objectInputStream.readObject());
                        System.out.println("Event received!");
                        if (event != null) {
                            System.out.println("It's a " + event.eventType.toString() + " event!");
                            this.decide(event, objectOutputStream);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Server() {
        System.out.println("Server up and running");
        this.session = new Session();
        System.out.println("Session created");
        try {
            this.serverSocket = new ServerSocket(5001);
            System.out.println("Listening to port 5001");
            System.out.println("a");
            new Reply();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("b");
                new AcceptClientInputThread(socket);
                System.out.println("c");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
