package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.RoomTypeDTO;
import iuh.fit.se.group1.dto.RoomViewDTO;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.service.RoomService;
import iuh.fit.se.group1.service.RoomTypeService;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.modal.RoomManagementModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;
import iuh.fit.se.group1.service.ExportExcelService;
import iuh.fit.se.group1.service.ImportExcelService;
import iuh.fit.se.group1.ui.component.custom.message.Message;

public class RoomManagement extends javax.swing.JPanel {

    private RoomService roomService;
    private RoomTypeService roomTypeService;
    private String currentTypeFilter = "Tất cả";
    private String currentStatusFilter = "Tất cả";

    private JLabel lblSingleType;
    private JLabel lblSingleHour, lblSingleNight, lblSingleDay;
    private JTextField txtSingleHour, txtSingleNight, txtSingleDay;
    private JLabel lblDoubleType;
    private JLabel lblDoubleHour, lblDoubleNight, lblDoubleDay;
    private JTextField txtDoubleHour, txtDoubleNight, txtDoubleDay;
    private JLabel lblSingleFirstHour, lblDoubleFirstHour;
    private JTextField txtSingleFirstHour, txtDoubleFirstHour;

    public RoomManagement() {
        initServices();
        initComponents();
        loadPricesFromFile();
        custom();
        loadTable(roomService.getAllRooms());
        try {
            setupFixedPrices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initServices() {
        roomService = new RoomService();
        roomTypeService = new RoomTypeService();
    }

    private void custom() {
        headerCustom.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý phòng</span>");
        headerCustom.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        btnAddRoom.setBackground(new Color(108, 165, 200));
        btnAddRoom.setForeground(Color.WHITE);
        btnAddRoom.setBorderRadius(10);
        btnAddRoom.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);

        btnUpdatePrice.setBackground(new Color(0, 150, 136));
        btnUpdatePrice.setForeground(Color.WHITE);
        btnUpdatePrice.setBorderRadius(10);
        btnUpdatePrice.setIcon(FontIcon.of(FontAwesomeSolid.SAVE, 17, Color.WHITE), SwingConstants.RIGHT);

        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);
        btnExport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_EXPORT, 17, Color.WHITE), SwingConstants.RIGHT);

        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);
        btnImport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_IMPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnImport.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImportExcelService importService = new ImportExcelService();
                List<RoomViewDTO> imported = importService.importRoomsFromExcel(file);
                if (imported != null && !imported.isEmpty()) {
                    roomService.getAllRooms().addAll(imported);
                    loadTable(roomService.getAllRooms());
                    Message.showMessage("Thành công", "Đã import " + imported.size() + " phòng!");
                } else {
                    Message.showMessage("Lỗi", "Không có dữ liệu nào được import!");
                }
            }
        });

        setupTableModel();
        setupTableActions();
        setupHeaderFilters();
        setupMouseListeners();
        setupSearchListener();
    }

    private void setupFixedPrices() throws IOException {

        txtSingleHour.setEditable(false);
        txtSingleNight.setEditable(false);
        txtSingleDay.setEditable(false);
        txtDoubleHour.setEditable(false);
        txtDoubleNight.setEditable(false);
        txtDoubleDay.setEditable(false);
        txtSingleFirstHour.setEditable(false);
        txtDoubleFirstHour.setEditable(false);

        btnUpdatePrice.addActionListener(e -> {
            boolean isEditing = txtSingleHour.isEditable();
            if (!isEditing) {
                txtSingleHour.setEditable(true);
                txtSingleNight.setEditable(true);
                txtSingleDay.setEditable(true);
                txtDoubleHour.setEditable(true);
                txtDoubleNight.setEditable(true);
                txtDoubleDay.setEditable(true);
                txtSingleFirstHour.setEditable(true);
                txtDoubleFirstHour.setEditable(true);
                btnUpdatePrice.setText("Lưu giá");
                return;
            }

            try {
                // Kiểm tra trống trước
                JTextField[] fields = {
                        txtSingleHour, txtSingleNight, txtSingleDay, txtSingleFirstHour,
                        txtDoubleHour, txtDoubleNight, txtDoubleDay, txtDoubleFirstHour
                };
                for (JTextField f : fields) {
                    if (f.getText().trim().isEmpty()) {
                        CustomDialog.showMessage(this,
                                "Vui lòng điền đầy đủ thông tin giá phòng!", "Lỗi",
                                CustomDialog.MessageType.ERROR,
                                600, 200);
                        return;
                    }
                }

                BigDecimal singleHour = new BigDecimal(txtSingleHour.getText().trim());
                BigDecimal singleNight = new BigDecimal(txtSingleNight.getText().trim());
                BigDecimal singleDay = new BigDecimal(txtSingleDay.getText().trim());
                BigDecimal singleFirstHour = new BigDecimal(txtSingleFirstHour.getText().trim());
                BigDecimal doubleHour = new BigDecimal(txtDoubleHour.getText().trim());
                BigDecimal doubleNight = new BigDecimal(txtDoubleNight.getText().trim());
                BigDecimal doubleDay = new BigDecimal(txtDoubleDay.getText().trim());
                BigDecimal doubleFirstHour = new BigDecimal(txtDoubleFirstHour.getText().trim());

                // Kiểm tra >0
                if (singleHour.compareTo(BigDecimal.ZERO) <= 0 ||
                        singleNight.compareTo(BigDecimal.ZERO) <= 0 ||
                        singleDay.compareTo(BigDecimal.ZERO) <= 0 ||
                        singleFirstHour.compareTo(BigDecimal.ZERO) <= 0 ||
                        doubleHour.compareTo(BigDecimal.ZERO) <= 0 ||
                        doubleNight.compareTo(BigDecimal.ZERO) <= 0 ||
                        doubleDay.compareTo(BigDecimal.ZERO) <= 0 ||
                        doubleFirstHour.compareTo(BigDecimal.ZERO) <= 0) {

                    CustomDialog.showMessage(this,
                            "Tất cả giá phòng phải lớn hơn 0!", "Lỗi",
                            CustomDialog.MessageType.ERROR,
                            600, 200);
                    return;
                }

                savePricesToFile();
                CustomDialog.showMessage(this,
                        "Cập nhật giá phòng thành công!", "Thành công",
                        CustomDialog.MessageType.SUCCESS,
                        600, 200);

                // Khóa lại các field
                for (JTextField f : fields) {
                    f.setEditable(false);
                }
                btnUpdatePrice.setText("Cập nhật giá");

            } catch (NumberFormatException ex) {
                CustomDialog.showMessage(this,
                        "Giá phòng không hợp lệ! Vui lòng nhập số.", "Lỗi",
                        CustomDialog.MessageType.ERROR,
                        600, 200);
            }
        });
    }

    private void savePricesToFile() {
        RoomTypeDTO singleType = new RoomTypeDTO();
        singleType.setHourlyRate(new BigDecimal(txtSingleFirstHour.getText().trim()));
        singleType.setDailyRate(new BigDecimal(txtSingleDay.getText().trim()));
        singleType.setOvernightRate(new BigDecimal(txtSingleNight.getText().trim()));
        singleType.setAdditionalHourRate(new BigDecimal(txtSingleHour.getText().trim()));
        singleType.setRoomTypeId("SINGLE");

        RoomTypeDTO doubleType = new RoomTypeDTO();
        doubleType.setHourlyRate(new BigDecimal(txtDoubleFirstHour.getText().trim()));
        doubleType.setDailyRate(new BigDecimal(txtDoubleDay.getText().trim()));
        doubleType.setOvernightRate(new BigDecimal(txtDoubleNight.getText().trim()));
        doubleType.setAdditionalHourRate(new BigDecimal(txtDoubleHour.getText().trim()));
        doubleType.setRoomTypeId("DOUBLE");

        roomTypeService.updateRoomType(singleType);
        roomTypeService.updateRoomType(doubleType);

    }

    private void loadPricesFromFile() {
        var roomType = roomTypeService.getAllRoomTypes();
        RoomTypeDTO singleType = roomType.stream()
                .filter(rt -> rt.getRoomTypeId().equals("SINGLE"))
                .findFirst()
                .orElse(null);
        RoomTypeDTO doubleType = roomType.stream()
                .filter(rt -> rt.getRoomTypeId().equals("DOUBLE"))
                .findFirst()
                .orElse(null);
        if (singleType == null || doubleType == null) {
            throw new IllegalStateException("Không tìm thấy loại phòng SINGLE hoặc DOUBLE trong CSDL!");
        }
        txtSingleDay.setText(singleType.getDailyRate().toString());
        txtDoubleDay.setText(doubleType.getDailyRate().toString());

        txtDoubleFirstHour.setText(doubleType.getHourlyRate().toString());
        txtSingleFirstHour.setText(singleType.getHourlyRate().toString());

        txtSingleNight.setText(singleType.getOvernightRate().toString());
        txtDoubleNight.setText(doubleType.getOvernightRate().toString());

        txtSingleHour.setText(singleType.getAdditionalHourRate().toString());
        txtDoubleHour.setText(doubleType.getAdditionalHourRate().toString());

    }

    private void loadTable(List<RoomViewDTO> rooms) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        model.setRowCount(0);
        for (RoomViewDTO room : rooms) {
            model.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomType() != null ? room.getRoomType().getName() : "N/A",
                    room.getRoomStatus() != null ? room.getRoomStatus().toString() : "AVAILABLE",
                    ""
            });
        }
    }

    private void setupTableModel() {
        String[] cols = {"Mã phòng", "Số phòng", "Loại phòng", "Trạng thái", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép edit cột "Chức năng" (column 4) để button hoạt động
                return column == 4;
            }
        };
        tblRoom.getTbl().setModel(model);

        // Khởi tạo TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblRoom.getTbl().setRowSorter(sorter);

        // Tắt sort cho tất cả các cột (chỉ dùng cho filter)
        for (int i = 0; i < tblRoom.getTbl().getColumnCount(); i++) {
            sorter.setSortable(i, false);
        }

        tblRoom.getTbl().setAutoCreateRowSorter(false);
        tblRoom.getTbl().getTableHeader().setReorderingAllowed(false);

        tblRoom.getTbl().getColumnModel().getColumn(0).setPreferredWidth(100);
        tblRoom.getTbl().getColumnModel().getColumn(1).setPreferredWidth(120);
        tblRoom.getTbl().getColumnModel().getColumn(2).setPreferredWidth(170);
        tblRoom.getTbl().getColumnModel().getColumn(3).setPreferredWidth(140);
        tblRoom.getTbl().getColumnModel().getColumn(4).setPreferredWidth(140);
    }

    private void setupTableActions() {
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
                handleEditRoom(row);
            }

            @Override
            public void onDelete(int row) {
                System.out.println("Delete row: " + row);
                handleDeleteRoom(row);
            }
        };
        tblRoom.setTableActionColumn(tblRoom.getTbl(), 4, event, false);
    }

    private void handleEditRoom(int row) {
        JTable table = tblRoom.getTbl();
        int modelRow = table.convertRowIndexToModel(row);


        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object idObj = model.getValueAt(modelRow, 0);
        Long roomId = idObj instanceof Number ? ((Number) idObj).longValue() : Long.parseLong(String.valueOf(idObj));
        String number = String.valueOf(model.getValueAt(modelRow, 1));
        String type = String.valueOf(model.getValueAt(modelRow, 2));
        String status = String.valueOf(model.getValueAt(modelRow, 3));

        System.out.println("Room data - ID: " + roomId + ", Number: " + number + ", Type: " + type + ", Status: " + status);

        // Kiểm tra phòng có trong hóa đơn loại 2 hoặc 3 không
        if (!roomService.canDeleteRoom(roomId)) {
            CustomDialog.showMessage(this,
                    "<html><div style='width:400px;'>" +
                            "Không thể chỉnh sửa phòng này!<br><br>" +
                            "<b>Lý do:</b><br>" +
                            "- Phòng đang được sử dụng trong hóa đơn loại \"Đang xử lí\" hoặc \"Đặt trước\"<br>" +
                            "- Vui lòng hoàn thành hoặc hủy hóa đơn trước khi chỉnh sửa phòng" +
                            "</div></html>",
                    "Không thể chỉnh sửa",
                    CustomDialog.MessageType.ERROR,
                    600, 250);
            return;
        }

        RoomManagementModal modal = new RoomManagementModal();
        modal.getBtnSave().setText("Cập nhật");
        modal.getTxtNumberRoom().setText(number);
        modal.getCmbTypeRoom().setSelectedItem(type);
        modal.getCmbStatus().setSelectedItem(status);

        modal.closeModel(ae -> GlassPanePopup.closePopupLast());
        modal.saveData(ae -> {
            if (validateRoomData(modal, roomId)) {
                String numberNew = modal.getTxtNumberRoom().getText().trim();
                String typeNew = (String) modal.getCmbTypeRoom().getSelectedItem();
                String statusNew = (String) modal.getCmbStatus().getSelectedItem();

//                if (roomService.existsByRoomNumber(numberNew)) {
//                   CustomDialog.showMessage(
//                           this,
//                           "Số phòng đã tồn tại!", "Lỗi",
//                           CustomDialog.MessageType.ERROR,
//                           500,200);
//                    return;
//                }

                RoomViewDTO updatedRoom = roomService.updateRoom(createRoomFromModal(roomId, numberNew, typeNew, statusNew));
                if (updatedRoom != null) {
                    loadTable(roomService.getAllRooms());
                    GlassPanePopup.closePopupLast();
                    CustomDialog.showMessage(
                            this,
                            "Cập nhật thành công!", "Thành công",
                            CustomDialog.MessageType.SUCCESS,
                            500, 200);
                } else {
                    CustomDialog.showMessage(
                            this,
                            "Cập nhật thất bại!", "Lỗi",
                            CustomDialog.MessageType.ERROR,
                            500, 200);
                }
            }
        });

        GlassPanePopup.showPopup(modal);
    }

    private void handleDeleteRoom(int row) {
        JTable table = tblRoom.getTbl();
        // Convert view row index to model row index
        int modelRow = table.convertRowIndexToModel(row);

        System.out.println("Delete - View row: " + row + ", Model row: " + modelRow);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        // Use modelRow instead of row
        Object idObj = model.getValueAt(modelRow, 0);
        Long roomId = idObj instanceof Number ? ((Number) idObj).longValue() : Long.parseLong(String.valueOf(idObj));
        System.out.println("Deleting room ID: " + roomId);

        int confirm = CustomDialog.showConfirm(this, "Bạn có chắc chắn muốn xóa phòng này?", "Xác nhận xóa",
                CustomDialog.MessageType.WARNING,
                600, 200);
        if (confirm == JOptionPane.YES_OPTION) {
            if (!roomService.canDeleteRoom(roomId)) {
                CustomDialog.showMessage(this,
                        "<html><div style='width:400px;'>" +
                                "Không thể xóa phòng này!<br><br>" +
                                "<b>Lý do:</b><br>" +
                                "- Phòng đang được sử dụng trong hóa đơn hiện tại, HOẶC<br>" +
                                "- Phòng được đặt trước nhưng không tìm thấy phòng thay thế cùng loại" +
                                "</div></html>",
                        "Không thể xóa",
                        CustomDialog.MessageType.ERROR,
                        600, 250);
                return;
            }
            roomService.deleteRoom(roomId);
            loadTable(roomService.getAllRooms());
            CustomDialog.showMessage(this, "Xóa phòng thành công!", "Thành công",
                    CustomDialog.MessageType.SUCCESS,
                    500, 200);
        }
    }

    private boolean validateRoomData(RoomManagementModal modal, Long roomId) {
        String number = modal.getTxtNumberRoom().getText().trim();
        modal.getLblErrolNumberRoom().setText("");
        modal.getLblErrolNumberRoom().setForeground(Color.RED);

        boolean isValid = true;

        if (number.isEmpty()) {
            modal.getLblErrolNumberRoom().setText("Số phòng không được để trống!");
            isValid = false;
        } else if (!number.matches("\\d+")) {
            modal.getLblErrolNumberRoom().setText("Số phòng chỉ được chứa chữ số!");
            isValid = false;
        } else {
            List<RoomViewDTO> existing = roomService.getRoomByKeyword(number);
            if (existing.stream().anyMatch(r -> !roomId.equals(r.getRoomId()) && r.getRoomNumber().equals(number))) {
                modal.getLblErrolNumberRoom().setText("Số phòng đã tồn tại!");
                isValid = false;
            }
        }

        return isValid;
    }

    private void setupHeaderFilters() {
        var header = tblRoom.getTbl().getTableHeader();
        List<RoomTypeDTO> types = roomTypeService.getAllRoomTypes();
        String[] typeItems = new String[types.size() + 1];
        typeItems[0] = "Tất cả";
        for (int i = 0; i < types.size(); i++) {
            typeItems[i + 1] = types.get(i).getName();
        }

        Combobox<String> cmbType = new Combobox<>(typeItems);
        Combobox<String> cmbStatus = new Combobox<>(new String[]{"Tất cả", RoomStatus.AVAILABLE.toString(), RoomStatus.OCCUPIED.toString(), RoomStatus.OUT_OF_ORDER.toString()});

        // ===== SỬA PHẦN NÀY: Dùng defaultRenderer thay vì custom renderer =====
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();

        // Reset tất cả cột về default renderer
        for (int i = 0; i < tblRoom.getTbl().getColumnCount(); i++) {
            tblRoom.getTbl().getColumnModel().getColumn(i).setHeaderRenderer(defaultRenderer);
        }

        // Cột Loại phòng - Dùng defaultRenderer với text có mũi tên
        TableColumn colType = tblRoom.getTbl().getColumnModel().getColumn(2);
        colType.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                // Thêm khoảng trắng và mũi tên xuống
                String text = "Loại phòng                            \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        // Cột Trạng thái - Dùng defaultRenderer với text có mũi tên
        TableColumn colStatus = tblRoom.getTbl().getColumnModel().getColumn(3);
        colStatus.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Trạng thái                            \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        // ===== ACTION LISTENERS =====
        cmbType.addActionListener(ev -> {
            currentTypeFilter = (String) cmbType.getSelectedItem();
            applyFilters();
            header.remove(cmbType);
            header.repaint();
        });

        cmbStatus.addActionListener(ev -> {
            currentStatusFilter = (String) cmbStatus.getSelectedItem();
            applyFilters();
            header.remove(cmbStatus);
            header.repaint();
        });

        // ===== MOUSE LISTENER: Click vào header để show combobox =====
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblRoom.getTbl().columnAtPoint(e.getPoint());

                // Remove các combobox cũ
                header.remove(cmbType);
                header.remove(cmbStatus);

                // Chỉ show combobox khi click vào cột 2 hoặc 3
                if (col != 2 && col != 3) {
                    return;
                }

                Rectangle rect = header.getHeaderRect(col);

                if (col == 2) {
                    // Show combobox Loại phòng
                    cmbType.setBounds(rect);
                    header.add(cmbType);
                    cmbType.setVisible(true);
                    cmbType.showPopup();
                } else if (col == 3) {
                    // Show combobox Trạng thái
                    cmbStatus.setBounds(rect);
                    header.add(cmbStatus);
                    cmbStatus.setVisible(true);
                    cmbStatus.showPopup();
                }
            }
        });
    }


    private void applyFilters() {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblRoom.getTbl().getRowSorter();
        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String type = entry.getStringValue(2);
                String status = entry.getStringValue(3);

                boolean typeMatches = currentTypeFilter.equals("Tất cả")
                        || (type != null && type.equals(currentTypeFilter));
                boolean statusMatches = currentStatusFilter.equals("Tất cả")
                        || (status != null && status.equals(currentStatusFilter));

                return typeMatches && statusMatches;
            }
        };
        sorter.setRowFilter(rf);
        sorter.setSortKeys(null);
    }

    private void setupMouseListeners() {
        tblRoom.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblRoom.getTbl().columnAtPoint(e.getPoint());
                if (col == 4) {
                    tblRoom.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblRoom.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblRoom.getTbl().clearSelection();
            }
        });
    }

    private void setupSearchListener() {
        headerCustom.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = headerCustom.getSearchText();
                if (text.isEmpty()) {
                    loadTable(roomService.getAllRooms());
                } else {
                    loadTable(roomService.getRoomByKeyword(text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = headerCustom.getSearchText();
                if (text.isEmpty()) {
                    loadTable(roomService.getAllRooms());
                } else {
                    loadTable(roomService.getRoomByKeyword(text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private RoomViewDTO createRoomFromModal(Long roomId, String number, String typeName, String statusStr) {
        RoomViewDTO room = new RoomViewDTO();
        room.setRoomId(roomId);
        room.setRoomNumber(number);


        if (typeName.equals("Phòng đôi")) {
            room.setRoomType(RoomTypeDTO.builder().roomTypeId(Constants.DOUBLE_ROOM_TYPE).build());
        } else {
            room.setRoomType(RoomTypeDTO.builder().roomTypeId(Constants.SINGLE_ROOM_TYPE).build());
        }

        try {
            room.setRoomStatus(RoomStatus.fromDisplayName(statusStr));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            room.setRoomStatus(RoomStatus.AVAILABLE);
        }

        return room;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        headerCustom = new iuh.fit.se.group1.ui.component.HeaderCustom();
        lblTitle = new javax.swing.JLabel();
        btnAddRoom = new iuh.fit.se.group1.ui.component.custom.Button();
        btnUpdatePrice = new iuh.fit.se.group1.ui.component.custom.Button(); // Custom Button
        tblRoom = new iuh.fit.se.group1.ui.component.table.Table();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

        // Nhóm Phòng đơn
        lblSingleType = new JLabel("Phòng đơn:");
        lblSingleType.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSingleHour = new JLabel("Giá giờ:");
        lblSingleNight = new JLabel("Giá đêm:");
        lblSingleDay = new JLabel("Giá ngày:");
        txtSingleHour = new JTextField(8);
        txtSingleNight = new JTextField(8);
        txtSingleDay = new JTextField(8);
        lblSingleFirstHour = new JLabel("Giá lần đầu/giờ:");
        lblSingleFirstHour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSingleFirstHour = new JTextField(8);

        // Nhóm Phòng đôi
        lblDoubleType = new JLabel("Phòng đôi:");
        lblDoubleType.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoubleHour = new JLabel("Giá giờ:");
        lblDoubleNight = new JLabel("Giá đêm:");
        lblDoubleDay = new JLabel("Giá ngày:");
        txtDoubleHour = new JTextField(8);
        txtDoubleNight = new JTextField(8);
        txtDoubleDay = new JTextField(8);
        lblDoubleFirstHour = new JLabel("Giá lần đầu/giờ:");
        lblDoubleFirstHour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDoubleFirstHour = new JTextField(8);

        JPanel pricePanel = new JPanel(new GridLayout(2, 6, 5, 5));
        pricePanel.setBackground(new Color(241, 241, 241));

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Cập nhật giá loại phòng");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        pricePanel.setBorder(titledBorder);

        pricePanel.add(lblSingleType);
        pricePanel.add(lblSingleHour);
        pricePanel.add(txtSingleHour);
        pricePanel.add(lblSingleNight);
        pricePanel.add(txtSingleNight);
        pricePanel.add(lblSingleDay);
        pricePanel.add(txtSingleDay);
        pricePanel.add(lblSingleFirstHour);
        pricePanel.add(txtSingleFirstHour);

        pricePanel.add(lblDoubleType);
        pricePanel.add(lblDoubleHour);
        pricePanel.add(txtDoubleHour);
        pricePanel.add(lblDoubleNight);
        pricePanel.add(txtDoubleNight);
        pricePanel.add(lblDoubleDay);
        pricePanel.add(txtDoubleDay);
        pricePanel.add(lblDoubleFirstHour);
        pricePanel.add(txtDoubleFirstHour);

        setBackground(new java.awt.Color(241, 241, 241));

        lblTitle.setBackground(new java.awt.Color(131, 131, 131));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30));
        lblTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Danh sách phòng");

        btnAddRoom.setText("Thêm phòng");
        btnAddRoom.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnAddRoom.setMaximumSize(new java.awt.Dimension(119, 27));
        btnAddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRoomActionPerformed(evt);
            }
        });

        tblRoom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        btnExport.setText("Xuất excel");
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnExport.addActionListener(e -> {
            try {
                byte[] data = ExportExcelService.exportTableToExcel(
                        tblRoom.getTbl(),
                        "Danh sách phòng ",
                        true
                );

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Lưu file Excel");

                // ✔ tên file mặc định
                String defaultFileName = "DanhSachPhong_" +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx";

                fileChooser.setSelectedFile(new File(defaultFileName));

                int result = fileChooser.showSaveDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                        file = new File(file.getAbsolutePath() + ".xlsx");
                    }

                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(data);
                    }

                    Message.showMessage("Thành công", "Đã lưu file: " + file.getAbsolutePath());
                }

            } catch (Exception ex) {
                Message.showMessage("Lỗi", ex.getMessage());
            }
        });

        btnImport.setText("Tải excel");
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnUpdatePrice.setText("Cập nhật giá");
        btnUpdatePrice.setFont(new java.awt.Font("Segoe UI", 1, 14));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerCustom, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(lblTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 160,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(pricePanel, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE,
                                        Short.MAX_VALUE)
                                .addContainerGap())
                        .addComponent(tblRoom, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerCustom, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(pricePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(tblRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 615,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(8, Short.MAX_VALUE)));
    }


    // CHẮC CHẮN ĐẢM BẢO NHU CẦU TỐI ĐA THEO HỆ HÀNG HÓA TỔNG SỐ ĐIỀU TRA TÊN THANH LÝ HÀNG HÓA NỘI ĐỊA NÓI CHUNG VÀ KHU VỰC NÓI RIÊNG, ĐẢM BẢO TÍNH SẴN DÙNG VÀ NHIỀU YẾU TỐ KHÁC
    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {
        RoomManagementModal modal = new RoomManagementModal();
        modal.closeModel(ae -> GlassPanePopup.closePopupLast());
        modal.saveData(ae -> {
            if (validateRoomData(modal, null)) {
                String number = modal.getTxtNumberRoom().getText().trim();
                String typeStr = (String) modal.getCmbTypeRoom().getSelectedItem();
                String statusStr = (String) modal.getCmbStatus().getSelectedItem();

                RoomViewDTO newRoom = createRoomFromModal(null, number, typeStr, statusStr);
                RoomViewDTO saved = roomService.createRoom(newRoom);
                if (saved != null) {
                    loadTable(roomService.getAllRooms());
                    GlassPanePopup.closePopupLast();
                    CustomDialog.showMessage(this, "Thêm phòng thành công!", "Thành công",
                            CustomDialog.MessageType.SUCCESS,
                            500, 200);
                } else {
                    CustomDialog.showMessage(this, "Thêm phòng thất bại!", "Lỗi",
                            CustomDialog.MessageType.ERROR,
                            500, 200);
                }
            }
        });
        GlassPanePopup.showPopup(modal);

    }


    public void loadData() {
        loadTable(roomService.getAllRooms());
        loadPricesFromFile();
    }

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Import Excel clicked");
    }

    private iuh.fit.se.group1.ui.component.custom.Button btnAddRoom;
    private Button btnUpdatePrice;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.table.Table tblRoom;
}