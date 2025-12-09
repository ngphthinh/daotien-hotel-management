/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.DenominationLabel;
import iuh.fit.se.group1.repository.*;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.message.*;
import iuh.fit.se.group1.ui.component.modal.ConfirmInfoModal;
import iuh.fit.se.group1.ui.component.shift.Money;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.Color;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CloseShift extends javax.swing.JPanel {
    private static final Logger log = LoggerFactory.getLogger(CloseShift.class);
    private EmployeeShiftService employeeShiftService;
    private DenominationDetailService denominationDetailService;
    private final ShiftCloseService shiftCloseService;
    private EmployeeShift currentEmployeeShift;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private ConfirmInfoModal confirmModal;

    // Timer
    private Timer autoRefreshTimer;
    private BigDecimal openingCash = new BigDecimal("5000000");

    private Runnable onCloseShiftSuccess;

    public CloseShift() {
        this.employeeShiftService = new EmployeeShiftService();
        this.denominationDetailService = new DenominationDetailService();
        this.shiftCloseService = new ShiftCloseService();
        this.confirmModal = new ConfirmInfoModal();
        initComponents();
        configureTextFields();
        initIcons();
        initMoneyLabels();
        clearForm();
        setupConfirmModal();
        setupAutoRefresh();
    }

    /**
     *  THIẾT LẬP TIMER TỰ ĐỘNG CẬP NHẬT MỖI 3 GIÂY
     */
    private void setupAutoRefresh() {
        autoRefreshTimer = new Timer(3000, e -> {
            if (currentEmployeeShift != null) {
                refreshRevenueData();
            }
        });
        autoRefreshTimer.start();
        log.info("Auto-refresh timer started (3 seconds interval)");
    }

    /**
     *  CẬP NHẬT LẠI DOANH THU VÀ TÍNH CHÊNH LỆCH TỰ ĐỘNG
     */
    private void refreshRevenueData() {
        try {
            // Lấy lại tổng doanh thu mới nhất từ database
            BigDecimal newRevenue = employeeShiftService.getTotalCashRevenueForShift(
                    currentEmployeeShift.getEmployeeShiftId()
            );
            if (!newRevenue.equals(totalRevenue)) {
                log.info("Revenue changed: {} -> {}",
                        formatCurrency(totalRevenue),
                        formatCurrency(newRevenue));

                totalRevenue = newRevenue;
                txtSystem.setText(formatCurrency(totalRevenue));
                calculateDifference();
            }

        } catch (Exception e) {
            log.error("Error refreshing revenue data: ", e);
        }
    }

    /**
     * DỪNG TIMER KHI PANEL BỊ XÓA
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (autoRefreshTimer != null && autoRefreshTimer.isRunning()) {
            autoRefreshTimer.stop();
            log.info("Auto-refresh timer stopped");
        }
    }
    private void setupConfirmModal() {
        confirmModal.getBtnConfirm().addActionListener(e -> {
            if (confirmModal.isProcessing()) {
                return;
            }
            confirmModal.setProcessing(true);

            try {
                String username = confirmModal.getUsername();
                String password = confirmModal.getPassword();

                System.out.println("Username result: '" + username + "' (isEmpty: " + username.isEmpty() + ")");
                System.out.println("Password result length: " + password.length() + " (isEmpty: " + password.isEmpty() + ")");

                if (username.isEmpty() || password.isEmpty()) {
                    Message.showMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
                    SwingUtilities.invokeLater(() -> confirmModal.clearFields());
                    return;
                }

                ShiftCloseRepository repository = new ShiftCloseRepository();
                Employee manager = repository.validateManager(username, password);

                if (manager != null) {
                    GlassPanePopup.closePopupLast();
                    confirmModal.clearFields();
                    performCloseShift(manager.getEmployeeId());
                } else {
                    Message.showMessage("Lỗi", "Sai tài khoản hoặc mật khẩu!\nHoặc tài khoản không phải Manager!");
                    SwingUtilities.invokeLater(() -> confirmModal.clearFields());
                }
            } finally {
                Timer resetTimer = new Timer(500, evt -> {
                    confirmModal.setProcessing(false);
                });
                resetTimer.setRepeats(false);
                resetTimer.start();
            }
        });

        confirmModal.getBtnCancel().addActionListener(e -> {
            GlassPanePopup.closePopupLast();
            confirmModal.clearFields();
        });
    }
    private void performCloseShift(Long managerId) {
        Message.showConfirm("Xác nhận đóng ca",
                "Bạn có chắc chắn muốn đóng ca làm việc này?",
                () -> {
                    try {
                        // Dừng timer trước khi đóng ca
                        if (autoRefreshTimer != null) {
                            autoRefreshTimer.stop();
                        }

                        ShiftClose shiftClose = new ShiftClose();
                        shiftClose.setEmployeeShift(currentEmployeeShift);

                        BigDecimal cashInDrawer = parseCurrency(txtReality.getText());
                        shiftClose.setCashInDrawer(cashInDrawer);
                        shiftClose.setTotalRevenue(totalRevenue);
                        shiftClose.setNote(jTextArea1.getText());
                        shiftClose.setManagerId(managerId);

                        LocalDateTime now = LocalDateTime.now();
                        shiftClose.setCreatedAt(now);

                        ShiftClose savedShiftClose = shiftCloseService.saveShiftClose(shiftClose);

                        String formattedTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                        String differenceText = formatCurrency(savedShiftClose.getDifference());

                        String message = String.format(
                                "<html>Đóng ca làm việc thành công!<br>" +
                                        "Thời gian đóng ca: %s<br>" +
                                        "Tổng doanh thu: %s<br>" +
                                        "Tiền trong két: %s<br>" +
                                        "Chênh lệch: %s<br>" +
                                        "<span style='color:red; font-weight:bold;'>NHẤN OK HỆ THỐNG TỰ ĐĂNG XUẤT</span>" +
                                        "</html>",
                                formattedTime,
                                formatCurrency(totalRevenue),
                                formatCurrency(cashInDrawer),
                                differenceText
                        );

                        CustomDialog.showMessage(null, message, "Đóng ca",
                                CustomDialog.MessageType.SUCCESS, 500, 280);

                        clearForm();

                        if (onCloseShiftSuccess != null) {
                            Timer timer = new Timer(0, e -> {
                                onCloseShiftSuccess.run();
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    } catch (Exception ex) {
                        log.error("Error closing shift: ", ex);
                        Message.showMessage("Lỗi", "Có lỗi xảy ra khi đóng ca: " + ex.getMessage());
                    }
                }
        );
    }

    private void configureTextFields() {
        txtMoneyOpenShift.setEditable(false);
        txtReality.setEditable(false);
        txtSystem.setEditable(false);
    }

    private void initIcons() {
        lblTitleMoneyOpenShift.setIcon(FontIcon.of(FontAwesomeSolid.MONEY_BILL_ALT, 20, Color.BLACK));
        lblTitleMoneyOfSafe.setIcon(FontIcon.of(FontAwesomeSolid.COINS, 17, Color.BLACK));
        lblCheckDifference.setIcon(FontIcon.of(FontAwesomeSolid.SHIELD_ALT, 17, Color.BLACK));
        lblNote.setIcon(FontIcon.of(FontAwesomeSolid.PEN, 17, Color.BLACK));
        btnClose.setIcon(FontIcon.of(FontAwesomeSolid.CHECK_CIRCLE, 20, Color.WHITE));
    }

    private void initMoneyLabels() {
        denominationDetailService = new DenominationDetailService();
        List<Long> denominations = denominationDetailService.getAvailableDenominations();
        Money[] moneyPanels = {money1, money2, money3, money4, money5, money6, money7, money8, money9};

        for (int i = 0; i < Math.min(denominations.size(), moneyPanels.length); i++) {
            long denom = denominations.get(i);
            moneyPanels[i].getLblMoney().setText(formatDenomination(denom));
        }
    }

    private String formatDenomination(long value) {
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        return formatter.format(value) + "VND";
    }

    public void setCurrentEmployeeShift(EmployeeShift employeeShift) {
        this.currentEmployeeShift = employeeShift;

        if (employeeShift != null) {
            try {
                EmployeeShift detailedShift = employeeShiftService.getEmployeeShiftWithDetails(
                        employeeShift.getEmployeeShiftId()
                );

                if (detailedShift != null) {
                    displayShiftInfo(detailedShift);
                    txtMoneyOpenShift.setText(formatCurrency(openingCash));
                    setDefaultMoneyDistribution();
                    totalRevenue = employeeShiftService.getTotalCashRevenueForShift(
                            employeeShift.getEmployeeShiftId()
                    );
                    txtSystem.setText(formatCurrency(totalRevenue));

                    setupAutoCalculation();
                    calculateDifference();
                }

            } catch (Exception e) {
                log.error("Error loading shift data: ", e);
                Message.showMessage("Lỗi",
                        "Không thể tải thông tin ca làm việc: " + e.getMessage());
            }
        }
    }

    private void displayShiftInfo(EmployeeShift shift) {
        if (shift.getEmployee() != null) {
            infoShift1.setEmployeeName(shift.getEmployee().getFullName());
            infoShift1.setEmployeeId(String.valueOf(shift.getEmployee().getEmployeeId()));
        }

        if (shift.getShift() != null) {
            String shiftName = shift.getShift().getName();
            String shiftTime = shift.getShift().getStartTime() + " - " + shift.getShift().getEndTime();
            String shiftDate = shift.getShiftDate() != null
                    ? shift.getShiftDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    : "";
            infoShift1.setShiftInfo(shiftName, shiftTime, shiftDate);
        }
    }

    private void setupAutoCalculation() {
        txtReality.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateDifference();
            }
        });

        java.awt.event.KeyAdapter moneyKeyListener = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalCashInDrawer();
            }
        };

        for (Money moneyPanel : getMoneyPanels()) {
            moneyPanel.getTxtQuantity().addKeyListener(moneyKeyListener);
        }
    }

    private Money[] getMoneyPanels() {
        return new Money[]{money1, money2, money3, money4, money5,
                money6, money7, money8, money9};
    }

    private void calculateTotalCashInDrawer() {
        try {
            BigDecimal total = BigDecimal.ZERO;

            total = total.add(calculateMoneyValue(money1, 500000));
            total = total.add(calculateMoneyValue(money2, 200000));
            total = total.add(calculateMoneyValue(money3, 100000));
            total = total.add(calculateMoneyValue(money4, 50000));
            total = total.add(calculateMoneyValue(money5, 20000));
            total = total.add(calculateMoneyValue(money6, 10000));
            total = total.add(calculateMoneyValue(money7, 5000));
            total = total.add(calculateMoneyValue(money8, 2000));
            total = total.add(calculateMoneyValue(money9, 1000));

            lblPrice.setText(formatCurrency(total));
            txtReality.setText(formatCurrency(total));

            calculateDifference();

        } catch (Exception e) {
            log.error("Error calculating total cash: ", e);
        }
    }

    private BigDecimal calculateMoneyValue(Money moneyPanel, long denomination) {
        try {
            String quantityText = moneyPanel.getTxtQuantity().getText();
            if (quantityText != null && !quantityText.trim().isEmpty()) {
                int quantity = Integer.parseInt(quantityText.trim());
                return BigDecimal.valueOf(quantity * denomination);
            }
        } catch (NumberFormatException e) {
            log.debug("Invalid quantity input: {}", e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * TÍNH CHÊNH LỆCH
     */
    private void calculateDifference() {
        try {
            BigDecimal reality = parseCurrency(txtReality.getText());

            // Công thức: Chênh lệch = Tiền thực tế - (Doanh thu + Tiền mở ca)
            BigDecimal difference = reality.subtract(totalRevenue.add(openingCash));

            txtMoneyDifference.setText(formatCurrency(difference));
            if (difference.compareTo(BigDecimal.ZERO) < 0) {
                txtMoneyDifference.setForeground(new Color(255, 51, 0)); // Đỏ - thiếu
            } else if (difference.compareTo(BigDecimal.ZERO) > 0) {
                txtMoneyDifference.setForeground(new Color(0, 153, 0)); // Xanh - thừa
            } else {
                txtMoneyDifference.setForeground(Color.WHITE); // Trắng - khớp
            }

        } catch (Exception e) {
            log.error("Error calculating difference: ", e);
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 VND";
        }
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        return formatter.format(amount) + " VND";
    }

    private BigDecimal parseCurrency(String text) {
        if (text == null || text.isEmpty()) {
            return BigDecimal.ZERO;
        }
        String numberOnly = text.replaceAll("[^\\d]", "");
        if (numberOnly.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(numberOnly);
    }

    public Button getBtnClose() {
        return btnClose;
    }

    public void setBtnClose(Button btnClose) {
        this.btnClose = btnClose;
    }

    public void setOnCloseShiftSuccess(Runnable callback) {
        this.onCloseShiftSuccess = callback;
    }

    public void saveData() {
        if (currentEmployeeShift == null) {
            Message.showMessage("Lỗi", "Không tìm thấy thông tin ca làm việc!");
            return;
        }

        saveDenominationDetails();
        GlassPanePopup.showPopup(confirmModal);
    }

    private void saveDenominationDetails() {
        List<DenominationDetail> details = new ArrayList<>();
        Money[] moneyPanels = {money1, money2, money3, money4, money5, money6, money7, money8, money9};
        DenominationLabel[] labels = {
                DenominationLabel.VND_500000,
                DenominationLabel.VND_200000,
                DenominationLabel.VND_100000,
                DenominationLabel.VND_50000,
                DenominationLabel.VND_20000,
                DenominationLabel.VND_10000,
                DenominationLabel.VND_5000,
                DenominationLabel.VND_2000,
                DenominationLabel.VND_1000
        };

        for (int i = 0; i < moneyPanels.length; i++) {
            try {
                String qtyText = moneyPanels[i].getTxtQuantity().getText();
                if (qtyText != null && !qtyText.trim().isEmpty()) {
                    int quantity = Integer.parseInt(qtyText.trim());
                    if (quantity > 0) {
                        DenominationDetail detail = new DenominationDetail();
                        detail.setDenomination(labels[i]);
                        detail.setQuantity(quantity);
                        detail.setEmployeeShift(currentEmployeeShift);
                        detail.setCreatedAt(java.time.LocalDate.now());
                        details.add(detail);
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid quantity for denomination {}", labels[i]);
            }
        }

        if (!details.isEmpty()) {
            DenominationDetailRepository repo = new DenominationDetailRepository();
            repo.saveBatch(details);
            log.info("Saved {} denomination details", details.size());
        }
    }

    private void setDefaultMoneyDistribution() {
        int[] defaultQuantities = {
                4,   // 500.000 x 4 = 2.000.000
                5,   // 200.000 x 5 = 1.000.000
                11,  // 100.000 x 11 = 1.100.000
                9,   // 50.000 x 9 = 450.000
                9,   // 20.000 x 9 = 180.000
                20,  // 10.000 x 20 = 200.000
                10,  // 5.000 x 10 = 50.000
                5,   // 2.000 x 5 = 10.000
                10   // 1.000 x 10 = 10.000
        };

        Money[] moneyPanels = getMoneyPanels();

        for (int i = 0; i < Math.min(moneyPanels.length, defaultQuantities.length); i++) {
            moneyPanels[i].getTxtQuantity().setText(String.valueOf(defaultQuantities[i]));
        }

        calculateTotalCashInDrawer();
    }

    private void clearForm() {
        txtReality.setText("");
        jTextArea1.setText("");

        setDefaultMoneyDistribution();

        txtMoneyDifference.setText("0 VND");
        txtMoneyDifference.setForeground(Color.WHITE);
        txtMoneyOpenShift.setText(formatCurrency(openingCash));
        txtSystem.setText("0 VND");

        totalRevenue = BigDecimal.ZERO;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        infoShift1 = new iuh.fit.se.group1.ui.component.shift.InfoShift();
        moneyInTheSafe1 = new iuh.fit.se.group1.ui.component.shift.MoneyInTheSafe();
        money1 = new iuh.fit.se.group1.ui.component.shift.Money();
        money2 = new iuh.fit.se.group1.ui.component.shift.Money();
        money3 = new iuh.fit.se.group1.ui.component.shift.Money();
        money4 = new iuh.fit.se.group1.ui.component.shift.Money();
        money5 = new iuh.fit.se.group1.ui.component.shift.Money();
        money6 = new iuh.fit.se.group1.ui.component.shift.Money();
        money7 = new iuh.fit.se.group1.ui.component.shift.Money();
        money8 = new iuh.fit.se.group1.ui.component.shift.Money();
        money9 = new iuh.fit.se.group1.ui.component.shift.Money();
        lblTitleMoneyOfSafe = new javax.swing.JLabel();
        minPanel4 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblTotalPrice = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        atTheEnd1 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblTitleMoneyOpenShift = new javax.swing.JLabel();
        txtMoneyOpenShift = new iuh.fit.se.group1.ui.component.custom.TextField();
        atTheEnd2 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblCheckDifference = new javax.swing.JLabel();
        minPanel1 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblSystem = new javax.swing.JLabel();
        txtSystem = new iuh.fit.se.group1.ui.component.custom.TextField();
        minPanel2 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblReality = new javax.swing.JLabel();
        txtReality = new iuh.fit.se.group1.ui.component.custom.TextField();
        minPanel3 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblMoneyDifference = new javax.swing.JLabel();
        txtMoneyDifference = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        atTheEnd3 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblNote = new javax.swing.JLabel();
        scrNote = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();

        jMenu1.setText("jMenu1");

        setBackground(new java.awt.Color(241, 241, 241));

        headerShift1.setSubTitle("");
        headerShift1.setTitle("Đóng ca làm việc");

        moneyInTheSafe1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitleMoneyOfSafe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitleMoneyOfSafe.setText("Tiền trong két");

        minPanel4.setBackground(new java.awt.Color(153, 153, 153));
        minPanel4.setForeground(new java.awt.Color(255, 255, 255));

        lblTotalPrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPrice.setText("Tổng tiền:");

        lblPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrice.setText("10000000 VND");

        javax.swing.GroupLayout minPanel4Layout = new javax.swing.GroupLayout(minPanel4);
        minPanel4.setLayout(minPanel4Layout);
        minPanel4Layout.setHorizontalGroup(
            minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        minPanel4Layout.setVerticalGroup(
            minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalPrice)
                    .addComponent(lblPrice))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout moneyInTheSafe1Layout = new javax.swing.GroupLayout(moneyInTheSafe1);
        moneyInTheSafe1.setLayout(moneyInTheSafe1Layout);
        moneyInTheSafe1Layout.setHorizontalGroup(
            moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                .addGroup(moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblTitleMoneyOfSafe)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(money1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                            .addComponent(money2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(minPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        moneyInTheSafe1Layout.setVerticalGroup(
            moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleMoneyOfSafe)
                .addGap(17, 17, 17)
                .addComponent(money1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(minPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        atTheEnd1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitleMoneyOpenShift.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitleMoneyOpenShift.setText("Số tiền khi mở ca");

        javax.swing.GroupLayout atTheEnd1Layout = new javax.swing.GroupLayout(atTheEnd1);
        atTheEnd1.setLayout(atTheEnd1Layout);
        atTheEnd1Layout.setHorizontalGroup(
            atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(atTheEnd1Layout.createSequentialGroup()
                        .addComponent(lblTitleMoneyOpenShift)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtMoneyOpenShift, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        atTheEnd1Layout.setVerticalGroup(
            atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleMoneyOpenShift)
                .addGap(20, 20, 20)
                .addComponent(txtMoneyOpenShift, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        atTheEnd2.setBackground(new java.awt.Color(255, 255, 255));

        lblCheckDifference.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblCheckDifference.setText("Kiểm tra chênh lệch");

        minPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblSystem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSystem.setText("Hệ thống:");

        txtSystem.setText("10000000 VND");

        javax.swing.GroupLayout minPanel1Layout = new javax.swing.GroupLayout(minPanel1);
        minPanel1.setLayout(minPanel1Layout);
        minPanel1Layout.setHorizontalGroup(
            minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblSystem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(txtSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        minPanel1Layout.setVerticalGroup(
            minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSystem)
                    .addComponent(txtSystem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        minPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblReality.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblReality.setText("Thực tế:");

        txtReality.setText("14000000 VND");
        txtReality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRealityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout minPanel2Layout = new javax.swing.GroupLayout(minPanel2);
        minPanel2.setLayout(minPanel2Layout);
        minPanel2Layout.setHorizontalGroup(
            minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblReality)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(txtReality, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        minPanel2Layout.setVerticalGroup(
            minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReality)
                    .addComponent(txtReality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        minPanel3.setBackground(new java.awt.Color(0, 0, 0));

        lblMoneyDifference.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoneyDifference.setForeground(new java.awt.Color(255, 255, 255));
        lblMoneyDifference.setText("Số tiền chênh lệch:");

        txtMoneyDifference.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMoneyDifference.setForeground(new java.awt.Color(255, 51, 0));
        txtMoneyDifference.setText("4000000 VND");

        javax.swing.GroupLayout minPanel3Layout = new javax.swing.GroupLayout(minPanel3);
        minPanel3.setLayout(minPanel3Layout);
        minPanel3Layout.setHorizontalGroup(
            minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblMoneyDifference)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMoneyDifference, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        minPanel3Layout.setVerticalGroup(
            minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMoneyDifference)
                    .addComponent(txtMoneyDifference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("-   ");

        javax.swing.GroupLayout atTheEnd2Layout = new javax.swing.GroupLayout(atTheEnd2);
        atTheEnd2.setLayout(atTheEnd2Layout);
        atTheEnd2Layout.setHorizontalGroup(
            atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckDifference)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(minPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(minPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(minPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        atTheEnd2Layout.setVerticalGroup(
            atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckDifference)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(atTheEnd2Layout.createSequentialGroup()
                        .addGroup(atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(37, 37, 37)))
                .addComponent(minPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        atTheEnd3.setBackground(new java.awt.Color(255, 255, 255));

        lblNote.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNote.setText("Ghi chú cho ca làm");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        scrNote.setViewportView(jTextArea1);

        javax.swing.GroupLayout atTheEnd3Layout = new javax.swing.GroupLayout(atTheEnd3);
        atTheEnd3.setLayout(atTheEnd3Layout);
        atTheEnd3Layout.setHorizontalGroup(
            atTheEnd3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNote)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(scrNote, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        atTheEnd3Layout.setVerticalGroup(
            atTheEnd3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNote)
                .addGap(20, 20, 20)
                .addComponent(scrNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        btnClose.setBackground(new java.awt.Color(0, 0, 0));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("Xác nhận đóng ca");
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(moneyInTheSafe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(atTheEnd3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(atTheEnd2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 738, Short.MAX_VALUE)
                            .addComponent(atTheEnd1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(infoShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 1194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(233, 233, 233))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(atTheEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(atTheEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(atTheEnd3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(moneyInTheSafe1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtRealityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRealityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRealityActionPerformed
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        saveData();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd1;
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd2;
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd3;
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private iuh.fit.se.group1.ui.component.shift.InfoShift infoShift1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblCheckDifference;
    private javax.swing.JLabel lblMoneyDifference;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblReality;
    private javax.swing.JLabel lblSystem;
    private javax.swing.JLabel lblTitleMoneyOfSafe;
    private javax.swing.JLabel lblTitleMoneyOpenShift;
    private javax.swing.JLabel lblTotalPrice;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel1;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel2;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel3;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel4;
    private iuh.fit.se.group1.ui.component.shift.Money money1;
    private iuh.fit.se.group1.ui.component.shift.Money money2;
    private iuh.fit.se.group1.ui.component.shift.Money money3;
    private iuh.fit.se.group1.ui.component.shift.Money money4;
    private iuh.fit.se.group1.ui.component.shift.Money money5;
    private iuh.fit.se.group1.ui.component.shift.Money money6;
    private iuh.fit.se.group1.ui.component.shift.Money money7;
    private iuh.fit.se.group1.ui.component.shift.Money money8;
    private iuh.fit.se.group1.ui.component.shift.Money money9;
    private iuh.fit.se.group1.ui.component.shift.MoneyInTheSafe moneyInTheSafe1;
    private javax.swing.JScrollPane scrNote;
    private javax.swing.JLabel txtMoneyDifference;
    private iuh.fit.se.group1.ui.component.custom.TextField txtMoneyOpenShift;
    private iuh.fit.se.group1.ui.component.custom.TextField txtReality;
    private iuh.fit.se.group1.ui.component.custom.TextField txtSystem;
    // End of variables declaration//GEN-END:variables
}
