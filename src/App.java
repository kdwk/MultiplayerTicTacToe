// Programmed for JRE 20.0.2

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import Builders.*;

/**
 * A class to deal with incoming ServerEvents from the Server
 * @see ServerEvent
 */
class Receiver implements Runnable {
	
    Socket socket;
    App app;
    
    /**
     * Constructs a new Receiver instance 
     * with references to an App instance 
     * and a Socket
     * @param app The App instance
     * @param socket The Socket connected to the Server
     * @see App
     * @see Server
     */
    Receiver(App app, Socket socket) {
        this.app = app;
        this.socket = socket;
        Thread receiveThread = new Thread(this);
        receiveThread.setName("receive");
        receiveThread.start();
    }
    
    /**
     * Construct the input stream. Sets up a loop to continually listen to 
     * ServerEvents from the input stream
     * @see ServerEvent
     */
    public void run() {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(this.socket.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            while (true) {
                try {
                    ServerEvent event = (ServerEvent) (objectInputStream.readObject());
                    if (event != null) {
                        System.out.println(event.eventType + " event received");
                        app.receive(event);
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception f) {
                    }
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

/**
 * A class to store a queue of outgoing messages to be sent 
 * and methods to send them
 */
class Sender implements Runnable {
	
	private Socket socket;
	/**
	 * A send queue for ClientEvents waiting to be sent
	 * @see ClientEvent
	 */
    public static Queue<ClientEvent> sendEventQueue = new ArrayDeque<ClientEvent>();
    
    /**
     * Constructs a new Sender instance.
     * @param socket The Socket connected to the Server
     */
    Sender(Socket socket) {
        this.socket = socket;
        System.out.println("a");
        Thread sendThread = new Thread(this);
        sendThread.setName("send");
        sendThread.start();
    }
    
    /**
     * Construct the output stream. Sets up a loop to continually 
     * poll the send queue for new ClientEvents to be sent
     */
    public void run() {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(this.socket.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            while (true) {
                try {
                    ClientEvent event = Sender.sendEventQueue.poll();
                    if (event != null) {
                        objectOutputStream.writeObject(event);
                        objectOutputStream.flush();
                        System.out.println(event.eventType.toString() + " event sent");
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception f) {
                    }
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

/**
 * A class to store information of the main app, UI code and button actions
 * Implements the ActionListener and ClientInterface interfaces
 * @see ClientInterface
 */
public class App implements ActionListener, ClientInterface {
	
	/**
	 * The designator of the player controlling this App instance
	 */
    Player designator;
    /**
     * The designator of the opponent
     */
    Player opponent;
    /**
     * Whether the game has ended
     */
    boolean gameHasEnded = false;
    /**
     * Whether this App is connected to the Server as a Client
     * @see Server
     * @see ClientInterface
     */
    boolean isConnected = false;

    /**
     * Constructs the main UI of the app using a custom, declarative abstraction
     * layer over Java Swing
     * 
     * @return A JFrame containing all Components in the UI, with all initial states
     *         and properties set
     * @see FrameBuilder
     * @see BoxBuilder
     * @see MenuBuilder
     * @see ButtonBuilder
     * @see TextFieldBuilder
     */
    JFrame view() {
        return new FrameBuilder("MainFrame")
                .name("Tic Tac Toe")
                .size(300, 350)
                .menuBar(new MenuBarBuilder()
                        .add(new MenuBuilder()
                                .label("Control")
                                .add(new JMenuItem("Exit"))
                                .itemsActionListener(this)
                                .build())
                        .add(new MenuBuilder()
                                .label("Help")
                                .add(new JMenuItem("Instructions"))
                                .itemsActionListener(this)
                                .build())
                        .build())
                .add(new BoxBuilder()
                        .layoutAxis(BoxLayout.PAGE_AXIS)
                        .add(new BoxBuilder()
                                .layoutAxis(BoxLayout.LINE_AXIS)
                                .add("HeaderLabel", new JLabel("Enter your player name..."))
                                .maxSize(150, 50)
                                .build())
                        .add(new GridBuilder()
                                .dimensions(3, 3)
                                .add(new ButtonBuilder("1-1")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("1-2")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("1-3")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("2-1")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("2-2")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("2-3")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("3-1")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("3-2")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .add(new ButtonBuilder("3-3")
                                        .preferredSize(50, 50)
                                        .enabled(false)
                                        .actionListener(this)
                                        .build())
                                .build())
                        .add(new BoxBuilder()
                                .layoutAxis(BoxLayout.LINE_AXIS)
                                .add(new TextFieldBuilder("NameInputField")
                                        .preferredSize(100, 20)
                                        .build())
                                .add(new ButtonBuilder("Submit")
                                        .text("Submit")
                                        .actionListener(this)
                                        .build())
                                .build())
                        .build())
                .closeOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
                .onClose(() -> {
                    this.send(ClientEvent.newDisconnect(this.designator));
                })
                .build();
    }

    /**
     * Set up responses to each button click event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        switch (event.getActionCommand()) {
            case "Submit":
                this.send(ClientEvent.newConnect());
                break;

            case "Instructions":
                String message = "Criteria for a valid move:\n- The move is not occupied by any mark.\n- The move is made in the player’s turn.\n- The move is made within the 3 x 3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n- Player 1 wins.\n- Player 2 wins. - Draw.";
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), message, "Instructions",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Exit":
                this.send(ClientEvent.newDisconnect(this.designator));
                new Timer().schedule(new TimerTask() {
                    public void run() {System.exit(0);}
                }, 250);
                break;

            default:
                if (Components.<JButton>get(event.getActionCommand()).getText() == "") {
                    this.send(ClientEvent.newPut(this.designator, event.getActionCommand()));
                }
                break;
        }
    }
    
    /**
     * Sets up 
     * how to deal with each type of incoming ServerEvents from 
     * the Server. Required by the ClientInterface interface.
     * @see ServerEvent
     * @see Server
     * @see ClientInterface
     */
    public void receive(ServerEvent event) {
        switch (event.eventType) {
            case Assign:
                this.isConnected = true;
                this.designator = event.player;
                switch (this.designator) {
                    case X:
                        this.opponent = Player.O;
                        break;
                    case O:
                        this.opponent = Player.X;
                        break;
                    default:
                        break;
                }
                System.out.println(this.designator.toString());
                String name = Components.<JTextField>get("NameInputField").getText();
                Components.<JTextField>get("NameInputField").setEnabled(false);
                Components.<JFrame>get("MainFrame").setTitle("Tic Tac Toe--Player: " + name);
                Components.<JButton>get("Submit").setEnabled(false);
                Components.<JLabel>get("HeaderLabel").setText("WELCOME " + name);
                break;

            case RefuseConnection:
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"),
                        "Server is full.", "Could Not Connect to Server",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case BeginGame:
                this.gameHasEnded = false;
                if (this.designator == Player.X) {
                    for (int i = 1; i <= 3; i++) {
                        for (int j = 1; j <= 3; j++) {
                            Components.<JButton>get(i + "-" + j).setEnabled(true);
                        }
                    }
                }
                break;

            case Put:
                System.out.println(event.player.toString() + event.cell);
                Components.<JButton>get(event.cell).setText(event.player.toString());
                if (event.player == this.designator) {
                    for (int i = 1; i <= 3; i++) {
                        for (int j = 1; j <= 3; j++) {
                            Components.<JButton>get(i + "-" + j).setEnabled(false);
                        }
                    }
                    Components.<JLabel>get("HeaderLabel").setText("Valid move,\nwait for your opponent.");
                } else {
                    if (!this.gameHasEnded) {
                        for (int i = 1; i <= 3; i++) {
                            for (int j = 1; j <= 3; j++) {
                                Components.<JButton>get(i + "-" + j).setEnabled(true);
                            }
                        }
                        Components.<JLabel>get("HeaderLabel").setText("Your opponent has moved,\nnow is your turn");
                    }
                }
                break;

            case Win:
                this.gameHasEnded = true;
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {
                        Components.<JButton>get(i + "-" + j).setEnabled(false);
                    }
                }
                int reply = -1;
                if (event.player == this.designator) {
                    reply = JOptionPane.showConfirmDialog(Components.<JFrame>get("MainFrame"),
                            "Congratulations. You won. Do you want to play again?", "Round over",
                            JOptionPane.YES_NO_OPTION);
                } else if (event.player == this.opponent) {
                    reply = JOptionPane.showConfirmDialog(Components.<JFrame>get("MainFrame"),
                            "You lost. Do you want to play again?", "Round over", JOptionPane.YES_NO_OPTION);
                } else {
                    reply = JOptionPane.showConfirmDialog(Components.<JFrame>get("MainFrame"),
                            "It's a draw. Do you want to play again?", "Round over", JOptionPane.YES_NO_OPTION);
                }
                if (reply == JOptionPane.YES_OPTION) {
                    this.send(ClientEvent.newRestartGame());
                } else if (reply != -1) {
                    this.send(ClientEvent.newDisconnect(this.designator));
                    new Timer().schedule(new TimerTask() {
                        public void run() {System.exit(0);}
                    }, 250);
                }
                break;

            case ClearBoard:
                for (Window dialog : Components.<JFrame>get("MainFrame").getOwnedWindows()) {
                    dialog.setVisible(false);
                }
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {
                        Components.<JButton>get(i + "-" + j).setText("");
                    }
                }
                break;

            case OpponentLeft:
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), "Game ends. One player left.",
                        "Message", JOptionPane.INFORMATION_MESSAGE);
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {
                        Components.<JButton>get(i + "-" + j).setEnabled(false);
                    }
                }
                break;

            default:
                break;
        }
    }
    
    /**
     * Sets up how to send a ClientEvent message to the Server.
     * Required by the ClientInterface interface
     * @see ClientEvent
     * @see ClientInterface
     * @see Server
     */
    public void send(ClientEvent event) {
        Sender.sendEventQueue.add(event);
    }

    /**
     * Constructor of the App class. 
     * Binds to the 127.0.0.1:5001 port.
     * Creates an instance of both the Sender and Receiver
     * Creates the UI and sets it to visible
     */
    App() {
        try {
            Socket socket = new Socket("127.0.0.1", 5001);
            new Sender(socket);
            new Receiver(this, socket);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        view()
                .setVisible(true);
    }

    /**
     * Creates a new instance of the App
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new App();
    }
}
