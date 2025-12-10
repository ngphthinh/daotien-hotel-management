package iuh.fit.se.group1;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
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

        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            try {
                System.out.println("Đang tải dữ liệu ứng dụng...");

                EmployeeService employeeService = new EmployeeService();
                if (employeeService.count() == 0) {
                    Employee admin = new Employee();
                    admin.setFullName("Quản Trị Viên Admin");
                    admin.setPhone("0123456789");
                    admin.setHireDate(LocalDate.now());
                    admin.setEmail("nguyenphuocthinh0710@gmail.com");
                    admin.setGender(false);
                    admin.setCitizenId("082205000819");
                    employeeService.createEmployee(admin, Role.MANAGER.toString());
                }

                System.out.println("Hoàn tất tải dữ liệu!");
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