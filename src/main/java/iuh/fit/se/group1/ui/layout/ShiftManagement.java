/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.entity.Shift;
import iuh.fit.se.group1.repository.ShiftRepository;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.service.EmployeeShiftService;
import iuh.fit.se.group1.service.ShiftService;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.shift.ShiftCard;
import iuh.fit.se.group1.ui.component.shift.ShiftList;
import iuh.fit.se.group1.ui.component.shift.ShiftProfile;
import iuh.fit.se.group1.util.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.imgscalr.Scalr;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;



/**
 *
 * @author THIS PC
 */
public class ShiftManagement extends javax.swing.JPanel {
    private static final Logger log = LoggerFactory.getLogger(ShiftManagement.class);
    private DateChooser dateChooser;
    private ShiftService shiftService;
    private List<Shift> shifts;
    private EmployeeShiftService employeeShiftService;
    private EmployeeService employeeService;

    /**
     * Creates new form ShiftManagement
     */
    public ShiftManagement() {
        initComponents();
        shiftService = new ShiftService();
        employeeShiftService = new EmployeeShiftService();
        employeeService = new EmployeeService();
        loadShiftsFromDatabase();
        setupDateChooser();

        setupShiftCardButtons();
        loadEmployeeShiftsByDate(LocalDate.now());
    }
    public ShiftList getShiftList() {
        return shiftList;
    }
    private void loadShiftsFromDatabase() {
        try {
            // Lấy tất cả shifts từ database
            shifts = shiftService.getAllShifts();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Gán dữ liệu vào các ShiftCard tương ứng
            ShiftCard[] shiftCards = {shiftCard1, shiftCard2, shiftCard3, shiftCard4};
            Color[] colors = {Color.RED, new Color(51, 204, 255), Color.GREEN, Color.yellow};

            for (int i = 0; i < Math.min(shifts.size(), shiftCards.length); i++) {
                Shift shift = shifts.get(i);
                ShiftCard card = shiftCards[i];

                // Set màu header
                card.setHeaderColor(colors[i]);

                // Set tên ca
                card.getLblShiftName().setText(shift.getName());

                // Set thời gian ca làm việc

                String startTime = shift.getStartTime().substring(0,5);
                String endTime = shift.getEndTime().substring(0,5);
                card.getLblTime().setText(startTime + " - " + endTime);
            }

            // Nếu không đủ 4 ca trong database, set giá trị mặc định cho các ca còn lại
            if (shifts.size() < 4) {
                String[] defaultNames = {"CA 01", "CA 02", "CA 03", "CA 04"};
                String[] defaultTimes = {"00:00 - 06:00", "06:00 - 12:00", "12:00 - 18:00", "18:00 - 00:00"};

                for (int i = shifts.size(); i < shiftCards.length; i++) {
                    shiftCards[i].setHeaderColor(colors[i]);
                    shiftCards[i].getLblShiftName().setText(defaultNames[i]);
                    shiftCards[i].getLblTime().setText(defaultTimes[i]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Message.showMessageNoCancel("Lỗi", "Không thể tải danh sách ca làm việc: " + e.getMessage());

            // Fallback: Set giá trị mặc định nếu có lỗi
            setDefaultShiftValues();
        }
    }
    private void loadEmployeeShiftsByDate(LocalDate date) {
        try {
            if (shifts == null || shifts.isEmpty()) return;

            ShiftCard[] shiftCards = {shiftCard1, shiftCard2, shiftCard3, shiftCard4};

            // Load ảnh mặc định
            BufferedImage defaultImage = null;
            try {
                URL defaultImg = getClass().getResource("/images/meomeo.jpg");
                if (defaultImg != null) defaultImage = ImageIO.read(defaultImg);
            } catch (Exception ex) {
                log.error("Error loading default image", ex);
            }

            // Reset toàn bộ ShiftCard về mặc định
            for (ShiftCard card : shiftCards) {
                card.getLblName1().setText("Vui lòng thêm nhân viên");
                card.getLblCode1().setText("Không có mã nhân viên");
                card.getLblName2().setText("Vui lòng thêm nhân viên");
                card.getLblCode2().setText("Không có mã nhân viên");
                card.getPnlInforEmployee1().setVisible(true);
                card.getPnlInforEmployee2().setVisible(true);
                card.getBtnAdd().setVisible(true);

                // Reset avatar về null trước khi set ảnh mặc định
                card.getAvatarLabel1().setImage(null);
                card.getAvatarLabel2().setImage(null);
                if (defaultImage != null) {
                    card.getAvatarLabel1().setImage(defaultImage);
                    card.getAvatarLabel2().setImage(defaultImage);
                }
            }

            // Lấy danh sách EmployeeShift theo ngày
            List<EmployeeShift> employeeShifts = employeeShiftService.getAllShiftsByDate(date);
            if (employeeShifts == null || employeeShifts.isEmpty()) {
                // Ngày này không có nhân viên -> giữ mặc định
                return;
            }

            // Nhóm EmployeeShift theo ShiftId
            Map<Long, List<EmployeeShift>> shiftMap = employeeShifts.stream()
                    .collect(Collectors.groupingBy(es -> es.getShift().getShiftId()));

            // Load nhân viên vào ShiftCard
            for (int i = 0; i < Math.min(shifts.size(), shiftCards.length); i++) {
                Shift shift = shifts.get(i);
                ShiftCard card = shiftCards[i];

                List<EmployeeShift> employeesInShift = shiftMap.get(shift.getShiftId());
                if (employeesInShift != null && !employeesInShift.isEmpty()) {
                    for (int j = 0; j < Math.min(2, employeesInShift.size()); j++) {
                        EmployeeShift es = employeesInShift.get(j);
                        Employee employee = employeeService.getEmployeeById(es.getEmployee().getEmployeeId());
                        if (employee == null) continue;

                        String employeeName = employee.getFullName();
                        String employeeCode = String.valueOf(employee.getEmployeeId());

                        BufferedImage image = defaultImage;
                        try {
                            if (employee.getAvt() != null && employee.getAvt().length > 0) {
                                image = ImageIO.read(new ByteArrayInputStream(employee.getAvt()));
                                image = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 60, 60);
                            }
                        } catch (Exception ex) {
                            log.error("Error loading image for employee {}", employeeName, ex);
                        }

                        if (j == 0) {
                            card.getLblName1().setText(employeeName);
                            card.getLblCode1().setText(employeeCode);
                            card.getAvatarLabel1().setImage(image);
                            card.getPnlInforEmployee1().setVisible(true);
                        } else {
                            card.getLblName2().setText(employeeName);
                            card.getLblCode2().setText(employeeCode);
                            card.getAvatarLabel2().setImage(image);
                            card.getPnlInforEmployee2().setVisible(true);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error loading shifts by date: ", e);
        }
    }

    private void setDefaultShiftValues() {
        shiftCard1.setHeaderColor(Color.RED);
        shiftCard2.setHeaderColor(new Color(51, 204, 255));
        shiftCard3.setHeaderColor(Color.GREEN);
        shiftCard4.setHeaderColor(Color.yellow);

        shiftCard1.getLblShiftName().setText("CA 01");
        shiftCard1.getLblTime().setText("00:00 - 06:00");

        shiftCard2.getLblShiftName().setText("CA 02");
        shiftCard2.getLblTime().setText("06:00 - 12:00");

        shiftCard3.getLblShiftName().setText("CA 03");
        shiftCard3.getLblTime().setText("12:00 - 18:00");

        shiftCard4.getLblShiftName().setText("CA 04");
        shiftCard4.getLblTime().setText("18:00 - 00:00");
    }

    private void setupDateChooser() {
        txtDate.setEditable(false);
        txtDate.setFocusable(false);
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(sdf.format(new Date()));
        dateChooser.setDateFormat("dd-MM-yyyy");
        dateChooser.toDay();
        dateChooser.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser.hidePopup();
                LocalDate selectedDate = LocalDate.of(
                        date.getYear(),
                        date.getMonth(),
                        date.getDay()
                );

                // Load shifts theo ngày đã chọn
                loadEmployeeShiftsByDate(selectedDate);
            }
        });
        dateChooser.setTextRefernce(txtDate);
        iconDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser.showPopup(txtDate, 0, txtDate.getHeight());
            }
        });
    }

    private void setupShiftCardButtons() {
        shiftCard1.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard1, 0));
        shiftCard2.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard2, 1));
        shiftCard3.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard3, 2));
        shiftCard4.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard4, 3));
    }

    private void handleAddEmployeesToShift(ShiftCard shiftCard, int shiftIndex) {
        // Lấy danh sách nhân viên đã chọn từ ShiftList
        List<ShiftProfile> selectedProfiles = shiftList.getSelectedEmployees();

        // Kiểm tra xem có đúng 2 nhân viên được chọn không
        if (selectedProfiles.size() != 2) {
            Message.showMessageNoCancel("Thông báo", "Vui lòng chọn đúng 2 nhân viên!");
            return;
        }

        // Lấy thông tin nhân viên
        ShiftProfile profile1 = selectedProfiles.get(0);
        ShiftProfile profile2 = selectedProfiles.get(1);
        String name1 = profile1.getLblName().getText();
        String name2 = profile2.getLblName().getText();
        String shiftName = shiftCard.getLblShiftName().getText();

        // Hiển thị hộp xác nhận
        Message.showConfirm(
                "Xác nhận",
                "Bạn có chắc chắn muốn thêm " + name1 + " và " + name2 + " vào ca " + shiftName + " không?",
                () -> {
                    try {
                        // Lấy ngày đã chọn từ DateChooser
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date selectedDate = sdf.parse(txtDate.getText());
                        LocalDate shiftDate = selectedDate.toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate();

                        // Lấy Shift từ database (giả sử shifts đã được load)
                        if (shiftIndex >= shifts.size()) {
                            Message.showMessageNoCancel("Lỗi", "Ca làm việc không tồn tại!");
                            return;
                        }
                        Shift shift = shifts.get(shiftIndex);

                        // Lưu nhân viên 1 vào EmployeeShift
                        EmployeeShift employeeShift1 = new EmployeeShift();
                        Employee emp1 = new Employee();
                        emp1.setEmployeeId(Long.parseLong(profile1.getLblCode().getText()));
                        employeeShift1.setEmployee(emp1);
                        employeeShift1.setShift(shift);
                        employeeShift1.setShiftDate(shiftDate);
                        employeeShift1.setClosingTime(null);
                        employeeShift1.setCreatedAt(LocalDate.now());

                        employeeShiftService.addEmployeeShift(employeeShift1);

                        // Lưu nhân viên 2 vào EmployeeShift
                        EmployeeShift employeeShift2 = new EmployeeShift();
                        Employee emp2 = new Employee();
                        emp2.setEmployeeId(Long.parseLong(profile2.getLblCode().getText()));
                        employeeShift2.setEmployee(emp2);
                        employeeShift2.setShift(shift);
                        employeeShift2.setShiftDate(shiftDate);
                        employeeShift2.setClosingTime(null);
                        employeeShift2.setCreatedAt(LocalDate.now());

                        employeeShiftService.addEmployeeShift(employeeShift2);

                        // Cập nhật UI
                        shiftCard.getLblName1().setText(name1);
                        shiftCard.getLblCode1().setText(profile1.getLblCode().getText());
                        shiftCard.getAvatarLabel1().setImage(profile1.getAvatarLabel().getImage());

                        shiftCard.getLblName2().setText(name2);
                        shiftCard.getLblCode2().setText(profile2.getLblCode().getText());
                        shiftCard.getAvatarLabel2().setImage(profile2.getAvatarLabel().getImage());

                        shiftCard.getPnlInforEmployee1().setVisible(true);
                        shiftCard.getPnlInforEmployee2().setVisible(true);
                        shiftCard.getBtnAdd().setVisible(true);

                        shiftList.clearAllSelections();

                        Message.showMessageNoCancel("Thành công",
                                "Đã thêm nhân viên vào " + shiftName + " thành công!");

                    } catch (Exception e) {
                        log.error("Error adding employees to shift: ", e);
                        Message.showMessageNoCancel("Lỗi",
                                "Không thể thêm nhân viên vào ca: " + e.getMessage());
                    }
                }
        );
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerShift = new iuh.fit.se.group1.ui.component.HeaderShift();
        panelShiftCard = new javax.swing.JPanel();
        shiftCard1 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard3 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard2 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard4 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        lblTitle = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        iconDate = new javax.swing.JLabel();
        search = new iuh.fit.se.group1.ui.component.booking.Search();
        shiftList = new iuh.fit.se.group1.ui.component.shift.ShiftList();
        lblSearch = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 241, 241));
        setOpaque(false);

        panelShiftCard.setBackground(new java.awt.Color(241, 241, 241));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30));
        lblTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle.setText("Danh sách ca làm");

        txtDate.setFont(new java.awt.Font("Segoe UI", 1, 14));
        txtDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });

        iconDate.setText(" ");

        search.setBackground(new java.awt.Color(241, 241, 241));

        lblSearch.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSearch.setText("Tìm kiếm nhân viên:");

        javax.swing.GroupLayout panelShiftCardLayout = new javax.swing.GroupLayout(panelShiftCard);
        panelShiftCard.setLayout(panelShiftCardLayout);
        panelShiftCardLayout.setHorizontalGroup(
            panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShiftCardLayout.createSequentialGroup()
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblTitle))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(shiftCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(shiftCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(shiftCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addComponent(lblSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(shiftList, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelShiftCardLayout.setVerticalGroup(
            panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShiftCardLayout.createSequentialGroup()
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shiftCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shiftCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(shiftList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerShift, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
            .addComponent(panelShiftCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelShiftCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed

    }//GEN-LAST:event_txtDateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift;
    private javax.swing.JLabel iconDate;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelShiftCard;
    private iuh.fit.se.group1.ui.component.booking.Search search;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard1;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard2;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard3;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard4;
    private iuh.fit.se.group1.ui.component.shift.ShiftList shiftList;
    private javax.swing.JTextField txtDate;
    // End of variables declaration//GEN-END:variables
}