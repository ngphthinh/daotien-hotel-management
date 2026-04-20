package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.DenominationDetail;

import java.util.List;

public interface DenominationDetailRepository {
    List<DenominationDetail> findByEmployeeShiftId(Long employeeShiftId);

    void saveBatch(List<DenominationDetail> details);

    List<Long> findAllDistinctDenominations();
}
