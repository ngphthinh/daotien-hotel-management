/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;


import iuh.fit.se.group1.ui.component.booking.InfoAmenityPanel;
import iuh.fit.se.group1.ui.component.booking.InfoRoomPanel;


import javax.management.monitor.StringMonitor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author THIS PC
 */
public class BookingPage extends javax.swing.JPanel {

    private int roomCount = 1;

    private List<Map<String, String>> bookingRooms;


    /**
     * Creates new form BookingPage
     */
    public BookingPage() {
        initComponents();
        headerBooking1.getBtn().setText("Đặt phòng");
        addRoomPanel1.addAction(a ->
                addNewRoom()
        );
        infoRoomPanel1.getTxtRoomId().setText(roomCount + "hihi");
        infoRoomPanel1.getBtnClose().setVisible(false);
        headerBooking1.addAction(e -> bookingRooms = getBookingRooms());

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
    }

    private List<Map<String, String>> getBookingRooms() {
        List<Map<String, String>> rooms = new ArrayList<>();
        for (Component comp : pnlListRoom.getComponents()) {
            if (comp instanceof InfoRoomPanel roomPanel) {
                String roomId = roomPanel.getTxtRoomId().getText().trim();
                String startDate = roomPanel.getTxtStartDate().getText().trim();
                String endDate = roomPanel.getTxtEndDate().getText().trim();
                if (!roomId.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                    rooms.add(Map.of(
                            "roomId", roomId,
                            "startDate", startDate,
                            "endDate", endDate
                    ));
                }
            }
        }
        return rooms;
    }

    private void addNewAmenity(String amenityId, String amenityName, String amenityPrice) {
        InfoAmenityPanel newAmenity = new InfoAmenityPanel();
        newAmenity.setAmenityId(amenityId);
        newAmenity.getLblName().setText(amenityName);
        newAmenity.getLblPrice().setText(amenityPrice);
        newAmenity.getNumberSpinner().addMinusActionListener(e -> {
            if (newAmenity.getNumberSpinner().getValue() <= 1) {
                closeAmenityCard(newAmenity, pnlListAmenityMain);
            } else {
                newAmenity.getNumberSpinner().setValue(newAmenity.getNumberSpinner().getValue() - 1);
            }
        });
        newAmenity.getNumberSpinner().setValue(1);
        newAmenity.getNumberSpinner().addPlusActionListener(e -> newAmenity.getNumberSpinner().setValue(newAmenity.getNumberSpinner().getValue() + 1));

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
        newRoom.getTxtRoomId().setText(roomCount + "hihi");

        newRoom.closeRoomCard(e -> closeRoomCard(newRoom));

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

    public void closeAmenityCard (InfoAmenityPanel amenityPanel, JPanel parentPanel) {
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

        jPanel2.setBackground(new java.awt.Color(217, 217, 217));
        jPanel2.setForeground(new java.awt.Color(217, 217, 217));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        listAmenity1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.add(listAmenity1);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(infoCustomerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlListRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlListAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(231, Short.MAX_VALUE))
        );

        scrollPaneWin111.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
            .addComponent(headerBooking1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(headerBooking1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE))
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
