package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.SurchargeDetail;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SurchargeDetailRepository {
    SurchargeDetail save(EntityManager em, SurchargeDetail surchargeDetail, Long orderId);

    List<SurchargeDetail> findSurchargeDetailsByOrderId(EntityManager em,Long orderId);

    boolean existsBySurchargeIdAndOrderId(EntityManager em,Long surchargeId, Long orderId);

    void deleteByOrderId(EntityManager em,Long orderId);

    boolean saveByOrderId(EntityManager em,Long orderId, List<SurchargeDetail> surchargeDetails);

    void deleteById(EntityManager em,long surchargeId, Long orderId);

    void deleteById(EntityManager em,Long orderId);

    void updateSurchargeDetail(EntityManager em,Long surchargeId, int quantity, Long orderId);

    BigDecimal getSurchargeRevenue(EntityManager em, LocalDateTime start, LocalDateTime end);
}
