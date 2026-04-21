package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.interfaces.SurchargeRepository;

import java.util.List;

public class SurchargeRepositoryImpl extends AbstractRepositoryImpl<Surcharge, Long> implements SurchargeRepository {
    public SurchargeRepositoryImpl() {
        super(Surcharge.class);
    }

    @Override
    public List<Surcharge> findBySurchargeNameOrId(String keyword) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT s
                        FROM Surcharge s
                        WHERE LOWER(s.name) LIKE LOWER(:kw)
                             OR CAST(s.surchargeId AS string) LIKE :kw
                        ORDER BY s.surchargeId ASC, s.name ASC
                    """;

            return em.createQuery(jpql, Surcharge.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        });
    }

    @Override
    public Surcharge findBySurchargeName(String name) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT s
                        FROM Surcharge s
                        WHERE s.name = :name
                    """;

            return em.createQuery(jpql, Surcharge.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }
}
