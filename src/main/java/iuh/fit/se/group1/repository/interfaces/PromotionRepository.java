package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Promotion;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionRepository {
    List<Promotion> findByPromotionIdOrName(EntityManager em, String keyword);

    Promotion findAllWithDiscountPriceMax();

    Promotion findAllWithDiscountPercentMax();

    Promotion findByPrice(BigDecimal price);

    Promotion findActivePromotion(EntityManager em,BigDecimal totalAmount);
}
