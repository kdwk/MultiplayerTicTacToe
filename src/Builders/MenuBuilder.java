package Builders;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A class to allow building a JMenu using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class MenuBuilder {
    private String id;
    private String label;
    private ArrayList<JMenuItem> menuItems = new ArrayList<>();
    private ActionListener actionListener = null;

    /**
     * Constructs a new MenuBuilder instance
     */
    public MenuBuilder() {}

    /**
     * Constructs a new MenuBuilder instance and registers the JMenu
     * @param id The ID of the JMenu
     */
    public MenuBuilder(String id) {this.id = id;}

    /**
     * Sets the label of the JMenu
     * @param label The desired label of the JMenu
     * @return This MenuBuilder instance
     */
    public MenuBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * Adds a JMenuItem to the JMenu
     * @param menuItem The JMenuItem to be added
     * @return This MenuBuilder instance
     */
    public MenuBuilder add(JMenuItem menuItem) {
        this.menuItems.add(menuItem);
        return this;
    }

    /**
     * Sets the ActionListener of all the JMenuItems in the JMenu. For example, if you would like
     * to handle events on the app level and have implemented the ActionListener 
     * interface for the app, pass the app to this method. All events generated 
     * by the JMenuItems will then be forwarded to the app
     * @param actionListener A class that implements the ActionListener interface
     * and to which events from this JButton will be forwarded to
     * @return This MenuBuilder instance
     */
    public MenuBuilder itemsActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * Returns the JMenu with all specified properties
     * @return The JMenu with all specified properties
     */
    public JMenu build() {
        JMenu menu = new JMenu(this.label);
        for (JMenuItem menuItem: this.menuItems) {
            menuItem.addActionListener(this.actionListener);
            menu.add(menuItem);
        }
        if (this.id != null) {
            Components.reg(this.id, menu);
        }
        return menu;
    }
}
