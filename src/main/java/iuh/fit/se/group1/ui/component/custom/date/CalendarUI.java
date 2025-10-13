/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.custom.date;

import iuh.fit.se.group1.ui.component.custom.Button;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.w3c.dom.events.MouseEvent;

/**
 * @author THIS PC
 */
public class CalendarUI extends JPanel {

    private Calendar cal = Calendar.getInstance();
    private static final Font FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public CalendarUI() {
        setLayout(new BorderLayout());
        JPanel week = new JPanel(new GridLayout(1, 7, 3, 3));
        week.setBackground(Color.WHITE);
        for (String d : new String[]{"CN", "T2", "T3", "T4", "T5", "T6", "T7"})
            week.add(label(d, 12, Font.BOLD, Color.GRAY));

        JPanel days = new JPanel(new GridLayout(6, 7, 3, 3));
        days.setBackground(Color.WHITE);
        for (int i = 0; i < 42; i++) {
            Button b = new Button();
            b.setPreferredSize(new Dimension(35, 35));
            b.setFont(FONT);
            basicBtn(b);
            b.addActionListener(e -> selectDay(b));
            dayBtns[i] = b;
            days.add(b);
        }

        add(week, BorderLayout.NORTH);
        add(days, BorderLayout.CENTER);
        refreshCalendar();
    }

    public void setCalendar(Calendar cal) {
        this.cal = cal;
        refreshCalendar();
    }

    private void selectDay(JButton b) {
        if (updating || b.getText().isEmpty())
            return;
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(b.getText()));
        refreshCalendar();
    }

    private JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    private boolean updating;
    private int selectedDay = -1;

    private void refreshCalendar() {
        updating = true;

        Calendar temp = (Calendar) cal.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int start = (temp.get(Calendar.DAY_OF_WEEK) + 6) % 7; // Monday=0
        int max = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 42; i++) {
            JButton b = dayBtns[i];

            if (i < start || i >= start + max) {
                b.setText("");
                b.setEnabled(false);
                b.setVisible(false);
            } else {
                int day = i - start + 1;
                b.setText(String.valueOf(day));
                b.setEnabled(true);
                b.setVisible(true);
                if (day == cal.get(Calendar.DAY_OF_MONTH)) {
                    b.setBackground(new Color(180, 180, 180));
                    b.setForeground(Color.RED);
                } else {
                    b.setBackground(Color.WHITE);
                    b.setForeground(Color.BLACK);
                }
            }
        }
//        updateTime();
        updating = false;
    }

    private final Button[] dayBtns = new Button[42];

    private void basicBtn(AbstractButton b) {
        b.setBorderPainted(false);
        b.setBorder(null);
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (b.isEnabled() && dragMode) {
                    selectedDay = Integer.parseInt(b.getText());
                    cal.set(Calendar.DAY_OF_MONTH, selectedDay);
                    refreshCalendar();
                } else if (b.isEnabled()) {
                    b.setBackground(new Color(141, 141, 141));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (b.isEnabled() && !dragMode) {
                    b.setBackground(Color.WHITE);
                }
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (b.isEnabled()) {
                    dragMode = true;
                    selectedDay = Integer.parseInt(b.getText());
                    cal.set(Calendar.DAY_OF_MONTH, selectedDay);
                    refreshCalendar();
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                dragMode = false;
            }
        });
    }
    private boolean dragMode = false;
}
