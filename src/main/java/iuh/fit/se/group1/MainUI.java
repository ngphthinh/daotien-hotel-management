package iuh.fit.se.group1;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    private JPanel containerPanel;
    private JButton btnAddRoom;
    private int roomCount = 1;

    public MainUI() {
        setTitle("Quản lý phòng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(new Color(245, 245, 245));

        // Thêm phòng đầu tiên
        containerPanel.add(new RoomCardPanel("Phòng 01"));
        containerPanel.add(Box.createVerticalStrut(10));

        // Nút thêm phòng
        btnAddRoom = new JButton("+ Thêm phòng");
        btnAddRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddRoom.addActionListener(e -> addNewRoom());

        containerPanel.add(btnAddRoom);

        JScrollPane scroll = new JScrollPane(containerPanel);
        scroll.setBorder(null);
        setContentPane(scroll);
    }

    private void addNewRoom() {
        roomCount++;
        RoomCardPanel newRoom = new RoomCardPanel(String.format("Phòng %02d", roomCount));

        int index = containerPanel.getComponentCount() - 1; // vị trí trước nút "Thêm phòng"
        containerPanel.add(newRoom, index);
        containerPanel.add(Box.createVerticalStrut(10), index + 1);

        containerPanel.revalidate();
        containerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}

