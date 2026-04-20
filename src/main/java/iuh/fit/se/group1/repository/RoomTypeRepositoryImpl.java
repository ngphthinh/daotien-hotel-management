package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.RoomType;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.repository.interfaces.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class RoomTypeRepositoryImpl implements Repository<RoomType, String>, iuh.fit.se.group1.repository.interfaces.RoomTypeRepository {

    private static final Logger log = LoggerFactory.getLogger(RoomTypeRepositoryImpl.class);
    private final Connection connection;

    public RoomTypeRepositoryImpl() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public RoomType save(RoomType entity) {
        String sql = """
                INSERT INTO RoomType (
                    roomTypeId, name, createdAt,
                    hourlyRate, dailyRate, overnightRate, additionalHourRate
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getRoomTypeId());
            ps.setString(2, entity.getName());

            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            ps.setDate(3, Date.valueOf(entity.getCreatedAt()));

            ps.setBigDecimal(4, entity.getHourlyRate());
            ps.setBigDecimal(5, entity.getDailyRate());
            ps.setBigDecimal(6, entity.getOvernightRate());
            ps.setBigDecimal(7, entity.getAdditionalHourRate());

            int rows = ps.executeUpdate();
            log.info("INSERT RoomType rows = {}", rows);

            return entity;

        } catch (SQLException e) {
            log.error("Error saving RoomType", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoomType update(RoomType entity) {

        try {
            if (connection.isClosed()) {
                JOptionPane.showMessageDialog(null, "Database connection is closed.");
                return null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        String sql = """
                    UPDATE RoomType
                    SET hourlyRate = ?, dailyRate = ?, overnightRate = ?, additionalHourRate =?
                    WHERE roomTypeId = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setBigDecimal(1, entity.getHourlyRate());
            ps.setBigDecimal(2, entity.getDailyRate());
            ps.setBigDecimal(3, entity.getOvernightRate());
            ps.setBigDecimal(4, entity.getAdditionalHourRate());
            ps.setString(5, entity.getRoomTypeId());
            int rows = ps.executeUpdate();
            log.info("UPDATE RoomType rows = {}", rows);

            return entity;

        } catch (SQLException e) {
            log.error("Error updating RoomType", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoomType findById(String id) {
        String sql = "SELECT * FROM RoomType WHERE roomTypeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RoomType roomType = new RoomType();
                    roomType.setRoomTypeId(resultSet.getString("roomTypeId"));
                    roomType.setName(resultSet.getString("name"));
                    roomType.setHourlyRate(resultSet.getBigDecimal("hourlyRate"));
                    roomType.setDailyRate(resultSet.getBigDecimal("dailyRate"));
                    roomType.setOvernightRate(resultSet.getBigDecimal("overnightRate"));
                    roomType.setAdditionalHourRate(resultSet.getBigDecimal("additionalHourRate"));
                    // Fix: Null-safe cho createdAt
                    Date createdAtDate = resultSet.getDate("createdAt");
                    roomType.setCreatedAt(createdAtDate != null ? createdAtDate.toLocalDate() : null);
                    log.debug("Loaded RoomType ID: {}", id); // Debug log
                    return roomType;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding RoomType by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM RoomType WHERE roomTypeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                log.info("Deleted RoomType with ID: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting RoomType: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoomType> findAll() {
        List<RoomType> roomTypes = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY roomTypeId";

        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setRoomTypeId(rs.getString("roomTypeId"));
                rt.setName(rs.getString("name"));

                Date createdAt = rs.getDate("createdAt");
                rt.setCreatedAt(createdAt != null ? createdAt.toLocalDate() : null);

                rt.setHourlyRate(rs.getBigDecimal("hourlyRate"));
                rt.setDailyRate(rs.getBigDecimal("dailyRate"));
                rt.setOvernightRate(rs.getBigDecimal("overnightRate"));
                rt.setAdditionalHourRate(rs.getBigDecimal("additionalHourRate"));

                roomTypes.add(rt);
            }

            log.info("Found {} RoomTypes", roomTypes.size());

        } catch (SQLException e) {
            log.error("Error finding RoomTypes", e);
            throw new RuntimeException(e);
        }

        return roomTypes;
    }

}