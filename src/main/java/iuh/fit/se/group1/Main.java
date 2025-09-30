package iuh.fit.se.group1;


import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JButton btnSave = new JButton("Save",
                FontIcon.of(FontAwesomeSolid.SAVE, 18));   // FontAwesome

        JButton btnHome = new JButton("Home",
                FontIcon.of(Material.HOME, 20));           // Material

        JPanel panel = new JPanel();
        panel.add(btnSave);
        panel.add(btnHome);

        JFrame frame = new JFrame("Ikonli Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}