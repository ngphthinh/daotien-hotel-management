package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

import java.sql.*;
import java.util.List;

public class SurchargeDetailRepository {

    private final Connection connection;

    public SurchargeDetailRepository() {
        connection = DatabaseUtil.getConnection();
    }


    public SurchargeDetail save(SurchargeDetail surchargeDetail, Long orderId) {
        String sql = """
                INSERT INTO SurchargeDetail (orderId, surchargerId, quantity)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, surchargeDetail.getSurcharge().getSurchargeId());
            ps.setInt(3, surchargeDetail.getQuantity());

            ps.executeUpdate();

            return surchargeDetail;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving SurchargeDetail", e);
        }
    }

    public List<SurchargeDetail> findSurchargeDetailsByOrderId(Long orderId) {
        String sql = """
                SELECT sd.surchargerId, sd.quantity, sd.createdAt, s.name, s.price
                FROM SurchargeDetail sd
                JOIN Surcharge s ON sd.surchargerId = s.surchargeId
                WHERE sd.orderId = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();

            List<SurchargeDetail> surchargeDetails = new java.util.ArrayList<>();
            while (rs.next()) {
                Surcharge surcharge = new Surcharge();
                surcharge.setSurchargeId(rs.getLong("surchargerId"));
                surcharge.setName(rs.getString("name"));
                surcharge.setPrice(rs.getBigDecimal("price"));

                SurchargeDetail surchargeDetail = new SurchargeDetail();
                surchargeDetail.setSurcharge(surcharge);
                surchargeDetail.setQuantity(rs.getInt("quantity"));
                surchargeDetail.setCreatedAt(rs.getDate("createdAt").toLocalDate());

                surchargeDetails.add(surchargeDetail);
            }
            return surchargeDetails;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving SurchargeDetails by Order ID", e);
        }
    }

    public boolean existsBySurchargeIdAndOrderId(Long surchargeId, Long orderId) {
        String sql = "SELECT 1 FROM SurchargeDetail WHERE surchargerId = ? AND orderId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, surchargeId);
            ps.setLong(2, orderId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of SurchargeDetail", e);
        }
    }
}
