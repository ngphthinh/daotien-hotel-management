package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailRepository {
    boolean save(EntityManager em, Order savedOrder, List<OrderDetail> orderDetails);

    // New: save by orderId (used when order already exists and we need to insert details)
    boolean saveByOrderId(EntityManager em, Long orderId, List<OrderDetail> orderDetails);

    // New: delete all details for an order
    void deleteByOrderId(EntityManager em, Long orderId);

    List<OrderDetail> findByOrderId(EntityManager em, Long orderId);

    void deleteById(EntityManager em, Long amenityId, Long orderId);

    OrderDetail save(EntityManager em, Long orderId, OrderDetail newDetail);

    void updateOrderDetailFormOrderId(EntityManager em, Long amenityId, BigDecimal unitPrice, int quantity, Long orderId);

    BigDecimal getServiceRevenue(EntityManager em, LocalDateTime start, LocalDateTime end);
}
