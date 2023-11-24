package Builders;
import java.awt.Dimension;
import javax.swing.JTextField;

/**
 * A class to allow building a JTextField using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class TextFieldBuilder {
    private JTextField textfield = new JTextField();

    /**
     * Constructs a new TextFieldBuilder
     */
    public TextFieldBuilder() {}

    /**
     * Constructs a new TextFieldBuilder and registers the JTextField
     * @param id The ID of the JTextField
     */
    public TextFieldBuilder(String id) {Components.reg(id, textfield);}

    /**
     * Sets the text in the JTextField
     * @param text The desired text in the JTextField
     * @return This TextFieldBuilder instance
     */
    public TextFieldBuilder text(String text) {
        this.textfield.setText(text);
        return this;
    }

    /**
     * Sets the preferred size of the JTextField. This should be used instead of setSize()
     * @param width Preferred width
     * @param height Preferred height
     * @return This TextFieldBuilder instance
     */
    public TextFieldBuilder preferredSize(int width, int height) {
        this.textfield.setPreferredSize(new Dimension(width, height));
        return this;
    }

    /**
     * Returns the JTextField with all specified properties
     * @return The JTextField with all specified properties
     */
    public JTextField build() {
        return this.textfield;
    }
}
