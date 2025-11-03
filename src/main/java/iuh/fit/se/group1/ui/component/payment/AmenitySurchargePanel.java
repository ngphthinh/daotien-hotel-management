package iuh.fit.se.group1.ui.component.payment;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AmenitySurchargePanel extends JPanel {

    private JPanel pnlAmenities;
    private JPanel pnlSurcharges;
    private JLabel lblTotalAmenity;
    private JLabel lblTotalSurcharge;

    private BigDecimal totalAmenity = BigDecimal.ZERO;
    private BigDecimal totalSurcharge = BigDecimal.ZERO;

    private final DecimalFormat moneyFormat = new DecimalFormat("#,### VND");

    public AmenitySurchargePanel() {
        initComponents();
        initializeGlue();
    }

    private void initializeGlue() {
        pnlAmenities.add(Box.createVerticalGlue());
        pnlSurcharges.add(Box.createVerticalGlue());
    }

    private void initComponents() {
        setBackground(Color.WHITE);

        // ======= PANEL DỊCH VỤ =======
        JLabel lblAmenityTitle = new JLabel("Dịch vụ");
        lblAmenityTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAmenityTitle.setForeground(new Color(131,176,212));

        pnlAmenities = new JPanel();
        pnlAmenities.setBackground(Color.WHITE);
        pnlAmenities.setLayout(new BoxLayout(pnlAmenities, BoxLayout.Y_AXIS));
        pnlAmenities.setPreferredSize(new Dimension(280, 70));
        pnlAmenities.add(createPanelEmpty("Chưa có dịch vụ nào"));

        lblTotalAmenity = new JLabel("Tổng: 0 VND");
        lblTotalAmenity.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalAmenity.setForeground(new Color(90, 90, 90));
        lblTotalAmenity.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel amenityWrapper = new JPanel(new BorderLayout(0, 4));
        amenityWrapper.setBackground(Color.WHITE);
        amenityWrapper.add(lblAmenityTitle, BorderLayout.NORTH);
        amenityWrapper.add(pnlAmenities, BorderLayout.CENTER);
        amenityWrapper.add(lblTotalAmenity, BorderLayout.SOUTH);

        // ======= PANEL PHỤ PHÍ =======
        JLabel lblSurchargeTitle = new JLabel("Phụ phí");
        lblSurchargeTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSurchargeTitle.setForeground(new Color(131,176,212));

        pnlSurcharges = new JPanel();
        pnlSurcharges.setBackground(Color.WHITE);
        pnlSurcharges.setLayout(new BoxLayout(pnlSurcharges, BoxLayout.Y_AXIS));
        pnlSurcharges.setPreferredSize(new Dimension(280, 70));
        pnlSurcharges.add(createPanelEmpty("Chưa có phụ phí nào"));

        lblTotalSurcharge = new JLabel("Tổng: 0 VND");
        lblTotalSurcharge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalSurcharge.setForeground(new Color(90, 90, 90));
        lblTotalSurcharge.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel surchargeWrapper = new JPanel(new BorderLayout(0, 4));
        surchargeWrapper.setBackground(Color.WHITE);
        surchargeWrapper.add(lblSurchargeTitle, BorderLayout.NORTH);
        surchargeWrapper.add(pnlSurcharges, BorderLayout.CENTER);
        surchargeWrapper.add(lblTotalSurcharge, BorderLayout.SOUTH);

        // ======= Layout chính (2 cột song song) =======
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(amenityWrapper, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                        .addComponent(surchargeWrapper, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(amenityWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(surchargeWrapper, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    // ===== Panel rỗng =====
    private JPanel createPanelEmpty(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        label.setForeground(new Color(160, 160, 160));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(260, 40));
        return panel;
    }

    // ===== Thêm dịch vụ =====
    public void addAmenity(JComponent comp, BigDecimal price) {
        removeEmptyPanel(pnlAmenities);
        int position = pnlAmenities.getComponentCount() - 1;
        if (position < 0) position = 0;
        pnlAmenities.add(comp, position);
        pnlAmenities.add(Box.createVerticalStrut(6), position + 1);
        adjustPanelSize(pnlAmenities);
        totalAmenity = totalAmenity.add(price);
        updateTotals();
    }

    // ===== Thêm phụ phí =====
    public void addSurcharge(JComponent comp, BigDecimal price) {
        removeEmptyPanel(pnlSurcharges);
        int position = pnlSurcharges.getComponentCount() - 1;
        if (position < 0) position = 0;
        pnlSurcharges.add(comp, position);
        pnlSurcharges.add(Box.createVerticalStrut(6), position + 1);
        adjustPanelSize(pnlSurcharges);
        totalSurcharge = totalSurcharge.add(price);
        updateTotals();
    }

    // ===== Cập nhật tổng =====
    private void updateTotals() {
        lblTotalAmenity.setText("Tổng: " + moneyFormat.format(totalAmenity));
        lblTotalSurcharge.setText("Tổng: " + moneyFormat.format(totalSurcharge));
    }

    // ===== Tự tăng chiều cao =====
    private void adjustPanelSize(JPanel panel) {
        int count = (panel.getComponentCount() - 1) / 2;
        int baseHeight = 70;
        int itemHeight = (count <= 1) ? 0 : 30;
        int height = baseHeight + (count * itemHeight);

        panel.setPreferredSize(new Dimension(280, height));
        panel.revalidate();
        panel.repaint();
        revalidate();
        repaint();
    }

    private void removeEmptyPanel(JPanel panel) {
        if (panel.getComponentCount() == 2 && panel.getComponent(0) instanceof JPanel) {
            panel.remove(0);
        }
    }

    // ===== Reset lại =====
    public void clearAll() {
        pnlAmenities.removeAll();
        pnlSurcharges.removeAll();
        pnlAmenities.add(createPanelEmpty("Chưa có dịch vụ nào"));
        pnlAmenities.add(Box.createVerticalGlue());
        pnlSurcharges.add(createPanelEmpty("Chưa có phụ phí nào"));
        pnlSurcharges.add(Box.createVerticalGlue());
        pnlAmenities.setPreferredSize(new Dimension(280, 70));
        pnlSurcharges.setPreferredSize(new Dimension(280, 70));
        totalAmenity = BigDecimal.ZERO;
        totalSurcharge = BigDecimal.ZERO;
        updateTotals();
        revalidate();
        repaint();
    }

    // ===== Getter =====
    public JPanel getPnlAmenities() {
        return pnlAmenities;
    }

    public JPanel getPnlSurcharges() {
        return pnlSurcharges;
    }
}
