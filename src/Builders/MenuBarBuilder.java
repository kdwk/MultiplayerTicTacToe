package Builders;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * A class to allow building a JMenuBar using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class MenuBarBuilder {
	
    private ArrayList<JMenu> menus = new ArrayList<>();
    private String id;
    
    /**
     * Construct a new MenuBarBuilder instance
     */
    public MenuBarBuilder() {}
    
    /**
     * Construct a new MenuBarBuilder instance 
     * and register it in the global Components registry
     * @param id The ID of the JMenuBar
     */
    public MenuBarBuilder(String id) {this.id = id;}
    
    /**
     * Add a JMenu to the JMenuBar
     * @param menu The JMenu to be added
     * @return This MenuBarBuilder instance
     */
    public MenuBarBuilder add(JMenu menu) {
        this.menus.add(menu);
        return this;
    }
    
    /**
     * Returns the JMenuBar with all the specified properties
     * @return The JMenuBar with all the specified properties
     */
    public JMenuBar build() {
        JMenuBar menuBar = new JMenuBar();
        for (JMenu menu: menus) {
            menuBar.add(menu);
        }
        if (this.id != null) {
            Components.reg(id, menuBar);
        }
        return menuBar;
    }
}
