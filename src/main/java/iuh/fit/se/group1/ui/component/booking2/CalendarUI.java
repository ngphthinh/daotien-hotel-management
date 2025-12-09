package iuh.fit.se.group1.ui.component.booking2;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Beautiful Calendar UI component with date and time picker
 */
public class CalendarUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color ACCENT_COLOR = new Color(40, 167, 69);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color WHITE = Color.WHITE;

    private LocalDate currentDate;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private JTextField targetTextField;
    private DateTimeFormatter formatter;
    private java.util.function.Consumer<String> onDateSelectedCallback;
    private boolean showTimeSelector;

    // UI Components
    private JLabel monthYearLabel;
    private JPanel calendarPanel;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JButton[] dayButtons;
    private JButton selectedButton;

    public CalendarUI(JFrame parent, JTextField textField) {
        this(parent, textField, null, true);
    }

    public CalendarUI(JFrame parent, JTextField textField, java.util.function.Consumer<String> callback) {
        this(parent, textField, callback, true);
    }

    public CalendarUI(JFrame parent, JTextField textField, java.util.function.Consumer<String> callback,
            boolean showTimeSelector) {
        super(parent, showTimeSelector ? "Chọn ngày và giờ" : "Chọn ngày", true);
        this.targetTextField = textField;
        this.onDateSelectedCallback = callback;
        this.showTimeSelector = showTimeSelector;
        this.currentDate = LocalDate.now();
        this.selectedDate = LocalDate.now();
        this.selectedTime = LocalTime.now();
        this.formatter = showTimeSelector ? DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                : DateTimeFormatter.ofPattern("dd/MM/yyyy");

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateCalendar();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // Month/Year navigation
        monthYearLabel = new JLabel();
        monthYearLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        monthYearLabel.setForeground(PRIMARY_COLOR);
        monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Calendar grid
        calendarPanel = new JPanel(new GridLayout(7, 7, 3, 3));
        calendarPanel.setBackground(WHITE);
        calendarPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Time spinners
        hourSpinner = new JSpinner(new SpinnerNumberModel(selectedTime.getHour(), 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(60, 35));
        styleSpinner(hourSpinner);

        minuteSpinner = new JSpinner(new SpinnerNumberModel(selectedTime.getMinute(), 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(60, 35));
        styleSpinner(minuteSpinner);

        dayButtons = new JButton[42]; // 6 weeks * 7 days
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel with navigation
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Calendar panel
        add(calendarPanel, BorderLayout.CENTER);

        // Time and action panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Background
        getContentPane().setBackground(WHITE);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 10, 15));

        // Previous month button
        JButton prevButton = createNavButton("left");
        prevButton.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        // Next month button
        JButton nextButton = createNavButton("right");
        nextButton.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        panel.add(prevButton, BorderLayout.WEST);
        panel.add(monthYearLabel, BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.EAST);

        return panel;
    }

    private JButton createNavButton(String text) {

        Icon icon;
        if (text.equals("left")) {
            icon = FontIcon.of(FontAwesomeSolid.ARROW_LEFT);
        } else {
            icon = FontIcon.of(FontAwesomeSolid.ARROW_RIGHT);
        }

        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(WHITE);
            }
        });

        return button;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        // Time panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        timePanel.setBackground(WHITE);

        JLabel timeLabel = new JLabel("Thời gian:");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(SECONDARY_COLOR);

        JLabel hourLabel = new JLabel("Giờ:");
        hourLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel minuteLabel = new JLabel("Phút:");
        minuteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        timePanel.add(timeLabel);
        timePanel.add(hourLabel);
        timePanel.add(hourSpinner);
        timePanel.add(minuteLabel);
        timePanel.add(minuteSpinner);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(WHITE);

        // Only show "Bây giờ" button if time selector is enabled
        if (showTimeSelector) {
            JButton nowButton = createActionButton("Bây giờ", new Color(255, 193, 7)); // Yellow/Orange color
            nowButton.addActionListener(e -> selectCurrentDateTime());
            actionPanel.add(nowButton);
        }

        JButton cancelButton = createActionButton("Hủy", SECONDARY_COLOR);
        cancelButton.addActionListener(e -> dispose());

        JButton confirmButton = createActionButton("Chọn", ACCENT_COLOR);
        confirmButton.addActionListener(e -> confirmSelection());

        actionPanel.add(cancelButton);
        actionPanel.add(confirmButton);

        // Add timePanel nếu showTimeSelector = true
        if (showTimeSelector) {
            panel.add(timePanel, BorderLayout.CENTER);
            panel.add(actionPanel, BorderLayout.SOUTH);
        } else {
            panel.add(actionPanel, BorderLayout.CENTER);
        }

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void setupEventHandlers() {
        // Close on ESC
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Confirm on ENTER
        getRootPane().registerKeyboardAction(
                e -> confirmSelection(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // Update month/year label
        String monthYear = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("vi-VN")) +
                " " + currentDate.getYear();
        monthYearLabel.setText(monthYear.substring(0, 1).toUpperCase() + monthYear.substring(1));

        // Add day headers
        String[] dayNames = { "CN", "T2", "T3", "T4", "T5", "T6", "T7" };
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dayLabel.setForeground(SECONDARY_COLOR);
            dayLabel.setPreferredSize(new Dimension(50, 35));
            dayLabel.setBackground(new Color(240, 242, 247));
            dayLabel.setOpaque(true);
            dayLabel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
            calendarPanel.add(dayLabel);
        }

        // Calculate first day of month and days in month
        LocalDate firstDay = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Sunday = 0
        int daysInMonth = currentDate.lengthOfMonth();

        int dayCounter = 1;

        // Add calendar day buttons
        for (int i = 0; i < 42; i++) {
            JButton dayButton = new JButton();
            dayButton.setPreferredSize(new Dimension(50, 40));
            dayButton.setMinimumSize(new Dimension(50, 40));
            dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            dayButton.setFocusPainted(false);
            dayButton.setBorderPainted(false);
            dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dayButton.setMargin(new Insets(2, 2, 2, 2));

            if (i >= firstDayOfWeek && dayCounter <= daysInMonth) {
                // Valid day in current month
                dayButton.setText(String.valueOf(dayCounter));
                dayButton.setBackground(WHITE);
                dayButton.setForeground(Color.BLACK);

                LocalDate buttonDate = currentDate.withDayOfMonth(dayCounter);

                // Highlight today
                if (buttonDate.equals(LocalDate.now())) {
                    dayButton.setBackground(new Color(230, 240, 255));
                    dayButton.setForeground(PRIMARY_COLOR);
                    dayButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    dayButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
                }

                // Highlight selected date
                if (buttonDate.equals(selectedDate)) {
                    dayButton.setBackground(PRIMARY_COLOR);
                    dayButton.setForeground(WHITE);
                    dayButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    dayButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 2));
                    selectedButton = dayButton;
                }

                // Add click handler
                final int day = dayCounter;
                dayButton.addActionListener(e -> selectDate(day));

                // Hover effect
                dayButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!dayButton.getBackground().equals(PRIMARY_COLOR)) {
                            dayButton.setBackground(new Color(245, 248, 255));
                            dayButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.brighter(), 1));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!dayButton.getBackground().equals(PRIMARY_COLOR)) {
                            if (buttonDate.equals(LocalDate.now())) {
                                dayButton.setBackground(new Color(230, 240, 255));
                                dayButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
                            } else {
                                dayButton.setBackground(WHITE);
                                dayButton.setBorder(null);
                            }
                        }
                    }
                });

                dayCounter++;
            } else {
                // Empty cell
                dayButton.setVisible(false);
            }

            dayButtons[i] = dayButton;
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void selectDate(int day) {
        // Reset previous selected button
        if (selectedButton != null) {
            selectedButton.setBackground(WHITE);
            selectedButton.setForeground(Color.BLACK);
            selectedButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            selectedButton.setBorder(null);

            // Keep today highlight
            LocalDate buttonDate = currentDate.withDayOfMonth(Integer.parseInt(selectedButton.getText()));
            if (buttonDate.equals(LocalDate.now())) {
                selectedButton.setBackground(new Color(230, 240, 255));
                selectedButton.setForeground(PRIMARY_COLOR);
                selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                selectedButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
            }
        }

        // Set new selected date
        selectedDate = currentDate.withDayOfMonth(day);

        // Update selected button appearance
        for (JButton button : dayButtons) {
            if (button != null && button.isVisible() &&
                    String.valueOf(day).equals(button.getText())) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(WHITE);
                button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                button.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 2));
                selectedButton = button;
                break;
            }
        }
    }

    private void selectCurrentDateTime() {
        // Set current date and time
        LocalDateTime now = LocalDateTime.now();
        selectedDate = now.toLocalDate();
        selectedTime = now.toLocalTime();

        // Update current date to show current month
        currentDate = selectedDate;

        // Update spinners
        hourSpinner.setValue(selectedTime.getHour());
        minuteSpinner.setValue(selectedTime.getMinute());

        // Update calendar display
        updateCalendar();
    }

    private void confirmSelection() {
        String formattedDateTime;

        if (showTimeSelector) {
            // Get selected time
            int hour = (Integer) hourSpinner.getValue();
            int minute = (Integer) minuteSpinner.getValue();
            selectedTime = LocalTime.of(hour, minute);

            // Combine date and time
            LocalDateTime selectedDateTime = selectedDate.atTime(selectedTime);
            formattedDateTime = selectedDateTime.format(formatter);
        } else {
            // Date only
            formattedDateTime = selectedDate.format(formatter);
        }

        // Set value to target text field if available
        if (targetTextField != null) {
            targetTextField.setText(formattedDateTime);
        }

        // Call callback if available
        if (onDateSelectedCallback != null) {
            onDateSelectedCallback.accept(formattedDateTime);
        }

        // Close dialog
        dispose();
    }

    /**
     * Static method to show calendar popup for a text field
     */
    public static void showCalendar(JTextField textField, JFrame parent) {
        showCalendar(textField, parent, null);
    }

    /**
     * Static method to show calendar popup for a text field with callback
     * 
     * @param textField      Target text field (can be null if only using callback)
     * @param parent         Parent frame
     * @param onDateSelected Callback function that receives the formatted date
     *                       string
     */
    public static void showCalendar(JTextField textField, JFrame parent,
            java.util.function.Consumer<String> onDateSelected) {
        showCalendar(textField, parent, onDateSelected, true);
    }

    /**
     * Static method to show calendar popup for a text field with callback and time
     * selector option
     * 
     * @param textField        Target text field (can be null if only using
     *                         callback)
     * @param parent           Parent frame
     * @param onDateSelected   Callback function that receives the formatted date
     *                         string
     * @param showTimeSelector Whether to show time selection controls
     */
    public static void showCalendar(JTextField textField, JFrame parent,
            java.util.function.Consumer<String> onDateSelected, boolean showTimeSelector) {
        CalendarUI calendar = new CalendarUI(parent, textField, onDateSelected, showTimeSelector);

        // Try to parse existing value in text field
        String existingValue = textField != null ? textField.getText().trim() : "";
        if (!existingValue.isEmpty()) {
            try {
                if (showTimeSelector && existingValue.contains(" ")) {
                    // Parse date with time
                    LocalDateTime existing = LocalDateTime.parse(existingValue,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    calendar.selectedDate = existing.toLocalDate();
                    calendar.selectedTime = existing.toLocalTime();
                    calendar.hourSpinner.setValue(calendar.selectedTime.getHour());
                    calendar.minuteSpinner.setValue(calendar.selectedTime.getMinute());
                } else {
                    // Parse date only
                    calendar.selectedDate = LocalDate.parse(existingValue,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                calendar.currentDate = calendar.selectedDate;
                calendar.updateCalendar();
            } catch (Exception e) {
                // Ignore parsing errors, use default values
            }
        }

        calendar.setVisible(true);
    }
}