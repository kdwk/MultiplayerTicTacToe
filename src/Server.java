import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import Builders.ArrayListBuilder;
import Builders.Components;

class Session {
    private HashMap<String, Player> cells = new HashMap<>();
    private int connected = 0;
    private Player current = Player.X;

    Session() {
        for (int i=1; i<=3; i++) {
            for (int j=1; j<=3; j++) {
                cells.put(i+"-"+j, Player.None);
            }
        }
        this.renderCells();
    }

    private Player tripletBelongsTo(ArrayList<String> triplet) {
        Player player1 = this.cells.get(triplet.get(0));
        Player player2 = this.cells.get(triplet.get(1));
        Player player3 = this.cells.get(triplet.get(2));
        if (player1==player2 && player2==player3 && player3==Player.X) {
            return Player.X;
        } else if (player1==player2 && player2==player3 && player3==Player.O) {
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
        for (int i=1; i<=3; i++) {
            for (int j=1; j<=3; j++) {
                if (!this.isCellOccupied(i+"-"+j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void renderCells() {
        // for (int i=1; i<=3; i++) {
        //     for (int j=1; j<=3; j++) {
        //         String id = i+"-"+j;
        //         switch (this.cells.get(id)) {
        //             case X:
        //                 Components.<JButton>get(id).setText("X");
        //                 break;
                    
        //             case O:
        //                 Components.<JButton>get(id).setText("O");
        //                 break;

        //             case None:
        //                 Components.<JButton>get(id).setText("");
        //                 break;

        //             default:
        //                 break;
        //         }
        //     }
        // }
    }

    public Player get(String id) {
        return this.cells.get(id);
    }

    public void put(String id) {
        this.cells.put(id, this.current);
        this.renderCells();
        switch (this.current) {
            case X:
                this.current = Player.O;
                break;
            
            case O:
                this.current = Player.X;
                break;

            default:
                break;
        }
        this.checkWin();
    }

    private void checkWin() {
        ArrayList<ArrayList<String>> triplets = new ArrayListBuilder<ArrayList<String>>(
                                                        new ArrayListBuilder<String>("1-1", "2-2", "3-3").build(),
                                                        new ArrayListBuilder<String>("1-3", "2-2", "3-1").build(),
                                                        new ArrayListBuilder<String>("1-1", "1-2", "1-3").build(),
                                                        new ArrayListBuilder<String>("2-1", "2-2", "2-3").build(),
                                                        new ArrayListBuilder<String>("3-1", "3-2", "3-3").build(),
                                                        new ArrayListBuilder<String>("1-1", "2-1", "3-1").build(),
                                                        new ArrayListBuilder<String>("1-2", "2-2", "3-2").build(),
                                                        new ArrayListBuilder<String>("1-3", "2-3", "3-3").build()
                                                    ).build();
        if (triplets.stream().map(triplet->this.tripletBelongsTo(triplet)).anyMatch(player->player==Player.X)) {
            System.out.println("X won");
            // this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "XWon"));
        } else if (triplets.stream().map(triplet->this.tripletBelongsTo(triplet)).anyMatch(player->player==Player.O)) {
            System.out.println("O won");
            // this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "OWon"));
        } else if (this.isFull()) {
            System.out.println("Tie");
            // this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Tie"));
        } else {
            System.out.println("Continue");
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
                break;
        }

        this.connected += 1;
        System.out.println("Client is connected. Designator "+player.toString());
        return player;
    }
}

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    Session session;

    Server() {
        System.out.println("Server up and running");
        this.session = new Session();
        System.out.println("Session created");
        try {
            this.serverSocket = new ServerSocket(5001);
            System.out.println("Listening to port 5001");
            this.clientSocket = this.serverSocket.accept();
            System.out.println("a");
            BufferedOutputStream outputStream = new BufferedOutputStream(this.clientSocket.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("b");
            BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());
            System.out.println("c1");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            System.out.println("c2");
            while (true) {
                try {
                    ClientEvent event = (ClientEvent)(objectInputStream.readObject());
                    if (event != null) {
                        switch (event.eventType) {
                            case Connect:
                                System.out.println("A client has requested to connect");
                                Player player = this.session.connect();
                                ServerEvent response = ServerEvent.newAssign(player);
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();
                                System.out.println(response.eventType.toString() + " event sent");
                                break;
                
                            default:
                                break;
                        }
                    }
                    Thread.sleep(50);
                } catch (EOFException e) {
                    Thread.sleep(50);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        new Server();
    }
}
