package iuh.fit.se.group1;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.repository.AccountRepository;
import iuh.fit.se.group1.repository.EmployeeRepository;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.util.PasswordUtil;

import java.time.LocalDate;

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
        AccountRepository accountRepository = new AccountRepository();
        Account account = accountRepository.findByUsername("thinh7");
        account.setPassword(PasswordUtil.hashPassword("ThinhDepTrai@123"));
        accountRepository.update(account);



    }
}