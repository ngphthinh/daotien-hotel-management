/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;


import com.raven.datechooser.SelectedDate;
import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.config.BookingSessionCache;
import iuh.fit.se.group1.dto.AmenityInfo;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.service.BookingService;
import iuh.fit.se.group1.service.CustomerService;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.service.RoomService;
import iuh.fit.se.group1.ui.component.booking.InfoAmenityPanel;
import iuh.fit.se.group1.ui.component.booking.InfoCustomerPanel;
import iuh.fit.se.group1.ui.component.booking.InfoRoomPanel;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author THIS PC
 */
public class BookingPage extends javax.swing.JPanel {

    private static final Logger log = LoggerFactory.getLogger(BookingPage.class);
    private final OrderService orderService;
    private final BookingService bookingService;
    private final RoomService roomService;
    private final CustomerService customerService;
    private int roomCount = 1;


    private final Map<String, AmenityInfo> amenityIds = new HashMap<>();

    AtomicReference<Customer> customerAtomicReference = new AtomicReference<>();

    /**
     * Creates new form BookingPage
     */
    public BookingPage() {
        initComponents();
        custom();
        orderService = new OrderService();
        customerService = new CustomerService();
        roomService = new RoomService();
        bookingService = new BookingService();

    }

    private void custom() {
        headerBooking1.getBtn().setText("Đặt phòng");
        addRoomPanel1.addAction(a ->
                addNewRoom()
        );
        infoRoomPanel1.getBtnClose().setVisible(false);
        headerBooking1.addAction(e -> createOrder());

        listAmenity1.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println(listAmenity1.getSearch1().getText() + "BookingPage");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println(listAmenity1.getSearch1().getText() + "BookingPage");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        listAmenity1.addTableMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = listAmenity1.getTable1().getTbl().getSelectedRow();
                if (row != -1) {
                    String amenityId = Objects.toString(listAmenity1.getTable1().getTbl().getValueAt(row, 0), "").trim();
                    String amenityName = Objects.toString(listAmenity1.getTable1().getTbl().getValueAt(row, 1), "").trim();
                    String amenityPrice = Objects.toString(listAmenity1.getTable1().getTbl().getValueAt(row, 2), "").trim();
                    addNewAmenity(amenityId, amenityName, amenityPrice);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        pnlListAmenityMain.add(createPanelEmpty());
        pnlListAmenityMain.revalidate();
        pnlListAmenityMain.repaint();

        infoCustomerPanel1.addActionTxtCitizenId(e -> {
            Customer customer = customerService.getCustomerByCitizenId(infoCustomerPanel1.getTxtCitizen().getText().trim());
            if (customer != null) {
                infoCustomerPanel1.getTxtCustomerName().setText(customer.getFullName());
                infoCustomerPanel1.getTxtPhoneNumber().setText(customer.getPhone());
                infoCustomerPanel1.getTxtEmail().setText(customer.getEmail());
                // gender: false: male -> index 0 | true : female -> index 0
                infoCustomerPanel1.getCboGender().setSelectedIndex(customer.isGender() ? 1 : 0);
                Date dob = Date.from(customer.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
                infoCustomerPanel1.getDateChooser().setSelectedDate(dob);
                customerAtomicReference.set(customer);
            }
        });

        infoRoomPanel1.getCboRoomType().addActionListener(e -> selectRoom(infoRoomPanel1));
        infoRoomPanel1.getTxtStartDate().addActionListener(e -> selectRoom(infoRoomPanel1));
        infoRoomPanel1.getTxtEndDate().addActionListener(e -> selectRoom(infoRoomPanel1));
    }


    private void selectRoom(InfoRoomPanel infoRoomPanel) {
        String currentRoomIdText = infoRoomPanel1.getTxtRoomId().getText().trim();
        if (!currentRoomIdText.isEmpty()) {
            try {
                Long currentRoomId = Long.parseLong(currentRoomIdText);
                BookingSessionCache.removePendingRoom(currentRoomId);
            } catch (NumberFormatException ex) {
                AppLogger.error("Room ID invalid format: " + currentRoomIdText, ex);
            }
        }
        autoSelectAvailableRoom(infoRoomPanel);
    }

    private void autoSelectAvailableRoom(InfoRoomPanel infoRoomPanel) {
        //TODO: Chưa test được  test lại sao khi có hóa đơn
        String roomTypeId = infoRoomPanel.getCboRoomType().getSelectedIndex() == 0 ? "SINGLE" : "DOUBLE";

        // Lấy danh sách phòng trống theo loại phòng
        List<Room> rooms = roomService.getRoomByStatusOrRoomType(roomTypeId, RoomStatus.AVAILABLE);

        // Nếu không có phòng loại đó trong hệ thống
        if (rooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có phòng loại " + roomTypeId + " trong hệ thống!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime checkInDate = LocalDateTime.parse(infoRoomPanel1.getTxtStartDate().getText().trim(), formatter);
        LocalDateTime checkOutDate = LocalDateTime.parse(infoRoomPanel1.getTxtEndDate().getText().trim(), formatter);

        // Tìm phòng đầu tiên còn trống trong thời gian đó
        Room availableRoom = null;
        for (Room room : rooms) {
            boolean isAvailable = !bookingService.existsByRoomIdAndDate(room.getRoomId(), checkInDate, checkOutDate);
            boolean isPending = BookingSessionCache.isRoomPending(room.getRoomId());
            if (isAvailable && !isPending) {
                availableRoom = room;
                BookingSessionCache.addPendingRoom(room.getRoomId());
                break;
            }
        }


        // Nếu không có phòng nào trống trong ngày đó
        if (availableRoom == null) {
            JOptionPane.showMessageDialog(this,
                    "Không có phòng trống loại " + roomTypeId + " trong khoảng thời gian đã chọn!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Gán thông tin phòng trống đầu tiên vào panel
        infoRoomPanel.getTxtRoomId().setText(availableRoom.getRoomId().toString());
        infoRoomPanel.getTxtRoomNumber().setText(availableRoom.getRoomNumber());
    }

    private void createOrder() {

        Customer customer = customerAtomicReference.get();

        if (customer == null) {
            validCustomer(infoCustomerPanel1);
            customer = new Customer();
            customer.setFullName(infoCustomerPanel1.getTxtCustomerName().getText().trim());
            customer.setPhone(infoCustomerPanel1.getTxtPhoneNumber().getText().trim());
            customer.setEmail(infoCustomerPanel1.getTxtEmail().getText().trim());
            customer.setCitizenId(infoCustomerPanel1.getTxtCitizen().getText().trim());
            customer.setGender(infoCustomerPanel1.getCboGender().getSelectedIndex() == 1);
            SelectedDate dob = infoCustomerPanel1.getDateChooser().getSelectedDate();
            LocalDate dateOfBirth = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDay());
            customer.setDateOfBirth(dateOfBirth);
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setEmployee(new Employee(4L));
        order.setDeposit(BigDecimal.ZERO);

        for (var room : getBookingRooms()) {
            Booking booking = new Booking();
            booking.setEmployee(new Employee(4L));
            booking.setBookingType(BookingType.fromIndex(Integer.parseInt(room.get("bookingType"))));
            booking.setRoom(new Room(Long.parseLong(room.get("roomId"))));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime checkInDate = LocalDateTime.parse(room.get("startDate"), formatter);
            LocalDateTime checkOutDate = LocalDateTime.parse(room.get("endDate"), formatter);
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);

            // tinh toan tien dua vao booking type va so ngay o va loai phong
            booking.calcTotalPrice(room.get("roomType"));
            order.addBooking(booking);
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (var amenityEntry : amenityIds.entrySet()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmenity(new Amenity(Long.parseLong(amenityEntry.getKey()), amenityEntry.getValue().getPrice()));
            orderDetail.setQuantity(amenityEntry.getValue().getQuantity());
            orderDetail.calcUnitPrice();
            orderDetails.add(orderDetail);
        }


        if (orderService.createOrder(order, orderDetails) != null) {
            CustomDialog.showMessage(null, "Tạo đơn đặt phòng thành công!",
                    "Thông báo", CustomDialog.MessageType.INFO);
        } else {
            CustomDialog.showMessage(null, "Tạo đơn đặt phòng thất bại!",
                    "Thông báo", CustomDialog.MessageType.ERROR);
        }


    }

    private void validCustomer(InfoCustomerPanel infoCustomerPanel) {

    }

    private List<Map<String, String>> getBookingRooms() {
        List<Map<String, String>> rooms = new ArrayList<>();
        for (Component comp : pnlListRoom.getComponents()) {
            if (comp instanceof InfoRoomPanel roomPanel) {
                String roomId = roomPanel.getTxtRoomId().getText().trim();
                String startDate = roomPanel.getTxtStartDate().getText().trim();
                String endDate = roomPanel.getTxtEndDate().getText().trim();
                String bookingTypeIndex = String.valueOf(roomPanel.getCboBookingType().getSelectedIndex());
                String roomTypeIndex = String.valueOf(roomPanel.getCboRoomType().getSelectedIndex());
                if (!roomId.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                    rooms.add(Map.of(
                            "roomId", roomId,
                            "startDate", startDate,
                            "endDate", endDate,
                            "bookingType", bookingTypeIndex,
                            "roomType", roomTypeIndex
                    ));
                }
            }
        }
        return rooms;
    }

    public JPanel createPanelEmpty() {
        JLabel label = new JLabel("Chưa có dịch vụ nào được chọn");
        label.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        label.setForeground(new Color(150, 150, 150));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255));
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private void addNewAmenity(String amenityId, String amenityName, String amenityPrice) {

//        // Xoá panel rỗng nếu có
        if (pnlListAmenityMain.getComponentCount() <= 1 &&
                !(pnlListAmenityMain.getComponent(0) instanceof InfoAmenityPanel)) {
            pnlListAmenityMain.removeAll();
        }

        // Nếu đã có: tăng số lượng
        if (amenityIds.containsKey(amenityId)) {
            for (Component comp : pnlListAmenityMain.getComponents()) {
                if (comp instanceof InfoAmenityPanel panel && amenityId.equals(panel.getAmenityId())) {
                    AmenityInfo info = amenityIds.get(amenityId);
                    int newValue = info.getQuantity() + 1;
                    info.setQuantity(newValue);
                    panel.getNumberSpinner().setValue(newValue);
                    return;
                }
            }
        }


        // Nếu chưa có: thêm mới

        int quantity = 1;
        amenityIds.put(amenityId, new AmenityInfo(quantity, new BigDecimal(Constants.parseVND(amenityPrice))));
        InfoAmenityPanel newAmenity = new InfoAmenityPanel();
        newAmenity.setAmenityId(amenityId);
        newAmenity.getLblName().setText(amenityName);
        newAmenity.getLblPrice().setText(amenityPrice);
        newAmenity.getNumberSpinner().setValue(quantity);

        newAmenity.getNumberSpinner().addMinusActionListener(e -> {
            AmenityInfo info = amenityIds.get(amenityId);
            if (info == null) return;

            int current = info.getQuantity();
            if (current <= 1) {
                // Xóa tiện ích khỏi danh sách nếu giảm về 0
                amenityIds.remove(amenityId);
                closeAmenityCard(newAmenity, pnlListAmenityMain);
            } else {
                // Giảm số lượng, giữ nguyên giá
                info.setQuantity(current - 1);
                newAmenity.getNumberSpinner().setValue(current - 1);
            }
        });

        newAmenity.getNumberSpinner().addPlusActionListener(e -> {
            AmenityInfo info = amenityIds.get(amenityId);
            if (info == null) return;

            int newValue = info.getQuantity() + 1;
            info.setQuantity(newValue);
            newAmenity.getNumberSpinner().setValue(newValue);
        });

        pnlListAmenityMain.add(newAmenity);
        pnlListAmenityMain.revalidate();
        pnlListAmenityMain.repaint();
    }

    private void addNewRoom() {
        roomCount++;

        infoRoomPanel1.getBtnClose().setVisible(roomCount != 1);
        infoRoomPanel1.repaintAll();

        InfoRoomPanel newRoom = new InfoRoomPanel();
        newRoom.getLblTitle().setText(String.format("Phòng %02d", roomCount));

        newRoom.closeRoomCard(e -> closeRoomCard(newRoom));
        newRoom.getCboRoomType().addActionListener(e -> selectRoom(newRoom));
        newRoom.getTxtStartDate().addActionListener(e -> selectRoom(newRoom));
        newRoom.getTxtEndDate().addActionListener(e -> selectRoom(newRoom));
        // vị trí trước nút "Thêm phòng"
        int index = pnlListRoom.getComponentCount() - 1;
        pnlListRoom.add(newRoom, index);

        // separator giữa các phòng
        JPanel separator = new JPanel();
        separator.setBackground(new Color(241, 241, 241));
        separator.setPreferredSize(new Dimension(newRoom.getWidth(), 8));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        pnlListRoom.add(separator, index + 1);

        pnlListRoom.revalidate();
        pnlListRoom.repaint();
    }

    public void closeAmenityCard(InfoAmenityPanel amenityPanel, JPanel parentPanel) {

        if (parentPanel.getComponentCount() == 1) {
            parentPanel.add(createPanelEmpty());
        }

        parentPanel.remove(amenityPanel);
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private void closeRoomCard(InfoRoomPanel newRoom) {
        roomCount--;
        infoRoomPanel1.getBtnClose().setVisible(roomCount != 1);
        infoRoomPanel1.repaintAll();

        int index = findComponentIndex(pnlListRoom, newRoom);
        if (index != -1) {
            pnlListRoom.remove(newRoom);
            // kiểm tra phần tử ngay sau newRoom có phải separator không
            if (index < pnlListRoom.getComponentCount()) {
                Component next = pnlListRoom.getComponent(index);
                if (next instanceof JPanel sep && sep.getPreferredSize().height == 8) {
                    pnlListRoom.remove(sep);
                }
            }
        }

        pnlListRoom.revalidate();
        pnlListRoom.repaint();
    }


    //  Hàm phụ để tìm index của component trong panel
    private int findComponentIndex(Container parent, Component comp) {
        Component[] comps = parent.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == comp) return i;
        }
        return -1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerBooking1 = new iuh.fit.se.group1.ui.component.HeaderBooking();
        scrollPaneWin111 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        jPanel1 = new javax.swing.JPanel();
        infoCustomerPanel1 = new iuh.fit.se.group1.ui.component.booking.InfoCustomerPanel();
        pnlListRoom = new javax.swing.JPanel();
        infoRoomPanel1 = new iuh.fit.se.group1.ui.component.booking.InfoRoomPanel();
        addRoomPanel1 = new iuh.fit.se.group1.ui.component.booking.AddRoomPanel();
        pnlListAmenity = new javax.swing.JPanel();
        lblListAmenityTitle = new javax.swing.JLabel();
        pnlListAmenityMain = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        listAmenity1 = new iuh.fit.se.group1.ui.component.booking.ListAmenity();

        scrollPaneWin111.setBackground(new java.awt.Color(241, 241, 241));

        jPanel1.setBackground(new java.awt.Color(241, 241, 241));

        pnlListRoom.setBackground(new java.awt.Color(255, 255, 255));
        pnlListRoom.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlListRoom.setForeground(new java.awt.Color(255, 255, 255));
        pnlListRoom.setLayout(new javax.swing.BoxLayout(pnlListRoom, javax.swing.BoxLayout.Y_AXIS));
        pnlListRoom.add(infoRoomPanel1);
        pnlListRoom.add(addRoomPanel1);

        pnlListAmenity.setBackground(new java.awt.Color(255, 255, 255));

        lblListAmenityTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblListAmenityTitle.setForeground(new java.awt.Color(131, 176, 211));
        lblListAmenityTitle.setText("Danh sách dịch vụ");

        pnlListAmenityMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlListAmenityMain.setLayout(new javax.swing.BoxLayout(pnlListAmenityMain, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlListAmenityLayout = new javax.swing.GroupLayout(pnlListAmenity);
        pnlListAmenity.setLayout(pnlListAmenityLayout);
        pnlListAmenityLayout.setHorizontalGroup(
                pnlListAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlListAmenityLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlListAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlListAmenityLayout.createSequentialGroup()
                                                .addComponent(lblListAmenityTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(pnlListAmenityMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pnlListAmenityLayout.setVerticalGroup(
                pnlListAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlListAmenityLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblListAmenityTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlListAmenityMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlListRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                        .addComponent(infoCustomerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnlListAmenity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(531, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(infoCustomerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlListRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlListAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(427, Short.MAX_VALUE))
        );

        scrollPaneWin111.setViewportView(jPanel1);

        jPanel2.setBackground(new java.awt.Color(217, 217, 217));
        jPanel2.setForeground(new java.awt.Color(217, 217, 217));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        listAmenity1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerBooking1, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(listAmenity1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(headerBooking1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
                                        .addComponent(listAmenity1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.booking.AddRoomPanel addRoomPanel1;
    private iuh.fit.se.group1.ui.component.HeaderBooking headerBooking1;
    private iuh.fit.se.group1.ui.component.booking.InfoCustomerPanel infoCustomerPanel1;
    private iuh.fit.se.group1.ui.component.booking.InfoRoomPanel infoRoomPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblListAmenityTitle;
    private iuh.fit.se.group1.ui.component.booking.ListAmenity listAmenity1;
    private javax.swing.JPanel pnlListAmenity;
    private javax.swing.JPanel pnlListAmenityMain;
    private javax.swing.JPanel pnlListRoom;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin111;
    // End of variables declaration//GEN-END:variables
}
