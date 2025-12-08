/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.service.AmenityService;
import iuh.fit.se.group1.service.ImportExcelService;
import iuh.fit.se.group1.service.PromotionService;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.modal.InfoPromotionModal;
import iuh.fit.se.group1.ui.component.modal.ServiceModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import iuh.fit.se.group1.util.Constants;

import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import raven.glasspanepopup.GlassPanePopup;
import iuh.fit.se.group1.service.ExportExcelService;

/**
 * @author Windows
 */
public class PromotionManagement extends javax.swing.JPanel {

    private static final Logger log = LoggerFactory.getLogger(PromotionManagement.class);
    private final PromotionService promotionService;

    /**
     * Creates new form PromotionManagement
     */
    public PromotionManagement() {
        initComponents();
        custom();
        promotionService = new PromotionService();
        loadTable(promotionService.getAllPromotions());
    }

    private void loadTable(java.util.List<Promotion> promotions) {
        DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
        model.setRowCount(0);
        for (Promotion promotion : promotions) {
            model.addRow(new Object[]{
                promotion.getPromotionId(),
                promotion.getPromotionName(),
                Constants.VND_FORMAT.format(promotion.getMinOrderAmount()),
                promotion.getDiscountPercent() + "%",
                promotion.getStartDate().format(Constants.DATE_FORMATTER),
                promotion.getEndDate().format(Constants.DATE_FORMATTER),
                promotion.getCreatedAt().format(Constants.DATE_FORMATTER)
            });
        }
    }

    private void custom() {
        btnAddPromotion.setBackground(new Color(108, 165, 200));
        btnAddPromotion.setForeground(Color.WHITE);
        btnAddPromotion.setBorderRadius(10);

        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);

        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);
        btnImport.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImportExcelService importService = new ImportExcelService();
                List<Promotion> imported = importService.importPromotionsFromExcel(file);
                if (imported != null && !imported.isEmpty()) {
                    promotionService.getAllPromotions().addAll(imported);
                    loadTable(promotionService.getAllPromotions());
                    Message.showMessage("Thành công", "Đã import " + imported.size() + " khuyến mãi!");
                } else {
                    Message.showMessage("Lỗi", "Không có dữ liệu nào được import!");
                }
            }
        });

        btnAddPromotion.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        btnImport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_IMPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnExport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_EXPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnExport.addActionListener(e -> {
            ExportExcelService.exportTableToExcel(
                    this,
                    tblPromotion.getTbl(),
                    "Danh sách khuyến mãi",
                    "DanhSachKhuyenMai"
            );
        });

        headerCustom1.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý khuyến mãi</span>");
        headerCustom1.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        String cols[] = {
            "Mã KM",
            "Tên KM",
            "Giá KM",
            "% Giảm",
            "Ngày bắt đầu",
            "Ngày kết thúc",
            "Ngày tạo",
            "Chức năng"

        };
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
                DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
                Long id = (Long) model.getValueAt(row, 0);
                String name = (String) model.getValueAt(row, 1);
                String discountPrice = String.valueOf(model.getValueAt(row, 2));
                String discountPercent = String.valueOf(model.getValueAt(row, 3));
                String startDate = (String) model.getValueAt(row, 4);
                String endDate = (String) model.getValueAt(row, 5);

                InfoPromotionModal modal = new InfoPromotionModal();
                modal.getLblTitle().setText("Cập nhật khuyến mãi");
                modal.getBtnSave().setText("Cập nhật");

                modal.getTxtName().setText(name);
                modal.getTxtPrice().setText(discountPrice);
                modal.getTxtDiscountPersent().setText(discountPercent);
                modal.getTxtStarDate().setText(startDate);
                modal.getTxtEndDate().setText(endDate);

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                modal.saveData(ae -> {
                    String title = "Xác nhận cập nhật khuyến mãi";
                    String message = "Bạn có chắc chắn muốn cập nhật khuyến mãi này không?";
                    Message.showConfirm(title, message, () -> {
                        var result = getValid(modal);
                        if (!result.valid) {
                            return;
                        }

                        Promotion promotion = new Promotion();
                        promotion.setPromotionId(id);
                        promotion.setPromotionName(result.name);
                        promotion.setMinOrderAmount(result.discountPrice);
                        promotion.setDiscountPercent(result.discountPercent);
                        promotion.setDescription(result.description);
                        promotion.setStartDate(result.startDate);
                        promotion.setEndDate(result.endDate);

                        Promotion entitySave = promotionService.updatePromotion(promotion);

                        model.setValueAt(entitySave.getPromotionName(), row, 1);

                        model.setValueAt(Constants.VND_FORMAT.format(entitySave.getMinOrderAmount()), row, 2);
                        model.setValueAt(entitySave.getDiscountPercent() + " %", row, 3);

                        model.setValueAt(entitySave.getMinOrderAmount(), row, 2);
                        model.setValueAt(entitySave.getDiscountPercent(), row, 3);

                        model.setValueAt(entitySave.getStartDate().format(Constants.DATE_FORMATTER), row, 4);
                        model.setValueAt(entitySave.getEndDate().format(Constants.DATE_FORMATTER), row, 5);
                        // Cột 6 là createdAt - không cập nhật
                        // Cột 7 là action column

                        GlassPanePopup.closePopupLast();
                    });
                });

                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                String title = "Xác nhận xóa khuyến mãi";
                String message = "Bạn có chắc chắn muốn xóa khuyến mãi này không?";
                Message.showConfirm(title, message, () -> {
                    JTable table = tblPromotion.getTbl();

                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }

                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int rowDelete = table.getSelectedRow();

                    if (rowDelete >= 0) {
                        Long id = (Long) model.getValueAt(rowDelete, 0);
                        model.removeRow(rowDelete);
                        promotionService.deletePromotion(id);
                    }
                });
            }
        };

        tblPromotion.setTableActionColumn(tblPromotion.getTbl(), 7, event, false);

        // SỬA: Cập nhật độ rộng cho 8 cột
        tblPromotion.getTbl().getColumnModel().getColumn(0).setPreferredWidth(80);
        tblPromotion.getTbl().getColumnModel().getColumn(1).setPreferredWidth(150);
        tblPromotion.getTbl().getColumnModel().getColumn(2).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(3).setPreferredWidth(80);
        tblPromotion.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(5).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(6).setPreferredWidth(100);
        tblPromotion.getTbl().getColumnModel().getColumn(7).setPreferredWidth(90);
        tblPromotion.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblPromotion.getTbl().columnAtPoint(e.getPoint());

                if (col == 7) {
                    tblPromotion.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblPromotion.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        headerCustom1.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = headerCustom1.getSearchText();
                if (text.isEmpty()) {
                    loadTable(promotionService.getAllPromotions());
                    return;
                }
                loadTable(promotionService.getPromotionByKeyword(text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = headerCustom1.getSearchText();
                if (text.isEmpty()) {
                    loadTable(promotionService.getAllPromotions());
                    return;
                }
                loadTable(promotionService.getPromotionByKeyword(text));
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        btnAddPromotion = new iuh.fit.se.group1.ui.component.custom.Button();
        lblTitle = new javax.swing.JLabel();
        tblPromotion = new iuh.fit.se.group1.ui.component.table.Table();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

        setBackground(new java.awt.Color(241, 241, 241));

        btnAddPromotion.setText("Thêm khuyến mãi");
        btnAddPromotion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddPromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPromotionActionPerformed(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(131, 131, 131));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle.setText("Danh sách khuyến mãi");

        tblPromotion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        btnExport.setText("Xuất Excel");
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnImport.setText("Tải excel");
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(lblTitle)
                                .addGap(307, 307, 307)
                                .addComponent(btnAddPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblTitle)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnAddPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(25, 25, 25)
                                .addComponent(tblPromotion, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                                .addGap(31, 31, 31))
        );
    }

    private void btnAddPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPromotionActionPerformed
        InfoPromotionModal modal = new InfoPromotionModal();
        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }
        });

        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveData(modal);
            }
        });

        raven.glasspanepopup.GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_btnAddPromotionActionPerformed

    private void saveData(InfoPromotionModal modal) {
        Valid result = getValid(modal);
        if (result.valid) {
            Promotion promotion = new Promotion();
            promotion.setPromotionName(result.name);
            promotion.setMinOrderAmount(result.discountPrice);
            promotion.setDiscountPercent(result.discountPercent);
            promotion.setDescription(result.description);
            promotion.setStartDate(result.startDate);
            promotion.setEndDate(result.endDate);

            Promotion entitySave = promotionService.createPromotion(promotion);

            DefaultTableModel model = (DefaultTableModel) tblPromotion.getTbl().getModel();
            // SỬA: Thêm đủ 7 cột dữ liệu (cột 8 là action tự động)
            model.addRow(new Object[]{
                entitySave.getPromotionId(), // Cột 0
                entitySave.getPromotionName(), // Cột 1
                Constants.VND_FORMAT.format(promotion.getPromotionName()), // Cột 2
                entitySave.getDiscountPercent() + " %", // Cột 3
                entitySave.getStartDate().format(Constants.DATE_FORMATTER), // Cột 4
                entitySave.getEndDate().format(Constants.DATE_FORMATTER), // Cột 5
                entitySave.getCreatedAt().format(Constants.DATE_FORMATTER), // Cột 6
                entitySave.getPromotionId(), // Cột 0
                entitySave.getPromotionName(), // Cột 1
                entitySave.getMinOrderAmount(), // Cột 2
                entitySave.getDiscountPercent(), // Cột 3
                entitySave.getStartDate().format(Constants.DATE_FORMATTER), // Cột 4
                entitySave.getEndDate().format(Constants.DATE_FORMATTER), // Cột 5
                entitySave.getCreatedAt().format(Constants.DATE_FORMATTER) // Cột 6
            });
            GlassPanePopup.closePopupLast();
        }
    }

    private static Valid getValid(InfoPromotionModal modal) {
        String name = modal.getTxtName().getText().trim();
        String priceStr = modal.getTxtPrice().getText().trim();
        String percentStr = modal.getTxtDiscountPersent().getText().trim();
        String description = modal.getTxtDesciption().getText().trim();
        String startDateStr = modal.getTxtStarDate().getText().trim();
        String endDateStr = modal.getTxtEndDate().getText().trim();

        // Reset error labels
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

        boolean valid = true;

        if (name.isEmpty()) {
            modal.getLblErrolName().setText("Tên không được để trống!");
            valid = false;
        } else if (name.length() < 2) {
            modal.getLblErrolName().setText("Tên quá ngắn (tối thiểu 2 ký tự)!");
            valid = false;
        }

        BigDecimal discountPrice = BigDecimal.ZERO;
        boolean hasPrice = false;
        if (priceStr.isEmpty()) {
            modal.getLblErrolPrice().setText("Giá không được để trống!");
            valid = false;
        } else {
            try {
                discountPrice = new BigDecimal(priceStr);
                if (discountPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    modal.getLblErrolPrice().setText("Giá phải lớn hơn 0!");
                    valid = false;
                } else {
                    hasPrice = true;
                }
            } catch (NumberFormatException e) {
                modal.getLblErrolPrice().setText("Giá phải là số hợp lệ!");
                valid = false;
                log.error("Lỗi chuyển đổi giá khuyến mãi: ", e);
            }
        }

        float discountPercent = 0;
        boolean hasPercent = false;
        if (percentStr.isEmpty()) {
            modal.getLblErrolDiscountPersent().setText("Phần trăm không được để trống!");
            valid = false;
        } else {
            try {
                discountPercent = Float.parseFloat(percentStr);
                if (discountPercent < 0 || discountPercent > 100) {
                    modal.getLblErrolDiscountPersent().setText("Phần trăm phải nằm trong khoảng 0-100!");
                    valid = false;
                } else {
                    hasPercent = true;
                }
            } catch (NumberFormatException e) {
                modal.getLblErrolDiscountPersent().setText("Phần trăm phải là số hợp lệ!");
                valid = false;
                log.error("Lỗi chuyển đổi phần trăm khuyến mãi: ", e);
            }
        }

        if (description.isEmpty()) {
            modal.getLblErrolDesciption().setText("Mô tả không được để trống!");
            valid = false;
        }

        LocalDate startDate = null;
        LocalDate endDate = null;
        if (startDateStr.isEmpty()) {
            modal.getLblErrolStarDate().setText("Ngày bắt đầu không được để trống!");
            valid = false;
        } else {
            try {
                startDate = LocalDate.parse(startDateStr, Constants.DATE_FORMATTER);
            } catch (Exception e) {
                modal.getLblErrolStarDate().setText("Ngày bắt đầu không hợp lệ!");
                valid = false;
                log.error("Lỗi parse ngày bắt đầu: ", e);
            }
        }

        if (endDateStr.isEmpty()) {
            modal.getLblErrolEndDate().setText("Ngày kết thúc không được để trống!");
            valid = false;
        } else {
            try {
                endDate = LocalDate.parse(endDateStr, Constants.DATE_FORMATTER);
            } catch (Exception e) {
                modal.getLblErrolEndDate().setText("Ngày kết thúc không hợp lệ!");
                valid = false;
                log.error("Lỗi parse ngày kết thúc: ", e);
            }
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            modal.getLblErrolEndDate().setText("Ngày kết thúc phải sau ngày bắt đầu!");
            valid = false;
        }

        return new Valid(name, valid, discountPrice, discountPercent, description, startDate, endDate);
    }

    private record Valid(String name,
            boolean valid,
            BigDecimal discountPrice,
            float discountPercent,
            String description,
            LocalDate startDate,
            LocalDate endDate) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddPromotion;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.table.Table tblPromotion;
    // End of variables declaration//GEN-END:variables
}
