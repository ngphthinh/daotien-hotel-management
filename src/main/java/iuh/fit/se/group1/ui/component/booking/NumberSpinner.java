package iuh.fit.se.group1.ui.component.booking;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NumberSpinner extends JPanel {
    private int value = 1;
    private final JLabel lblValue;
    private final JButton btnMinus;
    private final JButton btnPlus;

    public NumberSpinner() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setBackground(new Color(233,233,233)); // màu nền nhẹ như hình

        btnMinus = createButton(FontIcon.of(FontAwesomeSolid.MINUS, 10, Color.WHITE), new Color(220, 53, 69)); // đỏ
        lblValue = new JLabel(String.format("%02d", value), SwingConstants.CENTER);
        btnPlus = createButton(FontIcon.of(FontAwesomeSolid.PLUS, 10, Color.WHITE), new Color(52, 152, 219)); // xanh

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblValue.setPreferredSize(new Dimension(30, 30));
        lblValue.setForeground(Color.DARK_GRAY);

        add(btnMinus);
        add(lblValue);
        add(btnPlus);

        // hành vi nút
//        btnMinus.addActionListener(e -> setValue(value - 1));
//        btnPlus.addActionListener(e -> setValue(value + 1));
    }

    private JButton createButton(Icon icon, Color bg) {
        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);

        btn.setPreferredSize(new Dimension(19, 19));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        return btn;
    }

    public void setValue(int newValue) {
        if (newValue < 1) return; // chặn nhỏ hơn 1
        this.value = newValue;
        lblValue.setText(String.format("%02d", value));
    }

    public void addMinusActionListener(ActionListener listener) {
        btnMinus.addActionListener(listener);
    }
    public void addPlusActionListener(ActionListener listener) {
        btnPlus.addActionListener(listener);
    }

    public int getValue() {
        return value;
    }

    // test nhanh
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Spinner Demo");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new NumberSpinner());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
