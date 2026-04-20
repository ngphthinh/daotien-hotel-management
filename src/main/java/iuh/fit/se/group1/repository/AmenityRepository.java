package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;
import iuh.fit.se.group1.repository.interfaces.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AmenityRepository implements Repository<Amenity, Long> {

    private static final Logger log = LoggerFactory.getLogger(AmenityRepository.class);

    private final Connection connection;

    public AmenityRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Amenity save(Amenity entity) {
        String sql = "INSERT INTO Amenity (nameAmenity, price, createdAt) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getNameAmenity());
            preparedStatement.setBigDecimal(2, entity.getPrice());
            entity.setCreatedAt(LocalDate.now());
            preparedStatement.setDate(3, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setAmenityId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Amenity: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Amenity update(Amenity entity) {
        String sql = "UPDATE Amenity SET nameAmenity = ?, price = ? WHERE amenityId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getNameAmenity());
            preparedStatement.setBigDecimal(2, entity.getPrice());
            preparedStatement.setLong(3, entity.getAmenityId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Amenity findById(Long id) {
        String sql = "SELECT * FROM Amenity WHERE amenityId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(resultSet.getLong("amenityId"));
                    amenity.setNameAmenity(resultSet.getString("nameAmenity"));
                    amenity.setPrice(resultSet.getBigDecimal("price"));
                    amenity.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return amenity;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM Amenity WHERE amenityId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Amenity> findAll() {
        List<Amenity> amenities = new ArrayList<>();
        String sql = "SELECT * FROM Amenity";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(resultSet.getLong("amenityId"));
                    amenity.setNameAmenity(resultSet.getString("nameAmenity"));
                    amenity.setPrice(resultSet.getBigDecimal("price"));
                    amenity.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    amenities.add(amenity);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return amenities;
    }

    public List<Amenity> findByAmenityNameOrId(String keyword) {
        List<Amenity> amenities = new ArrayList<>();
        String sql = "SELECT * FROM Amenity WHERE nameAmenity COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? OR amenityId LIKE ? Order BY amenityId ASC, nameAmenity ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(resultSet.getLong("amenityId"));
                    amenity.setNameAmenity(resultSet.getString("nameAmenity"));
                    amenity.setPrice(resultSet.getBigDecimal("price"));
                    amenity.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    amenities.add(amenity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return amenities;
    }

    public Amenity findByAmenityName(String nameAmenity) {
        String sql = "SELECT * FROM Amenity WHERE nameAmenity = ? and  isDeleted = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nameAmenity);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(resultSet.getLong("amenityId"));
                    amenity.setNameAmenity(resultSet.getString("nameAmenity"));
                    amenity.setPrice(resultSet.getBigDecimal("price"));
                    amenity.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    return amenity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
