package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.service.RoomService;
import iuh.fit.se.group1.service.RoomTypeService;
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
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

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

        setupTableModel();
        setupTableActions();
        setupHeaderFilters();
        setupMouseListeners();
        setupSearchListener();
    }
    
    

    private void setupFixedPrices() throws IOException {
        
        txtSingleHour.setEditable(false); txtSingleNight.setEditable(false); txtSingleDay.setEditable(false);
        txtDoubleHour.setEditable(false); txtDoubleNight.setEditable(false); txtDoubleDay.setEditable(false);

       
        btnUpdatePrice.addActionListener(e -> {
            boolean isEditing = txtSingleHour.isEditable();
            if (!isEditing) {
                
                txtSingleHour.setEditable(true); txtSingleNight.setEditable(true); txtSingleDay.setEditable(true);
                txtDoubleHour.setEditable(true); txtDoubleNight.setEditable(true); txtDoubleDay.setEditable(true);
                btnUpdatePrice.setText("Lưu giá");
                return;
            }

           
            try {
                BigDecimal singleHour = new BigDecimal(txtSingleHour.getText().replaceAll("[^\\d]", ""));
                BigDecimal singleNight = new BigDecimal(txtSingleNight.getText().replaceAll("[^\\d]", ""));
                BigDecimal singleDay = new BigDecimal(txtSingleDay.getText().replaceAll("[^\\d]", ""));

                BigDecimal doubleHour = new BigDecimal(txtDoubleHour.getText().replaceAll("[^\\d]", ""));
                BigDecimal doubleNight = new BigDecimal(txtDoubleNight.getText().replaceAll("[^\\d]", ""));
                BigDecimal doubleDay = new BigDecimal(txtDoubleDay.getText().replaceAll("[^\\d]", ""));

                
                savePricesToFile();

                System.out.println("Lưu giá thành công vào prices.properties");
                JOptionPane.showMessageDialog(this, "Cập nhật và lưu giá thành công!");

                txtSingleHour.setEditable(false); txtSingleNight.setEditable(false); txtSingleDay.setEditable(false);
                txtDoubleHour.setEditable(false); txtDoubleNight.setEditable(false); txtDoubleDay.setEditable(false);
                btnUpdatePrice.setText("Cập nhật giá");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

   private void savePricesToFile() {
    Properties properties = new Properties();
    try {
        FileOutputStream output = new FileOutputStream("prices.properties");

        properties.setProperty("price.single.hourly", txtSingleHour.getText());
        properties.setProperty("price.single.overnight", txtSingleNight.getText());
        properties.setProperty("price.single.daily", txtSingleDay.getText());

        properties.setProperty("price.double.hourly", txtDoubleHour.getText());
        properties.setProperty("price.double.overnight", txtDoubleNight.getText());
        properties.setProperty("price.double.daily", txtDoubleDay.getText());

        properties.store(output, null);
        output.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   private void loadPricesFromFile() {
    Properties properties = new Properties();
    try {
        File file = new File("prices.properties");
        if (!file.exists()) return;

        FileInputStream input = new FileInputStream(file);
        properties.load(input);

        txtSingleHour.setText(properties.getProperty("price.single.hourly", ""));
        txtSingleNight.setText(properties.getProperty("price.single.overnight", ""));
        txtSingleDay.setText(properties.getProperty("price.single.daily", ""));

        txtDoubleHour.setText(properties.getProperty("price.double.hourly", ""));
        txtDoubleNight.setText(properties.getProperty("price.double.overnight", ""));
        txtDoubleDay.setText(properties.getProperty("price.double.daily", ""));

        input.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void loadTable(List<Room> rooms) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        model.setRowCount(0);
        for (Room room : rooms) {
            model.addRow(new Object[]{
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
        String[] cols = {"Mã phòng", "Số phòng", "Loại phòng", "Trạng thái", "Chức năng"};
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
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
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
        Combobox<String> cmbStatus = new Combobox<>(new String[]{"Tất cả", "AVAILABLE", "OCCUPIED", "MAINTENANCE"});

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

                boolean typeMatches = currentTypeFilter.equals("Tất cả") || (type != null && type.equals(currentTypeFilter));
                boolean statusMatches = currentStatusFilter.equals("Tất cả") || (status != null && status.equals(currentStatusFilter));

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
                            "Các loại có sẵn: " + availableTypes
            );
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
        btnUpdatePrice = new iuh.fit.se.group1.ui.component.custom.Button();  // Custom Button
        tblRoom = new iuh.fit.se.group1.ui.component.table.Table();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

        // Khởi tạo fields cho giá fixed
        // Nhóm Phòng đơn
        lblSingleType = new JLabel("Phòng đơn:");
        lblSingleType.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSingleHour = new JLabel("Giá giờ:");
        lblSingleNight = new JLabel("Giá đêm:");
        lblSingleDay = new JLabel("Giá ngày:");
        txtSingleHour = new JTextField(8);
        txtSingleNight = new JTextField(8);
        txtSingleDay = new JTextField(8);

        // Nhóm Phòng đôi
        lblDoubleType = new JLabel("Phòng đôi:");
        lblDoubleType.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoubleHour = new JLabel("Giá giờ:");
        lblDoubleNight = new JLabel("Giá đêm:");
        lblDoubleDay = new JLabel("Giá ngày:");
        txtDoubleHour = new JTextField(8);
        txtDoubleNight = new JTextField(8);
        txtDoubleDay = new JTextField(8);

        // Tạo JPanel cho phần giá (2 rows)
        JPanel pricePanel = new JPanel(new GridLayout(2, 6, 5, 5));  // 2 rows, 6 cols: type + 3 (label + field)
        pricePanel.setBorder(BorderFactory.createTitledBorder("Cập nhật giá loại phòng"));
        pricePanel.setBackground(new Color(241, 241, 241));

        // Row 1: Phòng đơn
        pricePanel.add(lblSingleType);
        pricePanel.add(lblSingleHour); pricePanel.add(txtSingleHour);
        pricePanel.add(lblSingleNight); pricePanel.add(txtSingleNight);
        pricePanel.add(lblSingleDay); pricePanel.add(txtSingleDay);

        // Row 2: Phòng đôi
        pricePanel.add(lblDoubleType);
        pricePanel.add(lblDoubleHour); pricePanel.add(txtDoubleHour);
        pricePanel.add(lblDoubleNight); pricePanel.add(txtDoubleNight);
        pricePanel.add(lblDoubleDay); pricePanel.add(txtDoubleDay);

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
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
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
            .addComponent(headerCustom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(pricePanel, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(tblRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(pricePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(tblRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
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
                    JOptionPane.showMessageDialog(this, "Thêm phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        GlassPanePopup.showPopup(modal);
    }

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Export Excel clicked");
    }
    
    private void saveRoomPrices() {
    Properties props = new Properties();
    saveRoomPrices();

    // Single
    props.setProperty("price.single.hourly", txtSingleHour.getText().trim());
    props.setProperty("price.single.overnight", txtSingleNight.getText().trim());
    props.setProperty("price.single.daily", txtSingleDay.getText().trim());

    // Double
    props.setProperty("price.double.hourly", txtDoubleHour.getText().trim());
    props.setProperty("price.double.overnight", txtDoubleNight.getText().trim());
    props.setProperty("price.double.daily", txtDoubleDay.getText().trim());

    try (FileOutputStream fos =
             new FileOutputStream("src/main/resources/price.properties")) {
        props.store(fos, null); // không ghi comment lên đầu file
        JOptionPane.showMessageDialog(this, "Lưu giá phòng thành công!");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi lưu giá phòng!");
    }
    }

    private void loadRoomPrices() {
    Properties props = new Properties();
    File file = new File("src/main/resources/price.properties");
    if (!file.exists()) return;

    try (FileInputStream fis = new FileInputStream(file)) {
        props.load(fis);

        txtSingleHour.setText(props.getProperty("price.single.hourly", "0"));
        txtSingleNight.setText(props.getProperty("price.single.overnight", "0"));
        txtSingleHour.setText(props.getProperty("price.single.daily", "0"));

        txtDoubleHour.setText(props.getProperty("price.double.hourly", "0"));
        txtDoubleNight.setText(props.getProperty("price.double.overnight", "0"));
        txtDoubleDay.setText(props.getProperty("price.double.daily", "0"));

    } catch (IOException e) {
        e.printStackTrace();
    }
}



    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Import Excel clicked");
    }

    // Variables declaration
    private iuh.fit.se.group1.ui.component.custom.Button btnAddRoom;
    private Button btnUpdatePrice;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.table.Table tblRoom;
    // End of variables declaration
}