package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SurchargeRepository implements Repository<Surcharge, Long> {

    private static final Logger log = LoggerFactory.getLogger(SurchargeRepository.class);
    private final Connection connection;

    public SurchargeRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Surcharge save(Surcharge entity) {
        String sql = "INSERT INTO Surcharge (name, price, orderId, createdAt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setBigDecimal(2, entity.getPrice());
            preparedStatement.setLong(3, entity.getOrderId());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setSurchargeId(generatedKeys.getLong(1));
                }
            }

            log.info("Inserted surcharge successfully: {}", entity);
            return entity;

        } catch (SQLException e) {
            log.error("Error saving Surcharge: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Surcharge update(Surcharge entity) {
        String sql = "UPDATE Surcharge SET name = ?, price = ?, orderId = ? WHERE surchargeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setBigDecimal(2, entity.getPrice());
            preparedStatement.setLong(3, entity.getOrderId());
            preparedStatement.setLong(4, entity.getSurchargeId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected (maybe ID not found).");
            }

            log.info("Updated surcharge successfully: {}", entity);
            return entity;

        } catch (SQLException e) {
            log.error("Error updating Surcharge: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Surcharge findById(Long id) {
        String sql = "SELECT * FROM Surcharge WHERE surchargeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Surcharge surcharge = new Surcharge();
                    surcharge.setSurchargeId(resultSet.getLong("surchargeId"));
                    surcharge.setName(resultSet.getString("name"));
                    surcharge.setPrice(resultSet.getBigDecimal("price"));
                    surcharge.setOrderId(resultSet.getLong("orderId"));
                    surcharge.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return surcharge;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding Surcharge by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Surcharge WHERE surchargeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted == 0) {
                log.warn("No surcharge found to delete with ID: {}", id);
            } else {
                log.info("Deleted surcharge with ID: {}", id);
            }

        } catch (SQLException e) {
            log.error("Error deleting Surcharge: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Surcharge> findAll() {
        List<Surcharge> surcharges = new ArrayList<>();
        String sql = "SELECT * FROM Surcharge";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Surcharge surcharge = new Surcharge();
                surcharge.setSurchargeId(resultSet.getLong("surchargeId"));
                surcharge.setName(resultSet.getString("name"));
                surcharge.setPrice(resultSet.getBigDecimal("price"));
                surcharge.setOrderId(resultSet.getLong("orderId"));
                surcharge.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                surcharges.add(surcharge);
            }

            log.info("Loaded {} surcharges from database", surcharges.size());
            return surcharges;

        } catch (SQLException e) {
            log.error("Error finding all Surcharges: ", e);
            throw new RuntimeException(e);
        }
    }
    public List<Surcharge> findBySurchargeNameOrId(String keyword) {
    List<Surcharge> surcharges = new ArrayList<>();
    String sql = """
        SELECT * FROM Surcharge
        WHERE name COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ?
           OR CAST(surchargeId AS NVARCHAR) LIKE ?
        ORDER BY surchargeId ASC, name ASC
        """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        String likeKeyword = "%" + keyword + "%";
        preparedStatement.setString(1, likeKeyword);
        preparedStatement.setString(2, likeKeyword);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Surcharge surcharge = new Surcharge();
                surcharge.setSurchargeId(resultSet.getLong("surchargeId"));
                surcharge.setName(resultSet.getString("name"));
                surcharge.setPrice(resultSet.getBigDecimal("price"));
                surcharge.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                surcharges.add(surcharge);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error finding Surcharge by name or ID", e);
    }

    return surcharges;
}

}
