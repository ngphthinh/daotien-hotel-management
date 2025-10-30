package iuh.fit.se.group1.repository;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.infrastructure.DatabaseUtil;

import java.sql.Connection;
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
}
