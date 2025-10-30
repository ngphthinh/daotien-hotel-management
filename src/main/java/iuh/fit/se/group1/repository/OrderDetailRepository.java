package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepository {

    private final Connection connection;

    public OrderDetailRepository() {
        this.connection = DatabaseUtil.getConnection();
    }


    public boolean save(Order savedOrder, List<OrderDetail> orderDetails) {
        String sql = "INSERT INTO OrderDetail(unitPrice, quantity, amenityId, orderId) values (?,?,?,?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            for (OrderDetail detail : orderDetails) {
                preparedStatement.setBigDecimal(1, detail.getUnitPrice());
                preparedStatement.setInt(2, detail.getQuantity());
                preparedStatement.setLong(3, detail.getAmenity().getAmenityId());
                preparedStatement.setLong(4, savedOrder.getOrderId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderDetail> findByOrderId(Long orderId) {
        String sql = """
                SELECT
                     OD.orderId, OD.amenityId, OD.quantity, OD.unitPrice,
                    A.amenityId AS aId, A.nameAmenity AS aName, A.price AS aPrice
                FROM OrderDetail OD
                JOIN Amenity A ON OD.amenityId = A.amenityId
                WHERE OD.orderId = ?
                """;

        List<OrderDetail> orderDetails = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Map Amenity
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(rs.getLong("aId"));
                    amenity.setNameAmenity(rs.getString("aName"));
                    amenity.setPrice(rs.getBigDecimal("aPrice"));

                    // Map OrderDetail
                    OrderDetail detail = new OrderDetail();
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getBigDecimal("unitPrice"));
                    detail.setAmenity(amenity);

                    orderDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

}
