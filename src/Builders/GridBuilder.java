package Builders;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GridBuilder {
    private JPanel panel = new JPanel();

    private GridLayout layout = new GridLayout();

    public GridBuilder() {}

    public GridBuilder(String id) {
        Components.reg(id, this.panel);
    }

    public GridBuilder dimensions(int rows, int cols) {
        this.layout.setColumns(cols);
        this.layout.setRows(rows);
        return this;
    }

    public GridBuilder gaps(int vgap, int hgap) {
        this.layout.setHgap(hgap);
        this.layout.setVgap(vgap);
        return this;
    }

    public GridBuilder add(Component component) {
        JPanel componentPanel = new JPanel();
        componentPanel.add(component);
        this.panel.add(componentPanel);
        return this;
    }

    public GridBuilder add(String id, Component component) {
        Components.reg(id, component);
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
    public GridBuilder layoutAxis(int axis) {
        this.panel.setLayout(new BoxLayout(this.panel, axis));
        return this;
    }

    /**
     * Sets the x-alignment of the JPanel
     * @param alignment Component.{BOTTOM,TOP,LEFT,RIGHT,CENTER}_ALIGNMENT
     * @return This BoxBuilder instance
     */
    public GridBuilder alignX(float alignment) {
        this.panel.setAlignmentX(alignment);
        return this;
    }

    /**
     * Sets the y-alignment of the JPanel
     * @param alignment Component.{BOTTOM,TOP,LEFT,RIGHT,CENTER}_ALIGNMENT
     * @return This BoxBuilder instance
     */
    public GridBuilder alignY(float alignment) {
        this.panel.setAlignmentY(alignment);
        return this;
    }

    /**
     * Sets the background colour of the JPanel
     * @param color The desired Color
     * @return This BoxBuilder instance
     */
    public GridBuilder background(Color color) {
        this.panel.setBackground(color);
        return this;
    }

    /**
     * Sets the maximum size of the JPanel
     * @param width Maximum width
     * @param height Maximum height
     * @return This BoxBuilder instance
     */
    public GridBuilder maxSize(int width, int height) {
        this.panel.setMaximumSize(new Dimension(width, height));
        return this;
    }

    /**
     * Sets the minimum size of the JPanel
     * @param width Minimum width
     * @param height Minimum height
     * @return This BoxBuilder instance
     */
    public GridBuilder minSize(int width, int height) {
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
    public GridBuilder preferredSize(int width, int height) {
        this.panel.setPreferredSize(new Dimension(width, height));
        return this;
    }

    public JPanel build() {
        this.panel.setLayout(this.layout);
        return this.panel;
    }
}
