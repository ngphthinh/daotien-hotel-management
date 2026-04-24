package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.interfaces.SurchargeDetailRepository;
import jakarta.persistence.EntityManager;

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
