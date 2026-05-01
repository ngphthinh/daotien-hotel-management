package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.DenominationDetail;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface DenominationDetailRepository {
    List<DenominationDetail> findByEmployeeShiftId(EntityManager em, Long employeeShiftId);

    void saveBatch(EntityManager em, List<DenominationDetail> details);

    List<Long> findAllDistinctDenominations(EntityManager em);
}
