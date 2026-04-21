package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.SurchargeDetail;

import java.util.List;

public interface SurchargeDetailRepository {
    SurchargeDetail save(SurchargeDetail surchargeDetail, Long orderId);

    List<SurchargeDetail> findSurchargeDetailsByOrderId(Long orderId);

    boolean existsBySurchargeIdAndOrderId(Long surchargeId, Long orderId);

    void deleteByOrderId(Long orderId);

    boolean saveByOrderId(Long orderId, List<SurchargeDetail> surchargeDetails);

    void deleteById(long surchargeId, Long orderId);

    void deleteById(Long orderId);

    void updateSurchargeDetail(Long surchargeId, int quantity, Long orderId);
}
