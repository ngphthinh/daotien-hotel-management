package iuh.fit.se.group1.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.CountDownLatch;

public class AdvancedSplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JLabel lblTitle;
    private JLabel lblVersion;
    private Timer timer;
    private int progress = 0;
    private final Color PRIMARY_COLOR = Color.decode("#FCF8ED");
    private final CountDownLatch latch; // đồng bộ với luồng khởi tạo

    public AdvancedSplashScreen(CountDownLatch latch) {
        this.latch = latch;
        initializeComponents();
        setupLayout();
        setupWindow();
        startProgress();
    }

    private void initializeComponents() {
        lblTitle = new JLabel("KHÁCH SẠN ĐÀO TIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTitle.setForeground(Color.decode("#FF6C03"));


        lblVersion = new JLabel("Phiên bản 1.0.0", JLabel.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(Color.decode("#FF6C03"));


        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                int progressWidth = (int) ((double) getValue() / getMaximum() * getWidth());
                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#FF6C03"),
                        progressWidth, 0, Color.decode("#FF6C03"));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 10, 10);
                g2d.dispose();
            }
        };

        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setOpaque(false);
        progressBar.setBorderPainted(false);

        lblStatus = new JLabel("Đang khởi tạo...", JLabel.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStatus.setForeground(Color.decode("#FF6C03"));
    }

    private void setupLayout() {
        JPanel pnlMain = new JPanel();
        pnlMain.setBackground(PRIMARY_COLOR);
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblLogo = new JLabel(new ImageIcon(getClass().getResource("/images/logo64.png")));
        lblLogo.setFont(new Font("Arial", Font.PLAIN, 60));

        JPanel pnlLogo = new JPanel(new FlowLayout());
        pnlLogo.setOpaque(false);
        pnlLogo.add(lblLogo);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblVersion);

        JPanel pnlProgress = new JPanel();
        pnlProgress.setLayout(new BoxLayout(pnlProgress, BoxLayout.Y_AXIS));
        pnlProgress.setOpaque(false);
        pnlProgress.add(Box.createVerticalStrut(30));
        pnlProgress.add(progressBar);
        pnlProgress.add(Box.createVerticalStrut(15));
        pnlProgress.add(lblStatus);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setOpaque(false);
        pnlCenter.add(titlePanel, BorderLayout.CENTER);
        pnlCenter.add(pnlProgress, BorderLayout.SOUTH);

        pnlMain.add(pnlLogo, BorderLayout.NORTH);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        add(pnlMain);
    }

    private void setupWindow() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
    }

    private void startProgress() {
        String[] loadingSteps = {
                "Khởi tạo ứng dụng...",
                "Tải cấu hình...",
                "Kết nối cơ sở dữ liệu...",
                "Tải giao diện...",
                "Chuẩn bị không gian làm việc...",
                "Hoàn tất thiết lập!",
        };

        timer = new Timer(80, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress++;
                progressBar.setValue(progress);
                int stepIndex = (progress - 1) * loadingSteps.length / 100;
                if (stepIndex >= 0 && stepIndex < loadingSteps.length) {
                    lblStatus.setText(loadingSteps[stepIndex]);
                }

                if (progress >= 100) {
                    timer.stop();
                    new Thread(() -> {
                        try {
                            // Đợi luồng khởi tạo hoàn thành
                            latch.await();
                            SwingUtilities.invokeLater(() -> fadeOutAndOpen());
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            }
        });
        timer.start();
    }

    private void fadeOutAndOpen() {
        Timer fadeTimer = new Timer(50, null);
        fadeTimer.addActionListener(new ActionListener() {
            private float opacity = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0) {
                    fadeTimer.stop();
                    dispose();
                    showMainApplication();
                } else {
                    setOpacity(opacity);
                }
            }
        });
        fadeTimer.start();
    }

    private void showMainApplication() {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        CountDownLatch latch = new CountDownLatch(1);

        // Luồng xử lý khởi tạo
        new Thread(() -> {
            try {
                System.out.println("Đang tải dữ liệu ứng dụng...");
                Thread.sleep(3000); // mô phỏng thời gian tải
                System.out.println("Hoàn tất tải dữ liệu!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); // báo hiệu xong
            }
        }).start();

        // Hiển thị splash screen
        SwingUtilities.invokeLater(() -> {
            new AdvancedSplashScreen(latch).setVisible(true);
        });
    }
}
