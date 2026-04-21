package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.repository.interfaces.PromotionRepository;

import java.math.BigDecimal;
import java.util.List;

public class PromotionRepositoryImpl extends AbstractRepositoryImpl<Promotion, Long> implements PromotionRepository {
    public PromotionRepositoryImpl() {
        super(Promotion.class);
    }

    @Override
    public List<Promotion> findByPromotionIdOrName(String keyword) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT p
                        FROM Promotion p
                        WHERE p.isDeleted = false
                          AND (
                                CAST(p.promotionId AS string) LIKE :kw
                             OR LOWER(p.promotionName) LIKE LOWER(:kw)
                          )
                        ORDER BY p.promotionId ASC, p.promotionName ASC
                    """;

            return em.createQuery(jpql, Promotion.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        });
    }

    @Override
    public Promotion findAllWithDiscountPriceMax() {
        return null;
    }

    @Override
    public Promotion findAllWithDiscountPercentMax() {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT p
                        FROM Promotion p
                        WHERE p.endDate > CURRENT_DATE
                        ORDER BY p.discountPercent DESC
                    """;

            return em.createQuery(jpql, Promotion.class)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public Promotion findByPrice(BigDecimal price) {
        return null;
    }

    @Override
    public Promotion findActivePromotion(BigDecimal totalAmount) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT p
                        FROM Promotion p
                        WHERE p.isDeleted = false
                          AND p.endDate > CURRENT_DATE
                          AND p.minOrderAmount <= :amount
                        ORDER BY p.discountPercent DESC
                    """;

            return em.createQuery(jpql, Promotion.class)
                    .setParameter("amount", totalAmount)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }
}
