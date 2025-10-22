/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.modal.ServiceModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

/**
 * @author THIS PC
 */
public class AmenityManagement extends javax.swing.JPanel {

    /**
     * Creates new form AmenityManagement
     */
    public AmenityManagement() {
        initComponents();
        btnAddAmenity.setBackground(new Color(108, 165, 200));
        btnAddAmenity.setForeground(Color.WHITE);
        btnAddAmenity.setBorderRadius(10);
        
        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);
        
        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);

        btnAddAmenity.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        btnImport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_IMPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnExport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_EXPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        String cols[] = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        tblAmenity.getTbl().setModel(model);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                DefaultTableModel model = (DefaultTableModel) tblAmenity.getTbl().getModel();
                String name = (String) model.getValueAt(row, 1);
                String price = String.valueOf(model.getValueAt(row, 2));

                ServiceModal modal = new ServiceModal();
                modal.getBtnSave().setText("Cập nhật");

                modal.getTxtName().setText(name);
                modal.getTxtPrice().setText(price);

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                modal.saveData(ae -> {
                    String nameNew = modal.getTxtName().getText().trim();
                    String priceNew = modal.getTxtPrice().getText().trim();

                    modal.getLblErrolName().setText("");
                    modal.getLblErrolPrice().setText("");

                    Color red = Color.RED;
                    modal.getLblErrolName().setForeground(red);
                    modal.getLblErrolPrice().setForeground(red);

                    boolean valid = true;

                    if (nameNew.isEmpty()) {
                        modal.getLblErrolName().setText("Tên không được để trống!");
                        valid = false;
                    } else if (nameNew.length() < 2) {
                        modal.getLblErrolName().setText("Tên quá ngắn (tối thiểu 2 ký tự)!");
                        valid = false;
                    }

                    double priceI = 0;
                    if (priceNew.isEmpty()) {
                        modal.getLblErrolPrice().setText("Giá không được để trống!");
                        valid = false;
                    } else {
                        try {
                            priceI = Double.parseDouble(priceNew);
                            if (priceI <= 0) {
                                modal.getLblErrolPrice().setText("Giá phải lớn hơn 0!");
                                valid = false;
                            }
                        } catch (NumberFormatException e) {
                            modal.getLblErrolPrice().setText("Giá phải là số hợp lệ!");
                            valid = false;
                        }
                    }

                    if (!valid) {
                        return;
                    }

                    model.setValueAt(nameNew, row, 1);
                    model.setValueAt(priceI, row, 2);
                    GlassPanePopup.closePopupLast();
                });

                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                if (tblAmenity.getTbl().isEditing()) {
                    tblAmenity.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblAmenity.getTbl().getModel();
                model.removeRow(row);
            }
        };
        tblAmenity.setTableActionColumn(tblAmenity.getTbl(), 3, event, false);
        tblAmenity.getTbl().getColumnModel().getColumn(0).setPreferredWidth(200);  // chiều rộng mong muốn
        tblAmenity.getTbl().getColumnModel().getColumn(1).setPreferredWidth(300);
        tblAmenity.getTbl().getColumnModel().getColumn(2).setPreferredWidth(200);
        tblAmenity.getTbl().getColumnModel().getColumn(3).setPreferredWidth(80);

        tblAmenity.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblAmenity.getTbl().columnAtPoint(e.getPoint());
                if (col == 3) {
                    tblAmenity.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblAmenity.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblAmenity.getTbl().clearSelection();
            }
        });
        headerCustom1.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String text = headerCustom1.getSearchText();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTiTle = new javax.swing.JLabel();
        btnAddAmenity = new iuh.fit.se.group1.ui.component.custom.Button();
        tblAmenity = new iuh.fit.se.group1.ui.component.table.Table();
        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

        setBackground(new java.awt.Color(241, 242, 241));

        lblTiTle.setBackground(new java.awt.Color(241, 241, 241));
        lblTiTle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTiTle.setForeground(new java.awt.Color(102, 102, 102));
        lblTiTle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTiTle.setText("Danh sách dịch vụ");

        btnAddAmenity.setText("Thêm dịch vụ");
        btnAddAmenity.setToolTipText("");
        btnAddAmenity.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddAmenity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAmenityActionPerformed(evt);
            }
        });

        tblAmenity.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));
        tblAmenity.setForeground(new java.awt.Color(255, 255, 255));

        btnExport.setText("Xuất Excel");
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnImport.setText("Nhập Excel");
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tblAmenity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(headerCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblTiTle, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(372, 372, 372)
                .addComponent(btnAddAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTiTle, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(tblAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        btnExport.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddAmenityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAmenityActionPerformed

        ServiceModal modal = new ServiceModal();
        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }

        });
        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String name = modal.getTxtName().getText().trim();
                String price = modal.getTxtPrice().getText().trim();

                modal.getLblErrolName().setText("");
                modal.getLblErrolPrice().setText("");

                Color red = Color.RED;
                modal.getLblErrolName().setForeground(red);
                modal.getLblErrolPrice().setForeground(red);

                boolean valid = true;

                if (name.isEmpty()) {
                    modal.getLblErrolName().setText("Tên không được để trống!");
                    valid = false;
                } else if (name.length() < 2) {
                    modal.getLblErrolName().setText("Tên quá ngắn (tối thiểu 2 ký tự)!");
                    valid = false;
                }

                double priceI = 0;
                if (price.isEmpty()) {
                    modal.getLblErrolPrice().setText("Giá không được để trống!");
                    valid = false;
                } else {
                    try {
                        priceI = Double.parseDouble(price);
                        if (priceI <= 0) {
                            modal.getLblErrolPrice().setText("Giá phải lớn hơn 0!");
                            valid = false;
                        }
                    } catch (NumberFormatException e) {
                        modal.getLblErrolPrice().setText("Giá phải là số hợp lệ!");
                        valid = false;
                    }
                }
                if (valid) {
                    DefaultTableModel model = (DefaultTableModel) tblAmenity.getTbl().getModel();
                    model.addRow(new Object[]{"", name, priceI, ""});
                    GlassPanePopup.closePopupLast();
                }
            }
        });
        GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_btnAddAmenityActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddAmenity;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel lblTiTle;
    private iuh.fit.se.group1.ui.component.table.Table tblAmenity;
    // End of variables declaration//GEN-END:variables
}
