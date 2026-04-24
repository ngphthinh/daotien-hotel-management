package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.repository.interfaces.OrderDetailRepository;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailRepositoryImpl extends AbstractRepositoryImpl<OrderDetail, OrderDetail.OrderDetailId> implements OrderDetailRepository {

    public OrderDetailRepositoryImpl() {
        super(OrderDetail.class);
    }

    @Override
    public boolean save(EntityManager em, Order savedOrder, List<OrderDetail> orderDetails) {
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(savedOrder);
            em.persist(detail);
        }
        return true;
    }

    @Override
    public boolean saveByOrderId(EntityManager em, Long orderId, List<OrderDetail> orderDetails) {
        Order orderRef = em.getReference(Order.class, orderId);

        for (OrderDetail detail : orderDetails) {
            detail.setOrder(orderRef);
            em.persist(detail);
        }
        return true;
    }

    @Override
    public void deleteByOrderId(EntityManager em, Long orderId) {
        em.createQuery("DELETE FROM OrderDetail od WHERE od.order.orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    @Override
    public List<OrderDetail> findByOrderId(EntityManager em, Long orderId) {
        return em.createQuery("""
                            SELECT od
                            FROM OrderDetail od
                            JOIN FETCH od.amenity
                            WHERE od.order.orderId = :orderId
                        """, OrderDetail.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public void deleteById(EntityManager em, Long amenityId, Long orderId) {
        em.createQuery("""
                            DELETE FROM OrderDetail od
                            WHERE od.order.orderId = :orderId
                              AND od.amenity.amenityId = :amenityId
                        """)
                .setParameter("orderId", orderId)
                .setParameter("amenityId", amenityId)
                .executeUpdate();
    }

    @Override
    public OrderDetail save(EntityManager em, Long orderId, OrderDetail newDetail) {
        Order orderRef = em.getReference(Order.class, orderId);
        newDetail.setOrder(orderRef);
        em.persist(newDetail);
        return newDetail;
    }

    @Override
    public void updateOrderDetailFormOrderId(EntityManager em, Long amenityId, BigDecimal unitPrice, int quantity, Long orderId) {
        em.createQuery("""
                            UPDATE OrderDetail od
                            SET od.unitPrice = :price,
                                od.quantity = :quantity
                            WHERE od.order.orderId = :orderId
                              AND od.amenity.amenityId = :amenityId
                        """)
                .setParameter("price", unitPrice)
                .setParameter("quantity", quantity)
                .setParameter("orderId", orderId)
                .setParameter("amenityId", amenityId)
                .executeUpdate();
    }

    public void deleteById(EntityManager em, Long orderId) {
        deleteByOrderId(em, orderId);
    }

}
