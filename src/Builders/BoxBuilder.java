package Builders;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * A class to allow building a JPanel with BoxLayout using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class BoxBuilder {

    private JPanel panel = new JPanel();
    
    /**
     * Constructs a new BoxBuilder, with a JPanel with BoxLayout with PAGE_AXIS by default
     */
    public BoxBuilder() {
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.PAGE_AXIS));
    }
    
    /**
     * Construct a new BoxBuilder, with a JPanel with BoxLayout with PAGE_AXIS by default, 
     * and register it in the global Components registry
     * @param id
     */
    public BoxBuilder(String id) {
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.PAGE_AXIS));
        Components.reg(id, this.panel);
    }

    /**
     * Registers a Component and adds it, wrapped in a JPanel, into this Box
     * @param id The ID of the Component, for registration purposes
     * @param component The Component to be added
     * @return This BoxBuilder instance
     */
    public BoxBuilder add(String id, Component component) {
        JPanel componentPanel = new JPanel();
        componentPanel.add(component);
        this.panel.add(componentPanel);
        Components.reg(id, component);
        return this;
    }

    /**
     * Adds a Component, wrapped in a JPanel, into this Box
     * @param component The Component to be added
     * @return This BoxBuilder instance
     */
    public BoxBuilder add(Component component) {
        JPanel componentPanel = new JPanel();
        componentPanel.add(component);
        this.panel.add(componentPanel);
        return this;
    }

    /**
     * Sets the layout axis of the BoxLayout used by the JPanel
     * @param axis BoxLayout.{LINE,PAGE,X,Y}_AXIS
     * @return This BoxBuilder instance
     */
    public BoxBuilder layoutAxis(int axis) {
        this.panel.setLayout(new BoxLayout(this.panel, axis));
        return this;
    }

    /**
     * Sets the x-alignment of the JPanel
     * @param alignment Component.{BOTTOM,TOP,LEFT,RIGHT,CENTER}_ALIGNMENT
     * @return This BoxBuilder instance
     */
    public BoxBuilder alignX(float alignment) {
        this.panel.setAlignmentX(alignment);
        return this;
    }

    /**
     * Sets the y-alignment of the JPanel
     * @param alignment Component.{BOTTOM,TOP,LEFT,RIGHT,CENTER}_ALIGNMENT
     * @return This BoxBuilder instance
     */
    public BoxBuilder alignY(float alignment) {
        this.panel.setAlignmentY(alignment);
        return this;
    }

    /**
     * Sets the background colour of the JPanel
     * @param color The desired Color
     * @return This BoxBuilder instance
     */
    public BoxBuilder background(Color color) {
        this.panel.setBackground(color);
        return this;
    }

    /**
     * Sets the maximum size of the JPanel
     * @param width Maximum width
     * @param height Maximum height
     * @return This BoxBuilder instance
     */
    public BoxBuilder maxSize(int width, int height) {
        this.panel.setMaximumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the minimum size of the JPanel
     * @param width Minimum width
     * @param height Minimum height
     * @return This BoxBuilder instance
     */
    public BoxBuilder minSize(int width, int height) {
        this.panel.setMinimumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the preferred size of the JPanel. This should be used instead of setSize()
     * as that method does not work on JPanels with a LayoutManager, like BoxLayout here
     * @param width Preferred width
     * @param height Preferred height
     * @return This BoxBuilder instance
     */
    public BoxBuilder preferredSize(int width, int height) {
        this.panel.setPreferredSize(new Dimension(width, height));
        return this;
    }

    /**
     * Returns the JPanel with BoxLayout with all the specified properties
     * @return The JPanel with BoxLayout with all the specified properties
     */
    public JPanel build() {
        return this.panel;
    }
}
