/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.shift;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.repository.jpa.EmployeeRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

/**
 *
 * @author Administrator
 */
public class ShiftList extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(ShiftList.class);

    /**
     * Creates new form ShiftList
     */
    public ShiftList() {
        initComponents();
        custom();
        loadEmployeesFromDatabase();
    }

    private void custom() {
        setPreferredSize(new Dimension(290, getPreferredSize().height));
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        scrollPaneWin111.setOpaque(false);
        scrollPaneWin111.getViewport().setOpaque(false);
        scrollPaneWin111.setBorder(null);
        scrollPaneWin111.getViewport().setBorder(null);
        scrollPaneWin111.setViewportBorder(null);
        scrollPaneWin111.putClientProperty("JComponent.borderType", "none");
        scrollPaneWin111.setBackground(new Color(255, 255, 255, 0));
        scrollPaneWin111.getViewport().setBackground(new Color(255, 255, 255, 0));

        // 🔧 Panel chứa nhân viên
        pnlEmployees.setLayout(new BoxLayout(pnlEmployees, BoxLayout.Y_AXIS));
        pnlEmployees.setBorder(null);
        pnlEmployees.setAlignmentY(TOP_ALIGNMENT);
        pnlEmployees.setOpaque(false);
        pnlEmployees.setAlignmentX(LEFT_ALIGNMENT);

        lblTitle.setOpaque(false);
        add(lblTitle, BorderLayout.NORTH);
        add(scrollPaneWin111, BorderLayout.CENTER);
    }

    public void reloadEmployees() {
        loadEmployeesFromDatabase();
    }

    public void addNewEmployee(Employee employee) {
        try {
            String name = employee.getFullName();
            String code = String.valueOf(employee.getEmployeeId());

            // Nếu có ảnh base64 trong DB
            BufferedImage image = null;
            try {
                if (employee.getAvt() != null && employee.getAvt().length > 0) {
                    image = ImageIO.read(new ByteArrayInputStream(employee.getAvt()));
                } else {
                    // Ảnh mặc định
                    URL defaultImg = getClass().getResource("/images/meomeo.jpg");
                    if (defaultImg != null) {
                        image = ImageIO.read(defaultImg);
                    }
                }
            } catch (Exception ex) {
                log.error("Error loading image for employee {}", name, ex);
            }

            ShiftProfile shiftProfile = new ShiftProfile();
            shiftProfile.getLblName().setText(name);
            shiftProfile.updateEmployeeCodeLabel(code);
            shiftProfile.setAlignmentX(Component.LEFT_ALIGNMENT);
            shiftProfile.setMaximumSize(new Dimension(303, 72));
            shiftProfile.setMinimumSize(new Dimension(0, 72));

            if (image != null) {
                BufferedImage resized = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 60, 60);
                shiftProfile.getAvatarLabel().setImage(resized);
            }

            pnlEmployees.add(shiftProfile);
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            separator.setMaximumSize(new Dimension(303, 1));
            separator.setForeground(new Color(180, 180, 180));
            separator.setBackground(new Color(180, 180, 180));
            separator.setOpaque(true);
            pnlEmployees.add(separator);

            shiftProfile.addMouseListener(shiftProfile);

            pnlEmployees.revalidate();
            pnlEmployees.repaint();

            // Scroll xuống dưới cùng để thấy nhân viên mới
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPaneWin111.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });

            log.info("Added new employee to shift list: {}", name);
        } catch (Exception ex) {
            log.error("Error adding new employee to shift list: ", ex);
        }
    }
    public void loadEmployees(List<Employee> employees) {
        try {
            pnlEmployees.removeAll();
            pnlEmployees.setAlignmentY(Component.TOP_ALIGNMENT);

            for (Employee e : employees) {
                String name = e.getFullName();
                String code = String.valueOf(e.getEmployeeId());

                BufferedImage image = null;
                try {
                    if (e.getAvt() != null && e.getAvt().length > 0) {
                        image = ImageIO.read(new ByteArrayInputStream(e.getAvt()));
                    } else {
                        URL defaultImg = getClass().getResource("/images/meomeo.jpg");
                        if (defaultImg != null) {
                            image = ImageIO.read(defaultImg);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Error loading image for employee {}", name, ex);
                }

                ShiftProfile shiftProfile = new ShiftProfile();
                shiftProfile.getLblName().setText(name);
                shiftProfile.updateEmployeeCodeLabel(code);
                shiftProfile.setAlignmentX(Component.LEFT_ALIGNMENT);
                shiftProfile.setMaximumSize(new Dimension(303, 72));
                shiftProfile.setMinimumSize(new Dimension(0, 72));

                if (image != null) {
                    BufferedImage resized = Scalr.resize(
                            image,
                            Scalr.Method.QUALITY,
                            Scalr.Mode.FIT_EXACT,
                            60,
                            60
                    );
                    shiftProfile.getAvatarLabel().setImage(resized);
                }

                pnlEmployees.add(shiftProfile);

                JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                separator.setAlignmentX(Component.LEFT_ALIGNMENT);
                separator.setMaximumSize(new Dimension(303, 1));
                separator.setForeground(new Color(180, 180, 180));
                separator.setBackground(new Color(180, 180, 180));
                separator.setOpaque(true);
                pnlEmployees.add(separator);

                shiftProfile.addMouseListener(shiftProfile);
            }

            pnlEmployees.revalidate();
            pnlEmployees.repaint();
            SwingUtilities.invokeLater(() ->
                    scrollPaneWin111.getVerticalScrollBar().setValue(0)
            );

        } catch (Exception ex) {
            log.error("Error loading employees into ShiftList", ex);
        }
    }

    private void loadEmployeesFromDatabase() {
        try {
            EmployeeRepositoryImpl employeeRepositoryImpl = new EmployeeRepositoryImpl();
            List<Employee> employees = employeeRepositoryImpl.findAllByRoleId(Role.RECEPTIONIST.toString());

            pnlEmployees.removeAll();
            pnlEmployees.setAlignmentY(Component.TOP_ALIGNMENT);
            for (Employee e : employees) {
                String name = e.getFullName();
                String code = String.valueOf(e.getEmployeeId());

                // Nếu có ảnh base64 trong DB
                BufferedImage image = null;
                try {
                    if (e.getAvt() != null && e.getAvt().length > 0) {
                        image = ImageIO.read(new ByteArrayInputStream(e.getAvt()));
                    } else {
                        URL defaultImg = getClass().getResource("/images/meomeo.jpg");
                        if (defaultImg != null) {
                            image = ImageIO.read(defaultImg);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Error loading image for employee {}", name, ex);
                }
                ShiftProfile shiftProfile = new ShiftProfile();
                shiftProfile.getLblName().setText(name);
                shiftProfile.updateEmployeeCodeLabel(code);
                shiftProfile.setAlignmentX(Component.LEFT_ALIGNMENT);
                shiftProfile.setMaximumSize(new Dimension(303, 72));
                shiftProfile.setMinimumSize(new Dimension(0, 72));

                if (image != null) {
                    BufferedImage resized = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 60, 60);
                    shiftProfile.getAvatarLabel().setImage(resized);
                }

                pnlEmployees.add(shiftProfile);
                JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                separator.setAlignmentX(Component.LEFT_ALIGNMENT);
                separator.setMaximumSize(new Dimension(303, 1));
                separator.setForeground(new Color(180, 180, 180));
                separator.setBackground(new Color(180, 180, 180));
                separator.setOpaque(true);
                pnlEmployees.add(separator);

                shiftProfile.addMouseListener(shiftProfile);
            }

            pnlEmployees.revalidate();
            pnlEmployees.repaint();
            SwingUtilities.invokeLater(() -> {
                scrollPaneWin111.getVerticalScrollBar().setValue(0);
            });
        } catch (Exception ex) {
            log.error("Error loading employees: ", ex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 5;
        int shadow = 6;

        // 🎨 Bóng mờ phía sau
        for (int i = shadow; i > 0; i--) {
            float alpha = 0.05f * (shadow - i + 1);
            g2.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
            g2.fillRoundRect(i, i, w - i * 2, h - i * 2, arc, arc);
        }

        // 🎨 Nền trắng
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - shadow, h - shadow, arc, arc);

        g2.dispose();
    }

    public List<ShiftProfile> getSelectedEmployees() {
        List<ShiftProfile> selectedList = new ArrayList<>();

        for (Component c : pnlEmployees.getComponents()) {
            if (c instanceof ShiftProfile) {
                ShiftProfile profile = (ShiftProfile) c;
                if (profile.isSelected()) {
                    selectedList.add(profile);
                }
            }
        }

        return selectedList;
    }

    // ✅ Method để bỏ chọn tất cả radio button
    public void clearAllSelections() {
        for (Component c : pnlEmployees.getComponents()) {
            if (c instanceof ShiftProfile) {
                ShiftProfile profile = (ShiftProfile) c;
                profile.getjRadioButton2().setSelected(false);
                profile.setIsSelect(false);
                profile.updateBackground();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        scrollPaneWin111 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        pnlEmployees = new javax.swing.JPanel();

        setBackground(new java.awt.Color(241, 241, 241));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Danh sách nhân viên");

        pnlEmployees.setLayout(new javax.swing.BoxLayout(pnlEmployees, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneWin111.setViewportView(pnlEmployees);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(lblTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(12, 12, 12)
                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void addEmployees(String employeeName, String code, String imagePath) {
        ShiftProfile shiftProfile = new ShiftProfile();
        shiftProfile.getLblName().setText(employeeName);
        shiftProfile.updateEmployeeCodeLabel(code);
        // todo:
        try {
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                BufferedImage original = ImageIO.read(url);
                // Resize mượt
                BufferedImage avatar = Scalr.resize(original, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 60, 60);
                shiftProfile.getAvatarLabel().setImage(avatar); // ✅ Gán ảnh vào AvatarLabel
                log.info("Loaded image from resources: {}", imagePath);
            } else {
                log.error("Image not found at path: {}", imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pnlEmployees.add(shiftProfile);
        JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // độ cao 1px, full width
        pnlEmployees.add(line);
        int profileHeight = 80;
        int lineHeight = 1;

        // Đếm số lượng phần tử trong panel
        int componentCount = pnlEmployees.getComponentCount();

        // Tính tổng chiều cao: mỗi 2 component là 1 profile + 1 line
        int pairCount = componentCount / 2;
        int contentHeight = pairCount * (profileHeight + lineHeight);

        // Nếu cuối cùng không có line (thêm nhân viên cuối), có thể +80
        if (componentCount % 2 != 0) {
            contentHeight += profileHeight;
        }

        // Cập nhật kích thước panel
        pnlEmployees.setPreferredSize(new Dimension(pnlEmployees.getWidth(), contentHeight));
        scrollPaneWin111.setViewportView(pnlEmployees);
        pnlEmployees.revalidate();
        pnlEmployees.repaint();
        shiftProfile.addMouseListener(shiftProfile);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlEmployees;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin111;
    // End of variables declaration//GEN-END:variables
}
