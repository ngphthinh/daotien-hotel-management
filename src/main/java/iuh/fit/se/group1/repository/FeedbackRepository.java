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

import iuh.fit.se.group1.entity.Feedback;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

public class FeedbackRepository implements Repository<Feedback,Long>{
    private static final Logger log = LoggerFactory.getLogger(AmenityRepository.class);

    private final Connection connection;

    public FeedbackRepository() {
        connection = DatabaseUtil.getConnection();
    }

    @Override
    public Feedback save(Feedback entity) {
        String sql = "Insert into Feedback (content, customerId, rating, createdAt) values (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getContent());
            preparedStatement.setLong(2, entity.getCustomer().getCustomerId());
            preparedStatement.setInt(3, entity.getRating());

            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setDate(4, Date.valueOf(entity.getCreatedAt()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setFeedbackId(generatedKeys.getLong(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving Feedback: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Feedback findById(Long id) {
        String sql = "select * from Feedback where feedbackId=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Feedback entity = new Feedback();
                    entity.setFeedbackId(rs.getLong(1));
                    entity.setContent(rs.getString(2));
                    entity.getCustomer().setCustomerId(rs.getLong(3));
                    entity.setRating(rs.getInt(4));
                    entity.setCreatedAt(rs.getDate(5).toLocalDate());
                    return entity;
                }
            }
        } catch (Exception e) {
            log.error("Error finding Feedback by ID: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from Feedback where feedbackId=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("Error deleting Feedback by ID: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Feedback> findAll() {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "select * from Feedback";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Feedback entity = new Feedback();
                entity.setFeedbackId(rs.getLong(1));              
                entity.setContent(rs.getString(2));               
                entity.getCustomer().setCustomerId(rs.getLong(3));              
                entity.setRating(rs.getInt(4));                   
                entity.setCreatedAt(rs.getDate(5).toLocalDate());
                feedbackList.add(entity);
            }

        } catch (SQLException e) {
            log.error("Error finding all Feedback: ", e);
            throw new RuntimeException(e);
        }

        return feedbackList;
    }

    @Override
    public Feedback update(Feedback entity) {
        String sql = "update Feedback set content=?,customerId=?,rating=? where feedbackId=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getContent());               
            preparedStatement.setLong(2, entity.getCustomer().getCustomerId());             
            preparedStatement.setInt(3, entity.getRating());                   
            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDate.now());
            }
            preparedStatement.setLong(4, entity.getFeedbackId());             
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            return entity;

        } catch (SQLException e) {
            log.error("Error updating Feedback: ", e);
            throw new RuntimeException(e);
        }
    }
    
}
