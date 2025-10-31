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

import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

public class PromotionRepository implements Repository<Promotion, Long>{
    private static final Logger log = LoggerFactory.getLogger(AmenityRepository.class);

    private final Connection connection;

    public PromotionRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Promotion save(Promotion entity) {
        String sql= "Insert into Promotion(promotionName,description,discountPercent,discountPrice,startDate,endDate,createdAt) values (?,?,?,?,?,?,?);";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getPromotionName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setFloat(3, entity.getDiscountPercent());
            preparedStatement.setBigDecimal(4, entity.getDiscountPrice());
            preparedStatement.setDate(5, Date.valueOf(entity.getStartDate()));
            preparedStatement.setDate(6, Date.valueOf(entity.getEndDate()));
            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(7, Date.valueOf(entity.getCreatedAt()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setPromotionId(generatedKeys.getLong(1));
                }
            }
            return entity;
        } catch (Exception e) {
            log.error("Error saving Promotion: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Promotion findById(Long id) {
        String sql = "Select * from Promotion where promotionId=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try(ResultSet rs = preparedStatement.executeQuery()){
                if(rs.next()){
                    Promotion entity = new Promotion();
                    entity.setPromotionId(rs.getLong(1));
                    entity.setPromotionName(rs.getString(2));
                    entity.setDescription(rs.getString(3));
                    entity.setDiscountPercent(rs.getFloat(4));
                    entity.setDiscountPrice(rs.getBigDecimal(5));
                    entity.setStartDate(rs.getDate(6).toLocalDate());
                    entity.setEndDate(rs.getDate(7).toLocalDate());
                    entity.setCreatedAt(rs.getDate(8).toLocalDate());
                    return entity;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Error finding Promotion by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "Delete from Promotion where promotionId=?;";
        try(PreparedStatement preparedStatement= connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("Error deleting Promotion by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "Select * from Promotion";
        try(PreparedStatement preparedStatement= connection.prepareStatement(sql);ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Promotion entity = new Promotion();
                entity.setPromotionId(rs.getLong("promotionId"));
                entity.setPromotionName(rs.getString("promotionName"));
                entity.setDescription(rs.getString("description"));
                entity.setDiscountPercent(rs.getFloat("discountPercent"));
                entity.setDiscountPrice(rs.getBigDecimal("discountPrice"));
                entity.setStartDate(rs.getDate("startDate").toLocalDate());
                entity.setEndDate(rs.getDate("endDate").toLocalDate());
                entity.setCreatedAt(rs.getDate("createdAt").toLocalDate());
    
                promotions.add(entity);
            }
        } catch (Exception e) {
            log.error("Error finding all promotions: ", e);
            throw new RuntimeException(e);
        }
        return promotions;
    }

    @Override
    public Promotion update(Promotion entity) {
        String sql = "Update Promotion set promotionName=?,description=?,discountPercent=?,discountPrice=?,startDate=?,endDate=? where promotionId=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getPromotionName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setFloat(3, entity.getDiscountPercent());
            preparedStatement.setBigDecimal(4, entity.getDiscountPrice());
            preparedStatement.setDate(5, Date.valueOf(entity.getStartDate()));
            preparedStatement.setDate(6, Date.valueOf(entity.getEndDate()));
            preparedStatement.setLong(7, entity.getPromotionId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setPromotionId(generatedKeys.getLong(1));
                }
            }
            return entity;
        } catch (Exception e) {
            log.error("Error updating Promotion: ", e);
            throw new RuntimeException(e);
        }
    }
    public List<Promotion> findByPromotionIdOrName(String keyword) {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM Promotion WHERE CAST(promotionId AS NVARCHAR) LIKE ? OR promotionName COLLATE SQL_Latin1_General_CP1_CI_AS LIKE ? ORDER BY promotionId ASC, promotionName ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Promotion promotion = new Promotion();
                    promotion.setPromotionId(resultSet.getLong("promotionId"));
                    promotion.setPromotionName(resultSet.getString("promotionName"));
                    promotion.setDescription(resultSet.getString("description"));
                    promotion.setDiscountPercent(resultSet.getFloat("discountPercent"));
                    promotion.setDiscountPrice(resultSet.getBigDecimal("discountPrice"));
                    promotion.setStartDate(resultSet.getDate("startDate").toLocalDate());
                    promotion.setEndDate(resultSet.getDate("endDate").toLocalDate());
                    promotion.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());
                    promotions.add(promotion);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return promotions;
    }

//    public Promotion findAllWithDiscountMax() {
//
//    }
}
