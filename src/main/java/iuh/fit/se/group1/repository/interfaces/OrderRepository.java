package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderRepository {
    void updateTotalAmount(Long orderId, BigDecimal totalAmount);

    void updateDeposit(Long orderId, BigDecimal deposit);

    void updateOrderType(Long orderId, Long newOrderTypeId);

    List<Order> findAllByOrderUnPaid();

    void updateOrderStatusToPaid(Order order);

    List<Order> findUnpaidOrdersByKeyword(String keyword);

    List<Order> findAllOrders();

    List<Order> findOrdersUnPendingByKeyWord(String keyword);

    BigDecimal calculateTotalRevenueBetweenDates(LocalDate from, LocalDate to);

    List<Order> searchOrdersByKeyword(String searchText);

    List<Order> findOrdersByRoomIdAndOrderType(Long roomId, Long orderTypeId);

    Map<String, BigDecimal> getRevenueByRoomType(LocalDate from, LocalDate to);

    Map<String, Integer> getBookingCountByRoomTypeAndDate(LocalDate date);


    boolean addSurchargeToOrder(long orderId, long surchargeAmount);

    boolean existsTransformType(Long orderId);

    boolean addRoomAmountToOrder(long orderId, long amount);

    boolean subtractAmountFromOrder(long orderId, double amount);
}
