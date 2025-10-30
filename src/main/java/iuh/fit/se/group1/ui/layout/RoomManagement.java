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
import java.math.BigDecimal;
import java.util.List;
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

    private JLabel lblRoomType;
    private JComboBox<String> cmbRoomType;
    private JLabel lblHourPrice;
    private JTextField txtHourPrice;
    private JLabel lblNightPrice;
    private JTextField txtNightPrice;
    private JLabel lblDayPrice;
    private JTextField txtDayPrice;

    public RoomManagement() {
        initServices();
        initComponents();
        custom();
        loadTable(roomService.getAllRooms());
        setupPriceEditing();
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
        btnUpdatePrice.setIcon(FontIcon.of(FontAwesomeSolid.SAVE, 17, Color.WHITE));

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

    private void setupPriceEditing() {
        // Populate combo with room types
        List<RoomType> types = roomTypeService.getAllRoomTypes();
        String[] typeItems = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeItems[i] = types.get(i).getName();
        }
        cmbRoomType = new JComboBox<>(typeItems);
        cmbRoomType.setSelectedIndex(0); // Default to first type

        // Sample prices (hardcoded as per user message; in real impl, load from DB)
        // Assuming "Phòng đơn" and "Phòng đôi" with prices 120k hour, 500k night, 600k day
        txtHourPrice.setText("120000");
        txtNightPrice.setText("500000");
        txtDayPrice.setText("600000");

        // Initially non-editable
        txtHourPrice.setEditable(false);
        txtNightPrice.setEditable(false);
        txtDayPrice.setEditable(false);

        btnUpdatePrice.addActionListener(e -> {
            // Enable editing
            txtHourPrice.setEditable(true);
            txtNightPrice.setEditable(true);
            txtDayPrice.setEditable(true);

            
            String selectedType = (String) cmbRoomType.getSelectedItem();
            try {
                BigDecimal hour = new BigDecimal(txtHourPrice.getText().replaceAll("[^\\d]", ""));
                BigDecimal night = new BigDecimal(txtNightPrice.getText().replaceAll("[^\\d]", ""));
                BigDecimal day = new BigDecimal(txtDayPrice.getText().replaceAll("[^\\d]", ""));

                JOptionPane.showMessageDialog(this, "Cập nhật giá cho " + selectedType + " thành công!");
                
                txtHourPrice.setEditable(false);
                txtNightPrice.setEditable(false);
                txtDayPrice.setEditable(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

    
        cmbRoomType.addActionListener(e -> {
           
            String selected = (String) cmbRoomType.getSelectedItem();
            if ("Phòng đơn".equals(selected) || "Phòng đôi".equals(selected)) {
                txtHourPrice.setText("120000");
                txtNightPrice.setText("500000");
                txtDayPrice.setText("600000");
            }
            
        });
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
    String cols[] = {"Mã phòng", "Số phòng", "Loại phòng", "Trạng thái", "Chức năng"};
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
                    JOptionPane.showMessageDialog(RoomManagement.this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(RoomManagement.this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        GlassPanePopup.showPopup(modal);
    }

    private void handleDeleteRoom(int row) {
        DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
        Long roomId = (Long) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(RoomManagement.this, "Xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            roomService.deleteRoom(roomId);
            loadTable(roomService.getAllRooms());
            JOptionPane.showMessageDialog(RoomManagement.this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
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

 
    TableColumn colStatus = tblRoom.getTbl().getColumnModel().getColumn(3); // Fixed index for status
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
            } else if (col == 3) { // Fixed for status column
                showComboboxAtHeader(cmbStatus, 3);
            }
        }

       private void showComboboxAtHeader(Combobox<String> cmb, int columnIndex) {
    JTableHeader header = tblRoom.getTbl().getTableHeader();
    Rectangle headerRect = header.getHeaderRect(columnIndex);
    

    Point headerLocation = header.getLocationOnScreen();
    Point tableLocation = tblRoom.getLocationOnScreen();
    
    int x = headerRect.x;
    int y = headerRect.y + headerRect.height;
    

    JPopupMenu popup = new JPopupMenu();
    popup.setLayout(new BorderLayout());
    popup.add(cmb, BorderLayout.CENTER);
    popup.setPreferredSize(new Dimension(headerRect.width, 200));
    
 
    popup.show(header, x, y);
    

    cmb.addActionListener(e -> {
        popup.setVisible(false);
    });
}
    });
}
    /**
     * @param header
     * @param cmb
     * @param col
     */
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
                String status = entry.getStringValue(3); // Fixed index

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
                if (col == 4) { // Action column
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
        btnUpdatePrice = new javax.swing.JButton(); 
        tblRoom = new iuh.fit.se.group1.ui.component.table.Table();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

        // New components for price editing
        lblRoomType = new JLabel("Tên loại phòng:");
        lblRoomType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRoomType = new JComboBox<>();
        lblHourPrice = new JLabel("Giá giờ:");
        lblHourPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHourPrice = new JTextField(10);
        lblNightPrice = new JLabel("Giá đêm:");
        lblNightPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNightPrice = new JTextField(10);
        lblDayPrice = new JLabel("Giá ngày:");
        lblDayPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDayPrice = new JTextField(10);
        btnUpdatePrice.setText("Cập nhật giá");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
                .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE) // New button after add
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblRoomType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHourPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHourPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNightPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNightPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDayPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDayPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tblRoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdatePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE) // New button
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRoomType)
                    .addComponent(cmbRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHourPrice)
                    .addComponent(txtHourPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNightPrice)
                    .addComponent(txtNightPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDayPrice)
                    .addComponent(txtDayPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    JOptionPane.showMessageDialog(RoomManagement.this, "Thêm phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(RoomManagement.this, "Thêm phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        GlassPanePopup.showPopup(modal);
    }

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Export Excel clicked");
    }

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Import Excel clicked");
    }

    private iuh.fit.se.group1.ui.component.custom.Button btnAddRoom;
    private JButton btnUpdatePrice; 
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.table.Table tblRoom;

}