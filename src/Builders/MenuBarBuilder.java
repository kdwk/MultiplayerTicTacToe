package Builders;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBarBuilder {
    private ArrayList<JMenu> menus = new ArrayList<>();
    private String id;

    public MenuBarBuilder() {}

    public MenuBarBuilder(String id) {this.id = id;}

    public MenuBarBuilder add(JMenu menu) {
        this.menus.add(menu);
        return this;
    }

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
