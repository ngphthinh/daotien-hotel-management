package iuh.fit.se.group1;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.repository.EmployeeRepository;
import iuh.fit.se.group1.repository.OrderRepository;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.util.PasswordUtil;

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
    public static void main(String[] args) {
        OrderRepository orderRepository = new OrderRepository();

        Customer customer = new Customer();
        customer.setEmail("ngphthinh@gmail.com");
        customer.setFullName("Nguyễn Phước Thịnh");
        customer.setPhone("0338687106");
        customer.setDateOfBirth(LocalDate.now());
        customer.setGender(false);
        customer.setCitizenId("082205000810");
        customer.setAddress("Hihis");


        Order order = new Order();

        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setEmployee(new Employee(4L));
        order.setOrderType(new OrderType(1L, "sad", LocalDate.now()));
        order.setDeposit(BigDecimal.ZERO);


        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Employee(4L),new Room(1L), BookingType.HOURLY,BigDecimal.valueOf(2000l),LocalDate.now());

        order.addBooking(booking);
        System.out.println(orderRepository.save(order));



    }
}