package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.repository.interfaces.OrderDetailRepository;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailRepositoryImpl extends AbstractRepositoryImpl<OrderDetail, OrderDetail.OrderDetailId> implements OrderDetailRepository {

    public OrderDetailRepositoryImpl() {
        super(OrderDetail.class);
    }

    @Override
    public boolean save(Order savedOrder, List<OrderDetail> orderDetails) {
        return callInTransaction(em -> {
            for (OrderDetail detail : orderDetails) {
                detail.setOrder(savedOrder);
                em.persist(detail);
            }
            return true;
        });
    }

    @Override
    public boolean saveByOrderId(Long orderId, List<OrderDetail> orderDetails) {
        return callInTransaction(em -> {
            Order orderRef = em.getReference(Order.class, orderId);

            for (OrderDetail detail : orderDetails) {
                detail.setOrder(orderRef);
                em.persist(detail);
            }
            return true;
        });
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        callInTransaction(em ->
                em.createQuery("DELETE FROM OrderDetail od WHERE od.order.orderId = :orderId")
                        .setParameter("orderId", orderId)
                        .executeUpdate()
        );
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT od
                                    FROM OrderDetail od
                                    JOIN FETCH od.amenity
                                    WHERE od.order.orderId = :orderId
                                """, OrderDetail.class)
                        .setParameter("orderId", orderId)
                        .getResultList()
        );
    }

    @Override
    public void deleteById(Long amenityId, Long orderId) {
        callInTransaction(em ->
                em.createQuery("""
                                    DELETE FROM OrderDetail od
                                    WHERE od.order.orderId = :orderId
                                      AND od.amenity.amenityId = :amenityId
                                """)
                        .setParameter("orderId", orderId)
                        .setParameter("amenityId", amenityId)
                        .executeUpdate()
        );
    }

    @Override
    public OrderDetail save(Long orderId, OrderDetail newDetail) {
        return callInTransaction(em -> {
            Order orderRef = em.getReference(Order.class, orderId);
            newDetail.setOrder(orderRef);
            em.persist(newDetail);
            return newDetail;
        });
    }

    @Override
    public void updateOrderDetailFormOrderId(Long amenityId, BigDecimal unitPrice, int quantity, Long orderId) {
        callInTransaction(em ->
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
                        .executeUpdate()
        );
    }

    public void deleteById(Long orderId) {
        deleteByOrderId(orderId);
    }

}
