package iuh.fit.se.group1.ui.component.payment;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

import iuh.fit.se.group1.service.PaymentSuggestionService;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.TextField;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

public class CashPaymentModal extends JPanel {

    // ====== Biến logic ======
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private double totalToPay = 1_250_000; // 💵 ví dụ
    private double moneyGiven = 0;

    // ====== Biến giao diện ======
    private iuh.fit.se.group1.ui.component.custom.Button btnComplete;
    private iuh.fit.se.group1.ui.component.custom.Button btnExit;
    private JLabel lblMoneyChange, lblMoneyGive, lblTitle;
    private JPanel pnlMoney;
    private iuh.fit.se.group1.ui.component.custom.TextField txtMoneyChange, txtMoneyGive;

    public CashPaymentModal(double totalToPay) {
        this.totalToPay = totalToPay;
        initComponents();
        setupUI();
        initSuggestedButtons();
    }

    // ======================= SETUP ==========================
    private void setupUI() {
        setBackground(new Color(129, 129, 129));

        btnExit.setIcon(FontIcon.of(FontAwesomeSolid.TIMES_CIRCLE, 20, Color.WHITE));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setText("");
        btnExit.setOpaque(false);
        btnExit.addActionListener(e -> GlassPanePopup.closePopupLast());

        txtMoneyGive.setText("0 VNĐ");
        txtMoneyChange.setText("0 VNĐ");
    }

    private String formatMoneyShort(long money) {
//        if (money >= 1_000_000) return (money / 1_000_000) + "tr";
//        if (money >= 1_000) return (money / 1_000) + "k";
        DecimalFormat shortDf = new DecimalFormat("#,###");

        return shortDf.format(money);
    }

    public String formatMoney(double value) {
        return df.format(value);
    }

    // ======================= NÚT GỢI Ý ==========================
    private void initSuggestedButtons() {
        pnlMoney.removeAll();
        pnlMoney.setLayout(new GridLayout(2, 4, 10, 10));
        pnlMoney.setBackground(new Color(129, 129, 129));

        PaymentSuggestionService service = new PaymentSuggestionService();
        int totalThousand = (int) Math.ceil(totalToPay / 1000.0);

        java.util.List<Integer> suggestions = service.getPaymentOptions(totalThousand);

        System.out.println(suggestions);

        for (int suggestion : suggestions) {
            long value = suggestion * 1000L;
            JButton btn = createMoneyButton(value);
            pnlMoney.add(btn);
        }

        pnlMoney.revalidate();
        pnlMoney.repaint();
    }

    private JButton createMoneyButton(long value) {
        JButton btn = new JButton(formatMoneyShort(value));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(255, 255, 255));
        btn.setForeground(new Color(40, 40, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        btn.setPreferredSize(new Dimension(80, 45));
        btn.addActionListener(e -> selectSuggestedMoney(value));

        // hiệu ứng hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(34, 197, 94));
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(40, 40, 40));
            }
        });
        return btn;
    }

    private void selectSuggestedMoney(double value) {
        moneyGiven = value;
        txtMoneyGive.setText(df.format(moneyGiven));

        double change = moneyGiven - totalToPay;
        txtMoneyChange.setText(change >= 0 ? df.format(change) : "0 VNĐ");
    }


    public DecimalFormat getDf() {
        return df;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(long totalToPay) {
        this.totalToPay = totalToPay;
    }

    public double getMoneyGiven() {
        return moneyGiven;
    }

    public void setMoneyGiven(long moneyGiven) {
        this.moneyGiven = moneyGiven;
    }

    public Button getBtnComplete() {
        return btnComplete;
    }

    public void setBtnComplete(Button btnComplete) {
        this.btnComplete = btnComplete;
    }

    public Button getBtnExit() {
        return btnExit;
    }

    public void setBtnExit(Button btnExit) {
        this.btnExit = btnExit;
    }

    public JLabel getLblMoneyChange() {
        return lblMoneyChange;
    }

    public void setLblMoneyChange(JLabel lblMoneyChange) {
        this.lblMoneyChange = lblMoneyChange;
    }

    public JLabel getLblMoneyGive() {
        return lblMoneyGive;
    }

    public void setLblMoneyGive(JLabel lblMoneyGive) {
        this.lblMoneyGive = lblMoneyGive;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public JPanel getPnlMoney() {
        return pnlMoney;
    }

    public void setPnlMoney(JPanel pnlMoney) {
        this.pnlMoney = pnlMoney;
    }

    public TextField getTxtMoneyChange() {
        return txtMoneyChange;
    }

    public void setTxtMoneyChange(TextField txtMoneyChange) {
        this.txtMoneyChange = txtMoneyChange;
    }

    public TextField getTxtMoneyGive() {
        return txtMoneyGive;
    }

    public void setTxtMoneyGive(TextField txtMoneyGive) {
        this.txtMoneyGive = txtMoneyGive;
    }

    // ======================= HOÀN TẤT ==========================
    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {
        if (moneyGiven < totalToPay) {
            CustomDialog.showMessage(this,
                    "Khách đưa chưa đủ tiền!",
                    "Thông báo", CustomDialog.MessageType.WARNING, 350, 150);
            return;
        }

        double change = moneyGiven - totalToPay;
        JOptionPane.showMessageDialog(this,
                "✅ Thanh toán hoàn tất!\n"
                        + "Tổng tiền: " + formatMoney(totalToPay) + "\n"
                        + "Khách đưa: " + formatMoney(moneyGiven) + "\n"
                        + "Tiền thối: " + formatMoney(change),
                "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
        GlassPanePopup.closePopupLast();
    }


    // ======================= GIAO DIỆN SWING ==========================
    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblTitle = new JLabel("Thanh toán tiền mặt", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        btnExit = new iuh.fit.se.group1.ui.component.custom.Button();
        btnExit.setBackground(new Color(129, 129, 129));

        // ====== Thêm phần hiển thị tổng tiền ======
        JLabel lblTotalTitle = new JLabel("Tổng cần thanh toán:");
        lblTotalTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalTitle.setForeground(Color.WHITE);

        JLabel lblTotalToPay = new JLabel(df.format(totalToPay));
        lblTotalToPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalToPay.setForeground(new Color(255, 215, 0)); // vàng nổi bật

        lblMoneyGive = new JLabel("Tiền khách đưa:");
        lblMoneyChange = new JLabel("Tiền thối:");

        lblMoneyGive.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMoneyGive.setForeground(Color.WHITE);
        lblMoneyChange.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMoneyChange.setForeground(Color.WHITE);

        txtMoneyGive = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtMoneyGive.setBackground(new Color(129, 129, 129));
        txtMoneyGive.setForeground(Color.WHITE);
        txtMoneyGive.setHorizontalAlignment(JTextField.CENTER);
        txtMoneyGive.setBorderColor(Color.white);
        txtMoneyGive.setFont(new Font("Segoe UI", Font.BOLD, 12));

        txtMoneyChange = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtMoneyChange.setBackground(new Color(129, 129, 129));
        txtMoneyChange.setForeground(Color.WHITE);
        txtMoneyChange.setHorizontalAlignment(JTextField.CENTER);
        txtMoneyChange.setBorderColor(Color.white);
        txtMoneyChange.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlMoney = new JPanel();

        btnComplete = new iuh.fit.se.group1.ui.component.custom.Button();
        btnComplete.setBackground(new Color(249, 115, 22));
        btnComplete.setForeground(Color.WHITE);
        btnComplete.setText("Hoàn tất");
        btnComplete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnComplete.setBorderRadius(5);

        // ===== Layout =====
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlMoney, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnComplete, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                                        // --- Thêm phần tổng tiền ---
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblTotalTitle)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTotalToPay, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblMoneyGive)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtMoneyGive, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblMoneyChange)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtMoneyChange, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(65)
                                .addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnExit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTitle))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // --- Tổng tiền nằm ở đây ---
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTotalTitle)
                                        .addComponent(lblTotalToPay))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMoneyGive)
                                        .addComponent(txtMoneyGive, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMoneyChange)
                                        .addComponent(txtMoneyChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlMoney, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
                                .addGap(27)
                                .addComponent(btnComplete, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(28, Short.MAX_VALUE))
        );
    }

}
