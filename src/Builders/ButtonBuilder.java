package Builders;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * A class to allow building a JButton using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class ButtonBuilder {
    private JButton button = new JButton();
    private String regID = null;

    /**
     * Constructs a new ButtonBuilder instance
     */
    public ButtonBuilder() {}

    /**
     * Constucts a new ButtonBuilder instance and registers the JButton
     * @param id The ID of the JButton
     */
    public ButtonBuilder(String id) {
        Components.reg(id, this.button);
        this.regID = id;
    }

    /**
     * Sets the text of the JButton
     * @param text The desired text
     * @return This ButtonBuilder instance
     */
    public ButtonBuilder text(String text) {
        this.button.setText(text);
        return this;
    }

    /**
     * Sets whether the JButton is enabled
     * @param enabled Whether the JButton should be enabled
     * @return This BoxBuilder instance
     */
    public ButtonBuilder enabled(boolean enabled) {
        this.button.setEnabled(enabled);
        return this;
    }

    public ButtonBuilder preferredSize(int width, int height) {
        this.button.setPreferredSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the ActionListener of the JButton. For example, if you would like
     * to handle events on the app level and have implemented the ActionListener 
     * interface for the app, pass the app to this method. All events generated 
     * by this JButton will then be forwarded to the app
     * @param actionListener A class that implements the ActionListener interface
     * and to which events from this JButton will be forwarded to
     * @return This ButtonBuilder instance
     */
    public ButtonBuilder actionListener(ActionListener actionListener) {
        if (regID != null) {
            this.button.setActionCommand(this.regID);
        }
        this.button.addActionListener(actionListener);
        return this;
    }

    /**
     * Returns the JButton with all specified properties
     * @return The JButton with all specified properties
     */
    public JButton build() {
        return this.button;
    }
}
