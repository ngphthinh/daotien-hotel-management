package iuh.fit.se.group1.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

public class EmployeeRepository implements Repository<Employee, Long> {
    private static final Logger log = LoggerFactory.getLogger(EmployeeRepository.class);

    private final Connection connection;

    public EmployeeRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Employee save(Employee entity) {
        String sql = "INSERT INTO Employee (fullName, phone,email,hireDate,citizenId,gender,accountId,createdAt) VALUES (?, ?, ?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setDate(4, Date.valueOf(entity.getHireDate()));
            preparedStatement.setString(5, entity.getCitizenId());
            preparedStatement.setBoolean(6, entity.isGender());
            preparedStatement.setLong(7, entity.getAccount().getAccountId());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(8, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setEmployeeId(generatedKeys.getLong(1));
                    ;
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Employee: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Employee findById(Long id) {
        String sql = "SELECT * FROM Employee WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(resultSet.getLong("employeeId"));
                    employee.setFullName(resultSet.getString("fullName"));
                    employee.setPhone(resultSet.getString("phone"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setHireDate(resultSet.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(resultSet.getString("citizenId"));
                    employee.setGender(resultSet.getBoolean("gender"));
                    employee.getAccount().setAccountId(resultSet.getLong("accountId"));
                    employee.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return employee;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM Employee WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();

                    employee.setEmployeeId(resultSet.getLong("employeeId"));
                    employee.setFullName(resultSet.getString("fullName"));
                    employee.setPhone(resultSet.getString("phone"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setHireDate(resultSet.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(resultSet.getString("citizenId"));
                    employee.setGender(resultSet.getBoolean("gender"));
                    employee.getAccount().setAccountId(resultSet.getLong("accountId"));
                    employee.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee update(Employee entity) {
        String sql = "UPDATE Employee SET fullName = ?, phone = ?, email = ?, hireDate = ?, citizenId = ?, gender = ?, accountId = ? WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setDate(4, Date.valueOf(entity.getHireDate()));
            preparedStatement.setString(5, entity.getCitizenId());
            preparedStatement.setBoolean(6, entity.isGender());
            preparedStatement.setLong(7, entity.getAccount().getAccountId());
            preparedStatement.setLong(8, entity.getEmployeeId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Employee> findByEmployeeNameOrId(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE fullName COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? OR employeeId LIKE ? Order BY employeeId ASC, fullName ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(resultSet.getLong("employeeId"));
                    employee.setFullName(resultSet.getString("fullName"));
                    employee.setPhone(resultSet.getString("phone"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setHireDate(resultSet.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(resultSet.getString("citizenId"));
                    employee.setGender(resultSet.getBoolean("gender"));
                    employee.getAccount().setAccountId(resultSet.getLong("accountId"));
                    employee.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

}
