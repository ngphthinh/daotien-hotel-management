package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.entity.Employee;

public class EmployeeMapper {


    private final AccountMapper accountMapper;

    public EmployeeMapper() {
        this.accountMapper = new AccountMapper();
    }

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null)
            return null;
        return EmployeeDTO.builder().employeeId(employee.getEmployeeId())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .gender(employee.isGender())
                .citizenId(employee.getCitizenId())
                .hireDate(employee.getHireDate())
                .account(this.accountMapper.toDTO(employee.getAccount()))
                .avt(employee.getAvt()).build();
    }

    public Employee toEmployee(EmployeeDTO employee) {
        if (employee == null)
            return null;
        return Employee.builder()
                .employeeId(employee.getEmployeeId())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .gender(employee.isGender())
                .citizenId(employee.getCitizenId())
                .hireDate(employee.getHireDate())
                .account(this.accountMapper.toAccount(employee.getAccount()))
                .avt(employee.getAvt())
                .build();
    }
}
