package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;

public class RoleRepository implements Repository<Role, String> {
    private static final Logger log = LoggerFactory.getLogger(RoleRepository.class);

    private final Connection connection;

    public RoleRepository() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public Role save(Role entity) {
        String sql = "INSERT INTO Role(roleId, roleName, createdAt) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getRoleId());
            preparedStatement.setString(2, entity.getRoleName());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(3, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }
            return entity;
        } catch (SQLException e) {
            log.error("Error saving Amenity: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Role findById(String id) {
        String sql = "SELECT * FROM Role WHERE roleId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Role role = new Role();
                    role.setRoleId(resultSet.getString("roleId"));
                    role.setRoleName(resultSet.getString("roleName"));
                    role.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return role;
                }
            }
        } catch (SQLException e) {
            log.error("Error finding Role by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public java.util.List<Role> findAll() {
        String sql = "SELECT * FROM Role";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                java.util.List<Role> roles = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    Role role = new Role();
                    role.setRoleId(resultSet.getString("roleId"));
                    role.setRoleName(resultSet.getString("roleName"));
                    role.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    roles.add(role);
                }
                return roles;
            }
        } catch (SQLException e) {
            log.error("Error finding all Roles: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role update(Role entity) {
        return null;
    }
}
