package Builders;
import javax.swing.*;

import java.awt.*;

/**
 * A class to allow building a JFrame using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class FrameBuilder {
    private JFrame frame = new JFrame();

    /**
     * Constructs a new FrameBuilder instance
     */
    public FrameBuilder() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Constucts a new FrameBuilder instance and registers the JFrame
     * @param id The ID of the JFrame
     */
    public FrameBuilder(String id) {
        Components.reg(id, this.frame);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Sets the name of the JFrame
     * @param title The desired name of the JFrame
     * @return This FrameBuilder instance
     */
    public FrameBuilder name(String name) {
        this.frame.setName(name);
        return this;
    }

    /**
     * Sets the minimum size of the JFrame
     * @param width Minimum width
     * @param height Minimum height
     * @return This FrameBuilder instance
     */
    public FrameBuilder minSize(int width, int height) {
        this.frame.setMinimumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the maximum size of the JFrame
     * @param width Maximum width
     * @param height Maximum height
     * @return This FrameBuilder instance
     */
    public FrameBuilder maxSize(int width, int height) {
        this.frame.setMaximumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the size of the JFrame. Should not be used with pack()
     * @see FrameBuilder#pack()
     * @param width Desired width
     * @param height Desired height
     * @return This FrameBuilder instance
     */
    public FrameBuilder size(int width, int height) {
        this.frame.setSize(width, height);
        return this;
    }

    /**
     * Add a Component to the JFrame
     * @param component The Component to be added
     * @return This FrameBuilder instance
     */
    public FrameBuilder add(Component component) {
        this.frame.add(component);
        return this;
    }

    /**
     * Sets the close operation of the JFrame. If not set, the default is 
     * EXIT_ON_CLOSE
     * @param closeOperation WindowConstants.{DISPOSE,DO_NOTHING,EXIT,HIDE}_ON_CLOSE
     * @return This FrameBuilder instance
     */
    public FrameBuilder closeOperation(int closeOperation) {
        this.frame.setDefaultCloseOperation(closeOperation);
        return this;
    }

    /**
     * Automatically resize the JFrame to just fit all Components. Should 
     * not be used with size()
     * @see FrameBuilder#size(int, int)
     * @return This FrameBuilder instance
     */
    public FrameBuilder pack() {
        this.frame.pack();
        return this;
    }

    /**
     * Adds a JMenuBar with one JMenu to the JFrame
     * @see MenuBuilder
     * @param menu The single JMenu to be added the the JMenuBar
     * @return This FrameBuilder instance
     */
    public FrameBuilder menuBar(JMenuBar menuBar) {
        this.frame.setJMenuBar(menuBar);
        return this;
    }

    /**
     * Returns the JFrame with all specified properties
     * @return The JFrame with all specified properties
     */
    public JFrame build() {
        return this.frame;
    }
}