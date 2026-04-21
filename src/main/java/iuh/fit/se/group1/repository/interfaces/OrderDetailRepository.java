package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailRepository {
    boolean save(Order savedOrder, List<OrderDetail> orderDetails);

    // New: save by orderId (used when order already exists and we need to insert details)
    boolean saveByOrderId(Long orderId, List<OrderDetail> orderDetails);

    // New: delete all details for an order
    void deleteByOrderId(Long orderId);

    List<OrderDetail> findByOrderId(Long orderId);

    void deleteById(Long amenityId, Long orderId);

    OrderDetail save(Long orderId, OrderDetail newDetail);

    void updateOrderDetailFormOrderId(Long amenityId, BigDecimal unitPrice, int quantity, Long orderId);

}
