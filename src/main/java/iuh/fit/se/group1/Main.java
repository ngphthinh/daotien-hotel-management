package iuh.fit.se.group1;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.repository.EmployeeRepository;
import iuh.fit.se.group1.repository.OrderRepository;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or


// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


//    public static void main(String args[]) {
//
////        RoleService roleService = new RoleService();
//
//        Employee employee = new Employee();
//        employee.setFullName("Nguyễn Phước Thịnh");
//        employee.setPhone("0338687106");
//        employee.setEmail("nguyenphuocthinh020@gmail.com");
//        employee.setGender(false);
//        employee.setCitizenId("082205000810");
//        employee.setHireDate(LocalDate.now());
//
//
//        EmployeeService employeeService = new EmployeeService();
//        Employee employeeSave = employeeService.createEmployee(employee, Role.RECEPTIONIST.name());
//        System.out.println(employeeSave);
//

    /// /        AuthenticateService authenticateService = new AuthenticateService();
    /// /        Account account = authenticateService.authenticate("thinh4","User@123;");
    /// /        if (account != null) {
    /// /            System.out.println("Đăng nhập thành công: " + account.getUsername() + " - Role: " + account.getRole().getRoleId());
    /// /        } else {
    /// /            System.out.println("Đăng nhập thất bại");
    /// /        }
//
//    }
    public static void main(String[] args) throws Exception {
        OrderRepository orderRepository = new OrderRepository();

        Customer customer = new Customer();
        customer.setEmail("ngphthinh@gmail.com");
        customer.setFullName("Nguyễn Phước Thịnh");
        customer.setPhone("0338687106");
        customer.setDateOfBirth(LocalDate.now());
        customer.setGender(false);
        customer.setCitizenId("082205000810");


        Order order = new Order();

        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setEmployee(new Employee(4L));
        order.setOrderType(new OrderType(1L, "sad", LocalDate.now()));
        order.setDeposit(BigDecimal.ZERO);
        order.setTotalAmount(new BigDecimal(2000));

        PaymentService paymentService = new PaymentService();


        JFrame frame = new JFrame("Thanh toán MoMo QR");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel lblImage = new JLabel("", SwingConstants.CENTER);
        JButton btnCheck = new JButton("Kiểm tra trạng thái");

        frame.add(lblImage, BorderLayout.CENTER);
        frame.add(btnCheck, BorderLayout.SOUTH);
        frame.setVisible(true);

        String response = paymentService.createPayment(order);

        // Parse JSON thủ công thay vì dùng Gson
        String payUrl = paymentService.extractJsonValue(response, "payUrl");
        String orderId = paymentService.extractJsonValue(response, "orderId");
        String resultCode = paymentService.extractJsonValue(response, "resultCode");


        System.out.println("Order ID: " + orderId);
        System.out.println("Result Code: " + resultCode);

        if (payUrl != null && !payUrl.isEmpty()) {
            lblImage.setIcon(new ImageIcon(paymentService.generateQRCodeImage(payUrl, 300, 300)));
            JOptionPane.showMessageDialog(frame, "QR Code đã tạo thành công!\nOrder ID: " + orderId);
        } else {
            JOptionPane.showMessageDialog(frame, "Không lấy được payUrl\nResponse: " + response);
        }


//
//
//        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Employee(4L),new Room(1L), BookingType.HOURLY,BigDecimal.valueOf(2000l),LocalDate.now());
//
//        order.addBooking(booking);
//        System.out.println(orderRepository.save(order));


    }
}