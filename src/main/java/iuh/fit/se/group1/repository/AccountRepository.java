package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;

public class AccountRepository implements Repository<Account, Long> {
    private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);

    private final Connection connection;

    public AccountRepository() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public Account save(Account entity) {
        String sql = "INSERT INTO Account(username, password, roleId, createdAt) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.setString(1,entity.getUsername());
            preparedStatement.setString(2,entity.getPassword());
            preparedStatement.setString(3,entity.getRole().getRoleId());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setAccountId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Amenity: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findById(Long id) {
        String sql = "SELECT * FROM Account WHERE accountId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setAccountId(resultSet.getLong("accountId"));
                    account.setUsername(resultSet.getString("username"));
                    account.setPassword(resultSet.getString("password"));
                    account.setRole(new Role(resultSet.getString("roleId")));
                    account.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return account;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public java.util.List<Account> findAll() {
        // Implementation here
        return null;
    }

    @Override
    public Account update(Account entity) {
        String sql = "UPDATE Account SET username = ?, roleId = ?, password = ? WHERE accountId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getRole().getRoleId());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setLong(4, entity.getAccountId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account findByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setAccountId(resultSet.getLong("accountId"));
                    account.setUsername(resultSet.getString("username"));
                    account.setPassword(resultSet.getString("password"));
                    account.setRole(new Role(resultSet.getString("roleId")));
                    account.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return account;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
