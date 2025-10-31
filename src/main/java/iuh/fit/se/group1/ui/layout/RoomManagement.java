package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.service.RoomService;
import iuh.fit.se.group1.service.RoomTypeService;
import iuh.fit.se.group1.service.Properties;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.modal.RoomManagementModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
                List<Room> imported = importService.importRoomsFromExcel(file);
                if (imported != null && !imported.isEmpty()) {
                    roomService.getAllRooms().addAll(imported);
                    loadTable(roomService.getAllRooms());
                    Message.showMessage("Thành công", "Đã import " + imported.size() + " nhân viên!");
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
                        JOptionPane.showMessageDialog(RoomManagement.this, "Không được để trống giá phòng!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
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

                    JOptionPane.showMessageDialog(this, "Tất cả giá phòng phải lớn hơn 0!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                savePricesToFile();

                JOptionPane.showMessageDialog(this, "Cập nhật và lưu giá thành công!");

                // Khóa lại các field
                for (JTextField f : fields) {
                    f.setEditable(false);
                }
                btnUpdatePrice.setText("Cập nhật giá");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void savePricesToFile() {
        String filePath = "prices.properties";
        Properties props = Properties.loadProperties(filePath);

        props.setProperty("/price.single.hourly", txtSingleHour.getText());
        props.setProperty("/price.single.overnight", txtSingleNight.getText());
        props.setProperty("/price.single.daily", txtSingleDay.getText());
        props.setProperty("/price.single.firsthour", txtSingleFirstHour.getText());

        props.setProperty("/price.double.hourly", txtDoubleHour.getText());
        props.setProperty("/price.double.overnight", txtDoubleNight.getText());
        props.setProperty("/price.double.daily", txtDoubleDay.getText());
        props.setProperty("/price.double.firsthour", txtDoubleFirstHour.getText());

        Properties.saveProperties(filePath, props);
    }

    private void loadPricesFromFile() {
        String filePath = "prices.properties";

        txtSingleHour.setText(Properties.get(filePath, "/price.single.hourly"));
        txtSingleNight.setText(Properties.get(filePath, "/price.single.overnight"));
        txtSingleDay.setText(Properties.get(filePath, "/price.single.daily"));
        txtSingleFirstHour.setText(Properties.get(filePath, "/price.single.firsthour"));

        txtDoubleHour.setText(Properties.get(filePath, "/price.double.hourly"));
        txtDoubleNight.setText(Properties.get(filePath, "/price.double.overnight"));
        txtDoubleDay.setText(Properties.get(filePath, "/price.double.daily"));
        txtDoubleFirstHour.setText(Properties.get(filePath, "/price.double.firsthour"));
    }

    private void loadTable(List<Room> rooms) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        model.setRowCount(0);
        for (Room room : rooms) {
            model.addRow(new Object[] {
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomType() != null ? room.getRoomType().getName() : "N/A",
                    room.getRoomStatus() != null ? room.getRoomStatus().toString() : "AVAILABLE",
                    ""
            });
        }
        System.out.println("UI Load: Found " + rooms.size() + " rooms from DB");
    }

    private void setupTableModel() {
        String[] cols = { "Mã phòng", "Số phòng", "Loại phòng", "Trạng thái", "Chức năng" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRoom.getTbl().setModel(model);
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
                handleEditRoom(row);
            }

            @Override
            public void onDelete(int row) {
                handleDeleteRoom(row);
            }
        };
        tblRoom.setTableActionColumn(tblRoom.getTbl(), 4, event, false);
    }

    private void handleEditRoom(int row) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        Long roomId = (Long) model.getValueAt(row, 0);
        String number = (String) model.getValueAt(row, 1);
        String type = (String) model.getValueAt(row, 2);
        String status = (String) model.getValueAt(row, 3);

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

                Room updatedRoom = roomService.updateRoom(createRoomFromModal(roomId, numberNew, typeNew, statusNew));
                if (updatedRoom != null) {
                    loadTable(roomService.getAllRooms());
                    GlassPanePopup.closePopupLast();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        GlassPanePopup.showPopup(modal);
    }

    private void handleDeleteRoom(int row) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        Long roomId = (Long) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            roomService.deleteRoom(roomId);
            loadTable(roomService.getAllRooms());
            JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
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
            List<Room> existing = roomService.getRoomByKeyword(number);
            if (existing.stream().anyMatch(r -> !roomId.equals(r.getRoomId()) && r.getRoomNumber().equals(number))) {
                modal.getLblErrolNumberRoom().setText("Số phòng đã tồn tại!");
                isValid = false;
            }
        }

        return isValid;
    }

    private void setupHeaderFilters() {
        var header = tblRoom.getTbl().getTableHeader();
        List<RoomType> types = roomTypeService.getAllRoomTypes();
        String[] typeItems = new String[types.size() + 1];
        typeItems[0] = "Tất cả";
        for (int i = 0; i < types.size(); i++) {
            typeItems[i + 1] = types.get(i).getName();
        }

        Combobox<String> cmbType = new Combobox<>(typeItems);
        Combobox<String> cmbStatus = new Combobox<>(new String[] { "Tất cả", "AVAILABLE", "OCCUPIED", "MAINTENANCE" });

        TableCellRenderer defaultRenderer = header.getDefaultRenderer();

        TableColumn colType = tblRoom.getTbl().getColumnModel().getColumn(2);
        colType.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(header.getBackground());

            JLabel lbl = new JLabel("Loại phòng ▼");
            lbl.setFont(header.getFont());
            lbl.setForeground(new Color(102, 102, 102));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            panel.add(lbl, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));

            return panel;
        });

        TableColumn colStatus = tblRoom.getTbl().getColumnModel().getColumn(3);
        colStatus.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(header.getBackground());

            JLabel lbl = new JLabel("Trạng thái ▼");
            lbl.setFont(header.getFont());
            lbl.setForeground(new Color(102, 102, 102));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            panel.add(lbl, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));

            return panel;
        });

        cmbType.addActionListener(ev -> {
            currentTypeFilter = (String) cmbType.getSelectedItem();
            applyFilters();
        });

        cmbStatus.addActionListener(ev -> {
            currentStatusFilter = (String) cmbStatus.getSelectedItem();
            applyFilters();
        });

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblRoom.getTbl().columnAtPoint(e.getPoint());
                if (col == 2) {
                    showComboboxAtHeader(cmbType, 2);
                } else if (col == 3) {
                    showComboboxAtHeader(cmbStatus, 3);
                }
            }

            private void showComboboxAtHeader(Combobox<String> cmb, int columnIndex) {
                JTableHeader header = tblRoom.getTbl().getTableHeader();
                Rectangle headerRect = header.getHeaderRect(columnIndex);

                int x = headerRect.x;
                int y = headerRect.y + headerRect.height;

                JPopupMenu popup = new JPopupMenu();
                popup.setLayout(new BorderLayout());
                popup.add(cmb, BorderLayout.CENTER);
                popup.setPreferredSize(new Dimension(headerRect.width, 200));

                popup.show(header, x, y);

                cmb.addActionListener(e -> popup.setVisible(false));
            }
        });
    }

    private void showComboboxFilter(Component header, Combobox<?> cmb, int col) {
        TableColumn column = tblRoom.getTbl().getColumnModel().getColumn(col);
        Rectangle rect = tblRoom.getTbl().getTableHeader().getHeaderRect(col);
        cmb.setBounds(rect);
        cmb.setVisible(true);
    }

    private void showComboboxFilter(JComponent header, Combobox<?> cmb, int col) {
        cmb.showPopup();
        cmb.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent fe) {
                cmb.setVisible(false);
                header.remove(cmb);
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

    private Room createRoomFromModal(Long roomId, String number, String typeName, String statusStr) {
        Room room = new Room();
        room.setRoomId(roomId != null ? roomId : 0);
        room.setRoomNumber(number);

        List<RoomType> allTypes = roomTypeService.getAllRoomTypes();

        System.out.println("=== DEBUG: Tìm loại phòng ===");
        System.out.println("Tên cần tìm: [" + typeName + "] (length: " + typeName.length() + ")");
        allTypes.forEach(rt -> {
            System.out.println("- DB có: [" + rt.getName() + "] (length: " + rt.getName().length() + ")");
        });

        RoomType type = allTypes.stream()
                .filter(rt -> rt.getName() != null && rt.getName().trim().equalsIgnoreCase(typeName.trim()))
                .findFirst()
                .orElse(null);

        if (type == null) {
            type = allTypes.stream()
                    .filter(rt -> rt.getRoomTypeId() != null && rt.getRoomTypeId().equals(typeName))
                    .findFirst()
                    .orElse(null);
        }

        if (type == null) {
            String availableTypes = allTypes.stream()
                    .map(rt -> "'" + rt.getName() + "'")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("(không có)");

            throw new IllegalArgumentException(
                    "Không tìm thấy loại phòng: '" + typeName + "'\n" +
                            "Các loại có sẵn: " + availableTypes);
        }

        room.setRoomType(type);

        try {
            room.setRoomStatus(RoomStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
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
            ExportExcelService.exportTableToExcel(
                    this,
                    tblRoom.getTbl(),
                    "DanhSachPhong",
                    "DanhSachPhong");
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

    private void exportTableToExcel(RoomManagement roomManagement, JTable tbl, String string, String string2,
            boolean b) {
        throw new UnsupportedOperationException("Unimplemented method 'exportTableToExcel'");
    }

    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {
        RoomManagementModal modal = new RoomManagementModal();
        modal.closeModel(ae -> GlassPanePopup.closePopupLast());
        modal.saveData(ae -> {
            if (validateRoomData(modal, null)) {
                String number = modal.getTxtNumberRoom().getText().trim();
                String typeStr = (String) modal.getCmbTypeRoom().getSelectedItem();
                String statusStr = (String) modal.getCmbStatus().getSelectedItem();

                Room newRoom = createRoomFromModal(null, number, typeStr, statusStr);
                Room saved = roomService.createRoom(newRoom);
                if (saved != null) {
                    loadTable(roomService.getAllRooms());
                    GlassPanePopup.closePopupLast();
                    JOptionPane.showMessageDialog(this, "Thêm phòng thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        GlassPanePopup.showPopup(modal);
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