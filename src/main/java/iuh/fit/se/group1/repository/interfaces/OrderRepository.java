package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Order;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderRepository {
    void updateTotalAmount(EntityManager em, Long orderId, BigDecimal totalAmount);

    void updateDeposit(EntityManager em, Long orderId, BigDecimal deposit);

    void updateOrderType(EntityManager em, Long orderId, Long newOrderTypeId);

    List<Order> findAllByOrderUnPaid(EntityManager em);

    void updateOrderStatusToPaid(EntityManager em, Order order);

    List<Order> findUnpaidOrdersByKeyword(EntityManager em, String keyword);

    List<Order> findAllOrders(EntityManager em);

    List<Order> findOrdersUnPendingByKeyWord(EntityManager em, String keyword);

    BigDecimal calculateTotalRevenueBetweenDates(EntityManager em, LocalDate from, LocalDate to);

    List<Order> searchOrdersByKeyword(EntityManager em, String searchText);

    List<Order> findOrdersByRoomIdAndOrderType(EntityManager em, Long roomId, Long orderTypeId);

    Map<String, BigDecimal> getRevenueByRoomType(EntityManager em, LocalDate from, LocalDate to);

    Map<String, Integer> getBookingCountByRoomTypeAndDate(EntityManager em, LocalDate date);


    boolean addSurchargeToOrder(EntityManager em, long orderId, long surchargeAmount);

    boolean existsTransformType(EntityManager em, Long orderId);

    boolean addRoomAmountToOrder(EntityManager em, long orderId, long amount);

    boolean subtractAmountFromOrder(EntityManager em, long orderId, double amount);
}
