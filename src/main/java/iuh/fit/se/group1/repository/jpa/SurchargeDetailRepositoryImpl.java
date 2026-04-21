package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.entity.SurchargeDetail;
import iuh.fit.se.group1.repository.interfaces.SurchargeDetailRepository;

import java.util.List;

public class SurchargeDetailRepositoryImpl extends AbstractRepositoryImpl<SurchargeDetail, SurchargeDetail.SurchargeDetailID> implements SurchargeDetailRepository {


    public SurchargeDetailRepositoryImpl() {
        super(SurchargeDetail.class);
    }

    @Override
    public SurchargeDetail save(SurchargeDetail surchargeDetail, Long orderId) {
        return callInTransaction(em -> {
            Order orderRef = em.getReference(Order.class, orderId);
            surchargeDetail.setOrder(orderRef);
            return em.merge(surchargeDetail);
        });
    }

    @Override
    public List<SurchargeDetail> findSurchargeDetailsByOrderId(Long orderId) {
        return callInTransaction(em ->
                em.createQuery("""
                                    SELECT sd FROM SurchargeDetail sd
                                    JOIN FETCH sd.surcharge
                                    WHERE sd.order.orderId = :orderId
                                """, SurchargeDetail.class)
                        .setParameter("orderId", orderId)
                        .getResultList()
        );
    }

    @Override
    public boolean existsBySurchargeIdAndOrderId(Long surchargeId, Long orderId) {
        return callInTransaction(em ->
                !em.createQuery("""
                                    SELECT sd FROM SurchargeDetail sd
                                    WHERE sd.surcharge.surchargeId = :sid
                                      AND sd.order.orderId = :oid
                                """, SurchargeDetail.class)
                        .setParameter("sid", surchargeId)
                        .setParameter("oid", orderId)
                        .setMaxResults(1)
                        .getResultList()
                        .isEmpty()
        );
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        callInTransaction(em -> {
            em.createQuery("""
                                DELETE FROM SurchargeDetail sd
                                WHERE sd.order.orderId = :orderId
                            """)
                    .setParameter("orderId", orderId)
                    .executeUpdate();
            return null;
        });
    }

    @Override
    public boolean saveByOrderId(Long orderId, List<SurchargeDetail> surchargeDetails) {
        return callInTransaction(em -> {
            Order orderRef = em.getReference(Order.class, orderId);

            for (SurchargeDetail sd : surchargeDetails) {
                sd.setOrder(orderRef);
                em.persist(sd);
            }

            return true;
        });
    }

    @Override
    public void deleteById(long surchargeId, Long orderId) {
        callInTransaction(em -> {
            em.createQuery("""
                                DELETE FROM SurchargeDetail sd
                                WHERE sd.surcharge.surchargeId = :sid
                                  AND sd.order.orderId = :oid
                            """)
                    .setParameter("sid", surchargeId)
                    .setParameter("oid", orderId)
                    .executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteById(Long orderId) {
        callInTransaction(em -> {
            em.createQuery("""
                                DELETE FROM SurchargeDetail sd
                                WHERE sd.order.orderId = :oid
                            """)
                    .setParameter("oid", orderId)
                    .executeUpdate();
            return null;
        });
    }

    @Override
    public void updateSurchargeDetail(Long surchargeId, int quantity, Long orderId) {
        callInTransaction(em -> {
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
            return null;
        });
    }
}
