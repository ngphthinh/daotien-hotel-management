/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.modal.InfoPromotionModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Windows
 */
public class PromotionManagement extends javax.swing.JPanel {

    /**
     * Creates new form PromotionManagement
     */
    public PromotionManagement() {
        initComponents();
        button1.setBackground(new Color(108, 165, 200));
        button1.setForeground(Color.WHITE);
        button1.setBorderRadius(40);
        button1.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        headerCustom1.getjLabel1().setText(
                "<html><span style='color:white;'>Quản lý khuyến mãi</span>");
        headerCustom1.getjLabel1().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        String cols[] = {"Mã khuyến mãi", "Tên khuyến mãi", "Giá khuyến mãi", "Ngày tạo", "Ngày hết hạn", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        tblPromotion.getTbl().setModel(model);
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblPromotion.getTbl().clearSelection();
            }
        });
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblPromotion.getTbl().isEditing()) {
                    tblPromotion.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
                model.removeRow(row);
            }
        };
        tblPromotion.setTableActionColumn(tblPromotion.getTbl(), 5, new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();

                String code = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                String price = model.getValueAt(row, 2).toString();
                String startDate = model.getValueAt(row, 3).toString();
                String endDate = model.getValueAt(row, 4).toString();

                InfoPromotionModal modal = new InfoPromotionModal();

                modal.getBtnSave().setText("Cập nhật");

                modal.getTxtName().setText(name);
                modal.getTxtPrice().setText(price);
                modal.getTxtStarDate().setText(startDate);
                modal.getTxtEndDate().setText(endDate);

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                modal.saveData(ae -> {
                    String nameNew = modal.getTxtName().getText().trim();
                    String priceNew = modal.getTxtPrice().getText().trim();
                    String discountPersentNew = modal.getTxtDiscountPersent().getText().trim();
                    String desciptionNew = modal.getTxtDesciption().getText().trim();
                    String startDateNew = modal.getTxtStarDate().getText().trim();
                    String endDateNew = modal.getTxtEndDate().getText().trim();

                    modal.getLblErrolName().setText("");
                    modal.getLblErrolPrice().setText("");
                    modal.getLblErrolDiscountPersent().setText("");
                    modal.getLblErrolDesciption().setText("");
                    modal.getLblErrolStarDate().setText("");
                    modal.getLblErrolEndDate().setText("");

                    Color red = Color.RED;
                    modal.getLblErrolName().setForeground(red);
                    modal.getLblErrolPrice().setForeground(red);
                    modal.getLblErrolDiscountPersent().setForeground(red);
                    modal.getLblErrolDesciption().setForeground(red);
                    modal.getLblErrolStarDate().setForeground(red);
                    modal.getLblErrolEndDate().setForeground(red);

                    boolean isValid = true;

                    if (nameNew.isEmpty()) {
                        modal.getLblErrolName().setText("Vui lòng nhập tên khuyến mãi!");
                        isValid = false;
                    }
                    double priceI = 0;
                    if (priceNew.isEmpty()) {
                        modal.getLblErrolPrice().setText("Giá không được để trống!");
                        isValid = false;
                    } else {
                        try {
                            priceI = Double.parseDouble(priceNew);
                            if (priceI <= 0) {
                                modal.getLblErrolPrice().setText("Giá phải lớn hơn 0!");
                                isValid = false;
                            }
                        } catch (NumberFormatException e) {
                            modal.getLblErrolPrice().setText("Giá phải là số hợp lệ!");
                            isValid = false;
                        }
                    }
                    double percent = 0;
                    if (discountPersentNew.isEmpty()) {
                        modal.getLblErrolDiscountPersent().setText("Không được trống!");
                        isValid = false;
                    } else {
                        try {
                            percent = Double.parseDouble(discountPersentNew);
                            if (percent < 0 || percent > 100) {
                                modal.getLblErrolDiscountPersent().setText("Trong khoảng 0–100!");
                                isValid = false;
                            }
                        } catch (NumberFormatException e) {
                            modal.getLblErrolDiscountPersent().setText("Là số hợp lệ!");
                            isValid = false;
                        }
                    }
                    if (desciptionNew.isEmpty()) {
                        modal.getLblErrolDesciption().setText("Vui lòng nhập mô tả khuyến mãi!");
                        isValid = false;
                    } else if (desciptionNew.length() < 10) {
                        modal.getLblErrolDesciption().setText("Mô tả phải ít nhất 10 ký tự!");
                        isValid = false;
                    }
                    if (endDateNew.compareTo(startDateNew) < 0) {
                        modal.getLblErrolEndDate().setText("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
                        isValid = false;
                    }
                    if (!isValid) {
                        return;
                    }

                    model.setValueAt(nameNew, row, 1);
                    model.setValueAt(priceNew, row, 2);
                    model.setValueAt(startDateNew, row, 3);
                    model.setValueAt(endDateNew, row, 4);
                    tblPromotion.getTbl().repaint();
tblPromotion.getTbl().revalidate();

                    GlassPanePopup.closePopupLast();
                });

                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                if (tblPromotion.getTbl().isEditing()) {
                    tblPromotion.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
                String code = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                String price = model.getValueAt(row, 2).toString();
                String startDate = model.getValueAt(row, 3).toString();
                String endDate = model.getValueAt(row, 4).toString();

                InfoPromotionModal modal = new InfoPromotionModal();
                modal.getBtnSave().setText("Xong");
                modal.getTxtName().setText(name);
                modal.getTxtPrice().setText(price);
                modal.getTxtStarDate().setText(startDate);
                modal.getTxtEndDate().setText(endDate);

                modal.getTxtName().setEditable(false);
                modal.getTxtName().setEditable(false);
                modal.getTxtPrice().setEditable(false);
                modal.getTxtStarDate().setEditable(false);
                modal.getTxtEndDate().setEditable(false);
                modal.getTxtDiscountPersent().setEditable(false);
                modal.getTxtDesciption().setEditable(false);

                modal.saveData(ae -> GlassPanePopup.closePopupLast());
                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                GlassPanePopup.showPopup(modal);
            }
        }, true);

        tblPromotion.getTbl().getColumnModel().getColumn(0).setPreferredWidth(100);  // chiều rộng mong muốn
        tblPromotion.getTbl().getColumnModel().getColumn(1).setPreferredWidth(150);
        tblPromotion.getTbl().getColumnModel().getColumn(2).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(3).setPreferredWidth(120);
        tblPromotion.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(5).setPreferredWidth(90);
        tblPromotion.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblPromotion.getTbl().columnAtPoint(e.getPoint());

                if (col == 5) {
                    tblPromotion.getTbl().setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblPromotion.getTbl().setCursor(Cursor.getDefaultCursor());
                }
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

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        button1 = new iuh.fit.se.group1.ui.component.custom.Button();
        jLabel1 = new javax.swing.JLabel();
        tblPromotion = new iuh.fit.se.group1.ui.component.table.Table();

        setBackground(new java.awt.Color(241, 241, 241));

        button1.setText("Thêm khuyến mãi");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(131, 131, 131));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Danh sách khuyến mãi");

        tblPromotion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
            .addComponent(headerCustom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tblPromotion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblPromotion, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addGap(31, 31, 31))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        var modal = new InfoPromotionModal();
        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                raven.glasspanepopup.GlassPanePopup.closePopupLast();
            }
        });

        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String name = modal.getTxtName().getText().trim();
                String price = modal.getTxtPrice().getText().trim();
                String discountPersent = modal.getTxtDiscountPersent().getText().trim();
                String desciption = modal.getTxtDesciption().getText().trim();
                String startDate = modal.getTxtStarDate().getText().trim();
                String endDate = modal.getTxtEndDate().getText().trim();

                modal.getLblErrolName().setText("");
                modal.getLblErrolPrice().setText("");
                modal.getLblErrolDiscountPersent().setText("");
                modal.getLblErrolDesciption().setText("");
                modal.getLblErrolStarDate().setText("");
                modal.getLblErrolEndDate().setText("");

                Color red = Color.RED;
                modal.getLblErrolName().setForeground(red);
                modal.getLblErrolPrice().setForeground(red);
                modal.getLblErrolDiscountPersent().setForeground(red);
                modal.getLblErrolDesciption().setForeground(red);
                modal.getLblErrolStarDate().setForeground(red);
                modal.getLblErrolEndDate().setForeground(red);

                boolean isValid = true;

                if (name.isEmpty()) {
                    modal.getLblErrolName().setText("Vui lòng nhập tên khuyến mãi!");
                    isValid = false;
                }
                double priceI = 0;
                if (price.isEmpty()) {
                    modal.getLblErrolPrice().setText("Giá không được để trống!");
                    isValid = false;
                } else {
                    try {
                        priceI = Double.parseDouble(price);
                        if (priceI <= 0) {
                            modal.getLblErrolPrice().setText("Giá phải lớn hơn 0!");
                            isValid = false;
                        }
                    } catch (NumberFormatException e) {
                        modal.getLblErrolPrice().setText("Giá phải là số hợp lệ!");
                        isValid = false;
                    }
                }
                double percent = 0;
                if (discountPersent.isEmpty()) {
                    modal.getLblErrolDiscountPersent().setText("Không được trống!");
                    isValid = false;
                } else {
                    try {
                        percent = Double.parseDouble(discountPersent);
                        if (percent < 0 || percent > 100) {
                            modal.getLblErrolDiscountPersent().setText("Trong khoảng 0–100!");
                            isValid = false;
                        }
                    } catch (NumberFormatException e) {
                        modal.getLblErrolDiscountPersent().setText("Là số hợp lệ!");
                        isValid = false;
                    }
                }
                if (desciption.isEmpty()) {
                    modal.getLblErrolDesciption().setText("Vui lòng nhập mô tả khuyến mãi!");
                    isValid = false;
                } else if (desciption.length() < 10) {
                    modal.getLblErrolDesciption().setText("Mô tả phải ít nhất 10 ký tự!");
                    isValid = false;
                }
                if (endDate.compareTo(startDate) < 0) {
                    modal.getLblErrolEndDate().setText("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
                    isValid = false;
                }
                if (!isValid) {
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
                model.addRow(new Object[]{"", name, price, startDate, endDate, ""});

                GlassPanePopup.closePopupLast();
            }

        });

        raven.glasspanepopup.GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button button1;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel jLabel1;
    private iuh.fit.se.group1.ui.component.table.Table tblPromotion;
    // End of variables declaration//GEN-END:variables
}
