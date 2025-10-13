package iuh.fit.se.group1;

import javax.swing.*;
import java.awt.*;

public class RoomCardPanel extends JPanel {
    private int borderRadius = 20;
    private Color shadowColor = new Color(0, 0, 0, 30);

    public RoomCardPanel(String title) {
        setOpaque(false);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initUI(title);
    }

    private void initUI(String title) {
        // ----- HEADER -----
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 14));

        JButton btnDelete = new JButton("✖");
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnDelete, BorderLayout.EAST);

        // ----- BODY -----
        JPanel body = new JPanel(new GridLayout(2, 3, 10, 10));
        body.setOpaque(false);

        JComboBox<String> cmbLoaiPhong = new JComboBox<>(new String[]{"Phòng đơn", "Phòng đôi"});
        JTextField txtThoiGianBD = new JTextField("dd/mm/yyyy");
        JTextField txtThoiGianKT = new JTextField("dd/mm/yyyy");
        JTextField txtMaPhong = new JTextField("MP300393");
        JTextField txtGiaPhong = new JTextField("500000");
        JTextField txtSoPhong = new JTextField("101");

        body.add(cmbLoaiPhong);
        body.add(txtThoiGianBD);
        body.add(txtThoiGianKT);
        body.add(txtMaPhong);
        body.add(txtGiaPhong);
        body.add(txtSoPhong);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 5, y = 5, w = getWidth() - 10, h = getHeight() - 10;

        // Shadow
        g2.setColor(shadowColor);
        g2.fillRoundRect(x + 2, y + 2, w, h, borderRadius, borderRadius);

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, w, h, borderRadius, borderRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
