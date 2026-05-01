package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.DenominationDetail;
import iuh.fit.se.group1.enums.DenominationLabel;
import iuh.fit.se.group1.repository.interfaces.DenominationDetailRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DenominationDetailRepositoryImpl extends AbstractRepositoryImpl<DenominationDetail, Long> implements DenominationDetailRepository {
    public DenominationDetailRepositoryImpl() {
        super(DenominationDetail.class);
    }

    @Override
    public List<DenominationDetail> findByEmployeeShiftId(EntityManager em,
                                                          Long employeeShiftId) {


        String jpql = """
                    SELECT d
                    FROM DenominationDetail d
                    WHERE d.employeeShift.employeeShiftId = :id
                    ORDER BY d.denominationDetailId
                """;

        return em.createQuery(jpql, DenominationDetail.class)
                .setParameter("id", employeeShiftId)
                .getResultList();

    }

    @Override
    public void saveBatch(EntityManager em, List<DenominationDetail> details) {


        int batchSize = 30;
        int i = 0;

        for (DenominationDetail detail : details) {

            em.persist(detail);

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }

            i++;
        }

    }

    @Override
    public List<Long> findAllDistinctDenominations(EntityManager em) {


        String jpql = """
                    SELECT DISTINCT d.denomination
                    FROM DenominationDetail d
                    ORDER BY d.denomination DESC
                """;

        List<DenominationLabel> result = em.createQuery(jpql, DenominationLabel.class)
                .getResultList();

        return result.stream()
                .map(DenominationLabel::getValue)
                .toList();

    }
}
