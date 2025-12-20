package iuh.fit.se.group1.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
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
                String base64String = Base64.getEncoder().encodeToString(entity.getAvt());
                preparedStatement.setString(8, base64String);
            } else {
                preparedStatement.setNull(8, java.sql.Types.NVARCHAR);
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
                    String base64String = resultSet.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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
        String sqlGetAccountId = "SELECT accountId FROM Employee WHERE employeeId = ?";
        String sqlDeleteEmployee = "DELETE FROM Employee WHERE employeeId = ?";
        String sqlDeleteAccount = "DELETE FROM Account WHERE accountId = ?";

        try {
            connection.setAutoCommit(false);
            Long accountId = null;
            try (PreparedStatement psGetAccount = connection.prepareStatement(sqlGetAccountId)) {
                psGetAccount.setLong(1, aLong);
                try (ResultSet rs = psGetAccount.executeQuery()) {
                    if (rs.next()) {
                        accountId = rs.getLong("accountId");
                    }
                }
            }
            try (PreparedStatement psDeleteEmployee = connection.prepareStatement(sqlDeleteEmployee)) {
                psDeleteEmployee.setLong(1, aLong);
                psDeleteEmployee.executeUpdate();
            }
            if (accountId != null && accountId > 0) {
                try (PreparedStatement psDeleteAccount = connection.prepareStatement(sqlDeleteAccount)) {
                    psDeleteAccount.setLong(1, accountId);
                    psDeleteAccount.executeUpdate();
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                log.error("Error rolling back transaction: ", ex);
            }
            log.error("Error deleting Employee and Account: ", e);
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
                    String base64String = rs.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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
        String updateEmployeeSql = """
                    UPDATE Employee
                    SET fullName = ?, phone = ?, email = ?, hireDate = ?, citizenId = ?,
                        gender = ?, accountId = ?, avt = ?
                    WHERE employeeId = ?
                """;

        String updateRoleSql = """
                    UPDATE Account
                    SET roleId = ?
                    WHERE accountId = ?
                """;

        try {
            connection.setAutoCommit(false); // bắt đầu transaction

            try (PreparedStatement psEmp = connection.prepareStatement(updateEmployeeSql);
                 PreparedStatement psRole = connection.prepareStatement(updateRoleSql)) {

                // ===== Update Employee =====
                psEmp.setString(1, entity.getFullName());
                psEmp.setString(2, entity.getPhone());
                psEmp.setString(3, entity.getEmail());
                psEmp.setDate(4, Date.valueOf(entity.getHireDate()));
                psEmp.setString(5, entity.getCitizenId());
                psEmp.setBoolean(6, entity.isGender());
                psEmp.setLong(7, entity.getAccount().getAccountId());

                if (entity.getAvt() != null && entity.getAvt().length > 0) {
                    String base64String = Base64.getEncoder().encodeToString(entity.getAvt());
                    psEmp.setString(8, base64String);
                } else {
                    psEmp.setNull(8, java.sql.Types.NVARCHAR);
                }

                psEmp.setLong(9, entity.getEmployeeId());
                psEmp.executeUpdate();

                // ===== Update Role =====
                if (entity.getAccount() != null && entity.getAccount().getRole() != null) {
                    psRole.setString(1, entity.getAccount().getRole().getRoleId());
                    psRole.setLong(2, entity.getAccount().getAccountId());
                    psRole.executeUpdate();
                }

                connection.commit(); // ✅ thành công
                return entity;
            } catch (SQLException e) {
                connection.rollback(); // ❌ lỗi thì rollback
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating employee and role", e);
        }
    }


    public List<Employee> findAllByRoleId(String roleId) {
        List<Employee> employees = new ArrayList<>();
        String sql = """
                SELECT e.employeeId, e.fullName, e.phone, e.email, e.hireDate, e.citizenId, e.gender,
                       e.accountId, a.username, a.password,
                       r.roleId, r.roleName,e.avt
                FROM Employee e
                JOIN Account a ON e.accountId = a.accountId
                JOIN Role r ON a.roleId = r.roleId
                     where r.roleId=?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roleId);
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
                    String base64String = rs.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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
                    String base64String = rs.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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
                    String base64String = rs.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM Employee";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            log.error("Error counting Employees: ", e);
            throw new RuntimeException(e);
        }
        return 0;
    }

    public Employee findByAccountId(Long accountId) {

        String sql = """
                SELECT * 
                FROM Employee E join Account A on E.accountId=A.accountId
                JOIN Role R ON A.roleId = R.roleId
                WHERE E.accountId = ? 
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, accountId);


            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {

                    Employee employee = new Employee();
                    employee.setEmployeeId(rs.getLong("employeeId"));
                    employee.setFullName(rs.getString("fullName"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setHireDate(rs.getDate("hireDate").toLocalDate());
                    employee.setCitizenId(rs.getString("citizenId"));
                    employee.setGender(rs.getBoolean("gender"));
                    String base64String = rs.getString("avt");
                    if (base64String != null && !base64String.isEmpty()) {
                        byte[] originalBytes = Base64.getDecoder().decode(base64String);
                        employee.setAvt(originalBytes);
                    }
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}