package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.repository.interfaces.OrderRepository;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order, Long> implements OrderRepository {
    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public void updateTotalAmount(EntityManager em, Long orderId, BigDecimal totalAmount) {

        em.createQuery("""
                            UPDATE Order o
                            SET o.totalAmount = :total
                            WHERE o.orderId = :id
                        """)
                .setParameter("total", totalAmount)
                .setParameter("id", orderId)
                .executeUpdate()
        ;
    }

    @Override
    public void updateDeposit(EntityManager em, Long orderId, BigDecimal deposit) {
        em.createQuery("""
                            UPDATE Order o
                            SET o.deposit = :deposit
                            WHERE o.orderId = :id
                        """)
                .setParameter("deposit", deposit)
                .setParameter("id", orderId)
                .executeUpdate();
    }

    @Override
    public void updateOrderType(EntityManager em, Long orderId, Long newOrderTypeId) {
        em.createQuery("""
                            UPDATE Order o
                            SET o.orderType.orderTypeId = :typeId
                            WHERE o.orderId = :id
                        """)
                .setParameter("typeId", newOrderTypeId)
                .setParameter("id", orderId)
                .executeUpdate();

    }

    @Override
    public List<Order> findAllByOrderUnPaid(EntityManager em) {
        return
                em.createQuery("""
                                    SELECT DISTINCT o
                                    FROM Order o
                                    JOIN FETCH o.customer c
                                    JOIN FETCH o.bookings b
                                    JOIN FETCH b.room r
                                    WHERE o.orderType.orderTypeId = 2
                                """, Order.class)
                        .getResultList()
                ;
    }

    @Override
    public void updateOrderStatusToPaid(EntityManager em, Order order) {

        Order managed = em.find(Order.class, order.getOrderId());

        managed.setTotalAmount(order.getTotalAmount());
        managed.setPaymentType(order.getPaymentType());
        managed.setPaymentDate(order.getPaymentDate());

        if (order.getPromotion() != null) {
            managed.setPromotion(em.getReference(
                    Promotion.class,
                    order.getPromotion().getPromotionId()
            ));
        } else {
            managed.setPromotion(null);
        }

        managed.setEmployeePayment(
                em.getReference(
                        Employee.class,
                        order.getEmployeePayment().getEmployeeId()
                )
        );

        OrderType paidType = em.getReference(OrderType.class, 1L);
        managed.setOrderType(paidType);

    }

    @Override
    public List<Order> findUnpaidOrdersByKeyword(EntityManager em, String keyword) {
        return em.createQuery("""
                            SELECT DISTINCT o
                            FROM Order o
                            JOIN FETCH o.customer c
                            JOIN FETCH o.bookings b
                            JOIN FETCH b.room r
                            WHERE o.orderType.orderTypeId = 2
                              AND (
                                    LOWER(c.fullName) LIKE LOWER(:kw)
                                 OR c.phone LIKE :kw
                                 OR r.roomNumber LIKE :kw
                              )
                        """, Order.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList()
                ;
    }


    @Override
    public List<Order> findAllOrdersCompleteYet(EntityManager em) {

        return em.createQuery("""
                            SELECT DISTINCT o
                            FROM Order o
                            JOIN FETCH o.orderType ot
                            LEFT JOIN FETCH o.customer c
                            JOIN FETCH o.bookings b
                            JOIN FETCH b.room r
                            JOIN FETCH o.employee e
                            WHERE o.orderType.orderTypeId <> 1
                        """, Order.class)
                .getResultList();
    }

    public List<Order> findAllOrdersWithRelationship(EntityManager em) {

        return em.createQuery("""
                            SELECT DISTINCT o
                            FROM Order o
                            JOIN FETCH o.orderType ot
                            LEFT JOIN FETCH o.customer c
                            JOIN FETCH o.bookings b
                            JOIN FETCH b.room r
                            JOIN FETCH o.employee e
                        """, Order.class)
                .getResultList();
    }


    @Override
    public List<Order> findOrdersUnPendingByKeyWord(EntityManager em, String keyword) {
        return
                em.createQuery("""
                                    SELECT DISTINCT o
                                    FROM Order o
                                    JOIN FETCH o.orderType ot
                                    JOIN FETCH o.customer c
                                    JOIN FETCH o.bookings b
                                    JOIN FETCH b.room r
                                    WHERE o.orderType.orderTypeId <> 1
                                      AND (
                                            LOWER(c.fullName) LIKE LOWER(:kw)
                                         OR c.phone LIKE :kw
                                         OR c.citizenId LIKE :kw
                                         OR r.roomNumber LIKE :kw
                                      )
                                """, Order.class)
                        .setParameter("kw", "%" + keyword + "%")
                        .getResultList()
                ;
    }

    @Override
    public BigDecimal calculateTotalRevenueBetweenDates(EntityManager em, LocalDate from, LocalDate to) {
        BigDecimal rs = em.createQuery("""
                            SELECT SUM(o.totalAmount)
                            FROM Order o
                            WHERE o.orderType.orderTypeId = 1
                              AND o.paymentDate BETWEEN :from AND :to
                        """, BigDecimal.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return rs != null ? rs : BigDecimal.ZERO;

    }

    @Override
    public List<Order> searchOrdersByKeyword(EntityManager em, String searchText) {
        return
                em.createQuery("""
                                    SELECT DISTINCT o
                                    FROM Order o
                                    JOIN FETCH o.orderType ot
                                    LEFT JOIN FETCH o.customer c
                                    JOIN FETCH o.bookings b
                                    JOIN FETCH b.room r
                                    WHERE (
                                            LOWER(c.fullName) LIKE LOWER(:kw)
                                         OR c.phone LIKE :kw
                                         OR c.citizenId LIKE :kw
                                         OR r.roomNumber LIKE :kw
                                         OR CAST(o.orderId AS string) LIKE :kw
                                    )
                                """, Order.class)
                        .setParameter("kw", "%" + searchText + "%")
                        .getResultList();
    }

    @Override
    public List<Order> findOrdersByRoomIdAndOrderType(EntityManager em, Long roomId, Long orderTypeId) {
        return
                em.createQuery("""
                                    SELECT DISTINCT o
                                    FROM Order o
                                    JOIN FETCH o.orderType ot
                                    JOIN FETCH o.bookings b
                                    JOIN FETCH b.room r
                                    WHERE r.roomId = :roomId
                                      AND o.orderType.orderTypeId = :typeId
                                """, Order.class)
                        .setParameter("roomId", roomId)
                        .setParameter("typeId", orderTypeId)
                        .getResultList()
                ;
    }

    @Override
    public Map<String, BigDecimal> getRevenueByRoomType(EntityManager em, LocalDate from, LocalDate to) {
        List<Object[]> rs = em.createQuery("""
                            SELECT rt.name, SUM(o.totalAmount)
                            FROM Order o
                            JOIN o.bookings b
                            JOIN b.room r
                            JOIN r.roomType rt
                            WHERE o.orderType.orderTypeId = 1
                              AND o.paymentDate BETWEEN :from AND :to
                            GROUP BY rt.name
                        """, Object[].class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

        Map<String, BigDecimal> map = new HashMap<>();

        for (Object[] row : rs) {
            map.put((String) row[0],
                    row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO);
        }
        return map;
    }

    @Override
    public Map<String, Integer> getBookingCountByRoomTypeAndDate(EntityManager em, LocalDate date) {
        List<Object[]> rs = em.createQuery("""
                            SELECT rt.name, COUNT(b.bookingId)
                            FROM Booking b
                            JOIN b.room r
                            JOIN r.roomType rt
                            JOIN b.order o
                            WHERE o.orderType.orderTypeId IN (1,2,3)
                              AND o.paymentDate = :date
                            GROUP BY rt.name
                        """, Object[].class)
                .setParameter("date", date)
                .getResultList();

        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : rs) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    @Override
    public boolean addSurchargeToOrder(EntityManager em, long orderId, long surchargeAmount) {

        int updated = em.createQuery("""
                        UPDATE Order o
                        SET o.totalAmount = o.totalAmount + :amount
                        WHERE o.orderId = :orderId
                        """)
                .setParameter("amount", BigDecimal.valueOf(surchargeAmount))
                .setParameter("orderId", orderId)
                .executeUpdate();

        return updated > 0;
    }

    @Override
    public boolean existsTransformType(EntityManager em, Long orderId) {
        Long count = em.createQuery("""
                        SELECT COUNT(b)
                        FROM Booking b
                        WHERE b.order.orderId = :orderId
                        """, Long.class)
                .setParameter("orderId", orderId)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public boolean addRoomAmountToOrder(EntityManager em, long orderId, long amount) {
        int updated = em.createQuery("""
                        UPDATE Order o
                        SET o.totalAmount = o.totalAmount + :amount
                        WHERE o.orderId = :orderId
                        """)
                .setParameter("amount", BigDecimal.valueOf(amount))
                .setParameter("orderId", orderId)
                .executeUpdate();

        return updated > 0;
    }

    @Override
    public Order save(EntityManager em, Order o) {

        Customer customer;

        if (o.getCustomer().getCustomerId() != null) {
            customer = em.find(Customer.class, o.getCustomer().getCustomerId());
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found");
            }
        } else {
            customer = o.getCustomer();
            em.persist(customer);
            em.flush(); // đảm bảo có ID ngay
        }

        Employee employee = em.find(Employee.class, o.getEmployee().getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        o.setCustomer(customer);
        o.setEmployee(employee);

        em.persist(o);

        return o;
    }

    @Override
    public boolean subtractAmountFromOrder(EntityManager em, long orderId, double amount) {
        int updated = em.createQuery("""
                        UPDATE Order o
                        SET o.totalAmount = o.totalAmount - :amount
                        WHERE o.orderId = :orderId
                        """)
                .setParameter("amount", BigDecimal.valueOf(amount))
                .setParameter("orderId", orderId)
                .executeUpdate();

        return updated > 0;
    }

    public boolean existsByEmployeeIdAndCompleteYet(EntityManager entityManager, Long employeeId) {
        Long count = entityManager.createQuery("""
                            SELECT COUNT(o)
                            FROM Order o
                            WHERE o.employee.employeeId = :employeeId and o.orderType.orderTypeId <> 1
                        """, Long.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();

        return count > 0;
    }

    public boolean existsByCustomerIdAndCompleteYet(EntityManager entityManager, Long customerId) {
        Long count = entityManager.createQuery("""
                            SELECT COUNT(o)
                            FROM Order o
                            WHERE o.customer.customerId = :customerId and o.orderType.orderTypeId <> 1
                        """, Long.class)
                .setParameter("customerId", customerId)
                .getSingleResult();

        return count > 0;
    }
}
