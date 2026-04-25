package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.interfaces.SurchargeDetailRepository;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SurchargeDetailRepositoryImpl extends AbstractRepositoryImpl<SurchargeDetail, SurchargeDetail.SurchargeDetailID> implements SurchargeDetailRepository {


    public SurchargeDetailRepositoryImpl() {
        super(SurchargeDetail.class);
    }

    @Override
    public SurchargeDetail save(EntityManager em, SurchargeDetail surchargeDetail, Long orderId) {

        Order orderRef = em.getReference(Order.class, orderId);
        surchargeDetail.setOrder(orderRef);
        return em.merge(surchargeDetail);
    }

    @Override
    public BigDecimal getSurchargeRevenue(EntityManager em, LocalDateTime start, LocalDateTime end) {
        BigDecimal result = (BigDecimal) em.createNativeQuery("""
                                SELECT ISNULL(SUM(s.price * sd.quantity), 0)
                                FROM SurchargeDetail sd
                                JOIN Surcharge s ON sd.surchargerId = s.surchargeId
                                JOIN Orders o ON sd.orderId = o.orderId
                                WHERE o.paymentDate IS NOT NULL
                                AND CAST(o.paymentDate AS DATE) BETWEEN CAST(:start AS DATE) AND CAST(:end AS DATE)
                        """)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<SurchargeDetail> findSurchargeDetailsByOrderId(EntityManager em, Long orderId) {
        return
                em.createQuery("""
                                    SELECT sd FROM SurchargeDetail sd
                                    JOIN FETCH sd.surcharge
                                    WHERE sd.order.orderId = :orderId
                                """, SurchargeDetail.class)
                        .setParameter("orderId", orderId)
                        .getResultList()
                ;
    }

    @Override
    public boolean existsBySurchargeIdAndOrderId(EntityManager em, Long surchargeId, Long orderId) {
        return !em.createQuery("""
                            SELECT sd FROM SurchargeDetail sd
                            WHERE sd.surcharge.surchargeId = :sid
                              AND sd.order.orderId = :oid
                        """, SurchargeDetail.class)
                .setParameter("sid", surchargeId)
                .setParameter("oid", orderId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    @Override
    public void deleteByOrderId(EntityManager em, Long orderId) {
        em.createQuery("""
                            DELETE FROM SurchargeDetail sd
                            WHERE sd.order.orderId = :orderId
                        """)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    @Override
    public boolean saveByOrderId(EntityManager em, Long orderId, List<SurchargeDetail> surchargeDetails) {
        Order orderRef = em.getReference(Order.class, orderId);

        for (SurchargeDetail sd : surchargeDetails) {
            sd.setOrder(orderRef);
            em.persist(sd);
        }

        return true;
    }

    @Override
    public void deleteById(EntityManager em, long surchargeId, Long orderId) {
        em.createQuery("""
                            DELETE FROM SurchargeDetail sd
                            WHERE sd.surcharge.surchargeId = :sid
                              AND sd.order.orderId = :oid
                        """)
                .setParameter("sid", surchargeId)
                .setParameter("oid", orderId)
                .executeUpdate();
    }

    @Override
    public void deleteById(EntityManager em, Long orderId) {
        em.createQuery("""
                            DELETE FROM SurchargeDetail sd
                            WHERE sd.order.orderId = :oid
                        """)
                .setParameter("oid", orderId)
                .executeUpdate();
    }

    @Override
    public void updateSurchargeDetail(EntityManager em, Long surchargeId, int quantity, Long orderId) {
        em.createQuery("""
                            UPDATE SurchargeDetail sd
                            SET sd.quantity = :qty
                            WHERE sd.surcharge.surchargeId = :sid
                              AND sd.order.orderId = :oid
                        """)
                .setParameter("qty", quantity)
                .setParameter("sid", surchargeId)
                .setParameter("oid", orderId)
                .executeUpdate();
    }
}
