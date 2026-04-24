package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.interfaces.SurchargeRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SurchargeRepositoryImpl extends AbstractRepositoryImpl<Surcharge, Long> implements SurchargeRepository {
    public SurchargeRepositoryImpl() {
        super(Surcharge.class);
    }

    @Override
    public List<Surcharge> findBySurchargeNameOrId(EntityManager em, String keyword) {

        String jpql = """
                    SELECT s
                    FROM Surcharge s
                    WHERE( LOWER(s.name) LIKE LOWER(:kw)
                         OR CAST(s.surchargeId AS string) LIKE :kw) AND s.isDeleted = false
                    ORDER BY s.surchargeId ASC, s.name ASC
                """;

        return em.createQuery(jpql, Surcharge.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    @Override
    public Surcharge findBySurchargeName(EntityManager em, String name) {

        String jpql = """
                    SELECT s
                    FROM Surcharge s
                    WHERE s.name = :name AND s.isDeleted = false
                """;

        return em.createQuery(jpql, Surcharge.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Surcharge> findAll(EntityManager em) {
        String query = """
                    SELECT s
                    FROM Surcharge s
                    WHERE s.isDeleted = false
                    ORDER BY s.surchargeId ASC, s.name ASC
                """;
        return em.createQuery(query, Surcharge.class).getResultList();

    }
}
