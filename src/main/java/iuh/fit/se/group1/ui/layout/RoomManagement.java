/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.modal.ServiceModal;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.modal.RoomManagementModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author THIS PC
 */
public class RoomManagement extends javax.swing.JPanel {

    /**
     * Creates new form RoomManagement
     */
    public RoomManagement() {
        initComponents();
        headerCustom.getjLabel1().setText("Quản lý phòng");
        btnAddRoom.setBackground(new Color(108, 165, 200));
        btnAddRoom.setForeground(Color.WHITE);
        btnAddRoom.setBorderRadius(40);

        btnAddRoom.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);

        String cols[] = {"Mã phòng", "Số phòng", "Loại phòng", "Giá phòng", "Trạng thái", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 5);
        tblRoom.getTbl().setModel(model);
         addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblRoom.getTbl().clearSelection();
            }
        });
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblRoom.getTbl().isEditing()) {
                    tblRoom.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblRoom.getTbl().getModel();
                model.removeRow(row);
            }
        };

        tblRoom.setTableActionColumn(tblRoom.getTbl(), 5, event, false);
        tblRoom.getTbl().getColumnModel().getColumn(0).setPreferredWidth(250);  // chiều rộng mong muốn
        tblRoom.getTbl().getColumnModel().getColumn(1).setPreferredWidth(250);
        tblRoom.getTbl().getColumnModel().getColumn(2).setPreferredWidth(250);
        tblRoom.getTbl().getColumnModel().getColumn(3).setPreferredWidth(250);
        tblRoom.getTbl().getColumnModel().getColumn(4).setPreferredWidth(250);
        tblRoom.getTbl().getColumnModel().getColumn(5).setPreferredWidth(100);

        var header = tblRoom.getTbl().getTableHeader();

        //todo: hard code
        Combobox<String> cmbType = new Combobox<>(new String[]{"Phòng đơn", "Phòng đôi"});
        Combobox<String> cmbStatus = new Combobox<>(new String[]{"Còn trống", "Đang sử dụng", "Bảo trì"});

        TableCellRenderer defaultRenderer = header.getDefaultRenderer();

        for (int i = 0; i < tblRoom.getTbl().getColumnCount(); i++) {
            tblRoom.getTbl().getColumnModel().getColumn(i).setHeaderRenderer(defaultRenderer);
        }

        TableColumn colGender = tblRoom.getTbl().getColumnModel().getColumn(2);
        colGender.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Loại phòng                          \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        TableColumn colPosition = tblRoom.getTbl().getColumnModel().getColumn(4);
        colPosition.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Trạng thái                           \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        cmbType.addActionListener(ev -> {
            String selectedType = (String) cmbType.getSelectedItem();
            String selectedStatus = (String) cmbStatus.getSelectedItem();
//    filterTable(selectedType, selectedStatus);
            header.remove(cmbType);
            header.repaint();
        });

        cmbStatus.addActionListener(ev -> {
            String selectedStatus = (String) cmbStatus.getSelectedItem();
            String selectedType = (String) cmbType.getSelectedItem();
//    filterTable(selectedType, selectedStatus);
            header.remove(cmbStatus);
            header.repaint();
        });

        tblRoom.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblRoom.getTbl().columnAtPoint(e.getPoint());

                if (col == 5) {
                    tblRoom.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblRoom.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblRoom.getTbl().columnAtPoint(e.getPoint());
                if (col == 2) {
                    Rectangle rect = header.getHeaderRect(col);

//                     Thiết lập vị trí và kích thước cho combo
                    cmbType.setBounds(rect);
                    cmbType.setVisible(true); // hiển thị combo tại vị trí cột
                    header.add(cmbType);
                    cmbType.showPopup(); // mở dropdown ngay lập tức

                    // Khi mất focus, ẩn combo
                    cmbType.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmbType.setVisible(false);
                            header.remove(cmbType);
                        }
                    });
                }
                if (col == 4) {
                    Rectangle rect = header.getHeaderRect(col);

//                     Thiết lập vị trí và kích thước cho combo
                    cmbStatus.setBounds(rect);
                    cmbStatus.setVisible(true); // hiển thị combo tại vị trí cột
                    header.add(cmbStatus);
                    cmbStatus.showPopup(); // mở dropdown ngay lập tức

                    // Khi mất focus, ẩn combo
                    cmbStatus.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmbStatus.setVisible(false);
                            header.remove(cmbStatus);
                        }
                    });
                }
            }
        });

        headerCustom.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String text = headerCustom.getSearchText();
                System.out.println("Search text in amenity search: " + text);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom = new iuh.fit.se.group1.ui.component.HeaderCustom();
        lblTitle = new javax.swing.JLabel();
        btnAddRoom = new iuh.fit.se.group1.ui.component.custom.Button();
        tblRoom = new iuh.fit.se.group1.ui.component.table.Table();

        setBackground(new java.awt.Color(241, 241, 241));

        lblTitle.setBackground(new java.awt.Color(131, 131, 131));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Danh sách phòng");

        btnAddRoom.setText("Thêm phòng");
        btnAddRoom.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddRoom.setMaximumSize(new java.awt.Dimension(119, 27));
        btnAddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRoomActionPerformed(evt);
            }
        });

        tblRoom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

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
                .addGap(86, 86, 86))
            .addComponent(tblRoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRoomActionPerformed
        RoomManagementModal modal = new RoomManagementModal();
        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }

        });
        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                modal.getjLabel1().setText("HIihihi");
                GlassPanePopup.closePopupLast();
                modal.getLblErrolNumberRoom().setForeground(Color.red);
                modal.getLblErrolPriceRoom().setForeground(Color.red);
//                System.out.println("Save data" + modal.getServiceName() + " - " + modal.getServicePrice());
            }
        });
        GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_btnAddRoomActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddRoom;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.table.Table tblRoom;
    // End of variables declaration//GEN-END:variables
}
