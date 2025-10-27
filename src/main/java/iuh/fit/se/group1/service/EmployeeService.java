package iuh.fit.se.group1.service;

import java.util.List;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.repository.EmployeeRepository;

public class EmployeeService {
     private final EmployeeRepository employeeRepository;

     public EmployeeService() {
        this.employeeRepository = new EmployeeRepository();
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public Employee updateEmployee(Employee employee) {
        return employeeRepository.update(employee);
    }

    public List<Employee> getEmployeeByKeyword(String keyword) {
        return employeeRepository.findByEmployeeNameOrId(keyword);
    }

}
