// Programmed for JRE 20.0.2

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.*;

import Builders.*;

class Receive implements Runnable {
    Socket socket;
    App app;

    Receive(App app) {
        this.app = app;
        try {
            this.socket = new Socket("127.0.0.1", 5001);
        } catch (Exception e) {e.printStackTrace();}
        Thread receiveThread = new Thread(this);
        receiveThread.setName("receive");
        receiveThread.start();
    }

    public void run() {
        while (true) {
            try {
                ServerEvent event = (ServerEvent)(new ObjectInputStream(this.socket.getInputStream())).readObject();
                app.receive(event);
                Thread.sleep(100);
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}

class Send implements Runnable {
    public static Queue<ClientEvent> sendEventQueue = new PriorityQueue<ClientEvent>();

    Socket socket;

    Send() {
        System.out.println("a");
        try{
            this.socket = new Socket("127.0.0.1", 5001);
            System.out.println("b");
        } catch (Exception e) {System.out.println("c"); e.printStackTrace(); System.exit(0);}
        Thread sendThread = new Thread(this);
        sendThread.setName("send");
        sendThread.start();
    }

    public void run() {
        while (true) {
            try {
                ClientEvent event = Send.sendEventQueue.poll();
                if (event != null) {
                    PrintWriter writer = new PrintWriter(this.socket.getOutputStream());
                    writer.println(event);
                }
                Thread.sleep(100);
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}




/**
 * A class to store information of the main app, UI code and button actions
 */
public class App implements ActionListener, Client {

    Player designator;

    /**
     * Constructs the main UI of the app using a custom, declarative abstraction layer over Java Swing
     * @return A JFrame containing all Components in the UI, with all initial states and properties set
     * @see FrameBuilder
     * @see BoxBuilder
     * @see MenuBuilder
     * @see ButtonBuilder
     * @see TextFieldBuilder
     */
    JFrame view() {
        return
        new FrameBuilder("MainFrame")
            .name("Tic Tac Toe")
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
            .pack()
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
                String name = Components.<JTextField>get("NameInputField").getText();
                Components.<JTextField>get("NameInputField").setEnabled(false);
                Components.<JFrame>get("MainFrame").setTitle("Tic Tac Toe--Player: "+name);
                Components.<JButton>get("Submit").setEnabled(false);
                Components.<JLabel>get("HeaderLabel").setText("WELCOME "+name);
                // this.session = new Session(this);
                for (int i=1; i<=3; i++) {
                    for (int j=1; j<=3; j++) {
                        Components.<JButton>get(i+"-"+j).setEnabled(true);
                    }
                }
                this.send(ClientEvent.newConnect());
                break;

            case "XWon":
                for (int i=1; i<=3; i++) {
                    for (int j=1; j<=3; j++) {
                        Components.<JButton>get(i+"-"+j).setEnabled(false);
                    }
                }
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), "Congratulations. X won. Do you want to play again?", "Round over", JOptionPane.YES_NO_OPTION);
                break;

            case "OWon":
                for (int i=1; i<=3; i++) {
                    for (int j=1; j<=3; j++) {
                        Components.<JButton>get(i+"-"+j).setEnabled(false);
                    }
                }
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), "Congratulations. X won. Do you want to play again?", "Round over", JOptionPane.YES_NO_OPTION);
                break;

            case "Tie":
                for (int i=1; i<=3; i++) {
                    for (int j=1; j<=3; j++) {
                        Components.<JButton>get(i+"-"+j).setEnabled(false);
                    }
                }
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), "Congratulations. No one won. Do you want to play again?", "Round over", JOptionPane.YES_NO_OPTION);
                break;

            case "Instructions":
                String message = "Criteria for a valid move:\n- The move is not occupied by any mark.\n- The move is made in the player’s turn.\n- The move is made within the 3 x 3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n- Player 1 wins.\n- Player 2 wins. - Draw.";
                JOptionPane.showMessageDialog(Components.<JFrame>get("MainFrame"), message, "Instructions", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Exit":
                System.exit(0);
                break;
            
            default:
                // this.session.put(event.getActionCommand());
                break;
        }
    }

    public void receive(ServerEvent event) {
        switch (event.eventType) {
            case Assign:
                this.designator = event.player;
                System.out.println(this.designator.toString());
                break;
            
            case Win:
                break;
            
            default:
                break;
        }
    }

    public void send(ClientEvent event) {
        Send.sendEventQueue.add(event);
    }
    
    /**
     * Constructor of the App class. Creates the UI and sets it to visible.
     */
    App() {
        try {
        new Send(); // new Receive(this);
        view()
            .setVisible(true);
        } catch (NullPointerException e) {e.printStackTrace(); System.exit(0);}
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
