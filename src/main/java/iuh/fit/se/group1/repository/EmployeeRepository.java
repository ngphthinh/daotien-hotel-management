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

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Role;
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
        String sql = "INSERT INTO Employee (fullName, phone,email,hireDate,citizenId,gender,accountId,avt,createdAt) VALUES (?, ?, ?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setDate(4, Date.valueOf(entity.getHireDate()));
            preparedStatement.setString(5, entity.getCitizenId());
            preparedStatement.setBoolean(6, entity.isGender());
            if (entity.getAccount() != null && entity.getAccount().getAccountId() != null) {
                preparedStatement.setLong(7, entity.getAccount().getAccountId());
            } else {
                preparedStatement.setNull(7, java.sql.Types.BIGINT);
            }
            entity.setCreatedAt(LocalDate.now());
            if (entity.getAvt() != null && entity.getAvt().length > 0) {
                preparedStatement.setBytes(8, entity.getAvt());
            } else {
                preparedStatement.setNull(8, java.sql.Types.VARBINARY);
            }
            preparedStatement.setDate(9, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setEmployeeId(generatedKeys.getLong(1));
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
                    Long accountId = resultSet.getLong("accountId");
                    if (accountId != null && accountId > 0) {
                        Account account = new Account();
                        account.setAccountId(accountId);
                        employee.setAccount(account);
                    }
                    byte[] avtBytes = resultSet.getBytes("avt");
                    employee.setAvt(avtBytes);
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
        String sql = """
                SELECT e.employeeId, e.fullName, e.phone, e.email, e.hireDate, e.citizenId, e.gender,
                       e.accountId, a.username, a.password,
                       r.roleId, r.roleName,e.avt
                FROM Employee e
                JOIN Account a ON e.accountId = a.accountId
                JOIN Role r ON a.roleId = r.roleId
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(rs.getLong("employeeId"));
                    employee.setFullName(rs.getString("fullName"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(rs.getString("citizenId"));
                    employee.setGender(rs.getBoolean("gender"));
                    byte[] avtBytes = rs.getBytes("avt");
                    employee.setAvt(avtBytes);

                    // Account
                    Account account = new Account();
                    account.setAccountId(rs.getLong("accountId"));
                    account.setUsername(rs.getString("username"));
                    account.setPassword(rs.getString("password"));

                    // Role
                    Role role = new Role();
                    role.setRoleId(rs.getString("roleId"));
                    role.setRoleName(rs.getString("roleName"));
                    account.setRole(role);

                    // Liên kết
                    employee.setAccount(account);

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
        String sql = "UPDATE Employee SET fullName = ?, phone = ?, email = ?, hireDate = ?, citizenId = ?, gender = ?, accountId = ?,avt=? WHERE employeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPhone());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setDate(4, Date.valueOf(entity.getHireDate()));
            preparedStatement.setString(5, entity.getCitizenId());
            preparedStatement.setBoolean(6, entity.isGender());
            preparedStatement.setLong(7, entity.getAccount().getAccountId());
            if (entity.getAvt() != null && entity.getAvt().length > 0) {
                preparedStatement.setBytes(8, entity.getAvt());
            } else {
                preparedStatement.setNull(8, java.sql.Types.VARBINARY);
            }
            preparedStatement.setLong(9, entity.getEmployeeId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee findByCitizenId(String citizenId) {
        String sql = """
                SELECT e.employeeId, e.fullName, e.phone, e.email, e.hireDate, e.citizenId, e.gender,
                       e.accountId, a.username, a.password,
                       r.roleId, r.roleName,e.avt
                FROM Employee e
                JOIN Account a ON e.accountId = a.accountId
                JOIN Role r ON a.roleId = r.roleId
                WHERE e.citizenId = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, citizenId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Employee
                    Employee employee = new Employee();
                    employee.setEmployeeId(rs.getLong("employeeId"));
                    employee.setFullName(rs.getString("fullName"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(rs.getString("citizenId"));
                    employee.setGender(rs.getBoolean("gender"));
                    employee.setAvt(rs.getBytes("avt"));
                    // Account
                    Account account = new Account();
                    account.setAccountId(rs.getLong("accountId"));
                    account.setUsername(rs.getString("username"));
                    account.setPassword(rs.getString("password"));

                    // Role
                    Role role = new Role();
                    role.setRoleId(rs.getString("roleId"));
                    role.setRoleName(rs.getString("roleName"));
                    account.setRole(role);

                    // Liên kết
                    employee.setAccount(account);

                    return employee;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi truy vấn Employee theo citizenId", e);
        }
        return null;
    }

    public List<Employee> findByIdOrNameOrPhoneNumber(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String sql = """
                SELECT e.employeeId, e.fullName, e.phone, e.email, e.hireDate, e.citizenId, e.gender,
                       e.accountId, a.username, a.password,
                       r.roleId, r.roleName
                FROM Employee e
                JOIN Account a ON e.accountId = a.accountId
                JOIN Role r ON a.roleId = r.roleId
                WHERE employeeId LIKE ? OR fullName COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? OR phone LIKE ?
                Order BY employeeId , fullName
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            preparedStatement.setString(3, likeKeyword);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(rs.getLong("employeeId"));
                    employee.setFullName(rs.getString("fullName"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(rs.getString("citizenId"));
                    employee.setGender(rs.getBoolean("gender"));

                    // Account
                    Account account = new Account();
                    account.setAccountId(rs.getLong("accountId"));
                    account.setUsername(rs.getString("username"));
                    account.setPassword(rs.getString("password"));

                    // Role
                    Role role = new Role();
                    role.setRoleId(rs.getString("roleId"));
                    role.setRoleName(rs.getString("roleName"));
                    account.setRole(role);

                    // Liên kết
                    employee.setAccount(account);

                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }
}