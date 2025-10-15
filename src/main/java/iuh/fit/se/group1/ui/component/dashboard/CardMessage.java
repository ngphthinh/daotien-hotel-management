package iuh.fit.se.group1.ui.component.dashboard;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class CardMessage extends JPanel {
    private Color bgColor;
    private Color textColor;
    private Icon icon;
    private String title;
    private String subtitle;
    private int cornerRadius = 20;

    public CardMessage() {
        this.bgColor = new Color(212,110,110);
        this.textColor = new Color(212,110,110);
        this.icon = FontIcon.of(FontAwesomeSolid.WALKING, 20, Color.RED);
        this.title = title;
        this.subtitle = subtitle;
        setOpaque(false);
        setPreferredSize(new Dimension(260, 70));
        init();
    }


    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public CardMessage(Color bgColor, Color textColor, Icon icon, String title, String subtitle) {
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        setOpaque(false);
        setPreferredSize(new Dimension(260, 70));
        init();
    }

    private void init() {
        setLayout(new BorderLayout(10, 0));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(textColor);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLabel.setForeground(Color.WHITE);

        textPanel.add(titleLabel);
        textPanel.add(subLabel);

        add(iconLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        // Bóng
//        g2.setColor(new Color(0, 0, 0, 40));
//        g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        // Nền
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
