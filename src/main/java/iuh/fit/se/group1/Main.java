package iuh.fit.se.group1;

import iuh.fit.se.group1.config.InitData;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import iuh.fit.se.group1.network.client.AppSocketManager;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.ui.swing.AdvancedSplashScreen;


import javax.swing.*;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;


public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
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

        // Initialize Socket Client Connection
        System.out.println("Initializing socket connection...");
        if (AppSocketManager.initialize("localhost", 9999)) {
            System.out.println("✓ Socket connected successfully!");
        } else {
            System.out.println("✗ Warning: Socket connection failed. Is TestServer running?");
        }

        // Add shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            AppSocketManager.shutdown();
        }));

        CountDownLatch latch = new CountDownLatch(1);



        latch.countDown();
        // Hiển thị splash screen
        SwingUtilities.invokeLater(() -> {
            new AdvancedSplashScreen(latch).setVisible(true);
        });
    }
}