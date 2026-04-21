package iuh.fit.se.group1.service;

import java.math.BigDecimal;
import java.util.List;

import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.repository.jpa.PromotionRepositoryImpl;

public class PromotionService {
    private final PromotionRepositoryImpl promotionRepositoryImpl;

    public PromotionService() {
        this.promotionRepositoryImpl = new PromotionRepositoryImpl();
    }

    public Promotion createPromotion(Promotion promotion) {
        return promotionRepositoryImpl.save(promotion);
    }

    public void deletePromotion(Long promotionId) {
        promotionRepositoryImpl.deleteById(promotionId);
    }

    public Promotion getPromotionById(Long promotionId) {
        return promotionRepositoryImpl.findById(promotionId);
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepositoryImpl.findAll();
    }

    public Promotion updatePromotion(Promotion promotion) {
        return promotionRepositoryImpl.update(promotion);
    }

    public List<Promotion> getPromotionByKeyword(String keyword) {
        return promotionRepositoryImpl.findByPromotionIdOrName(keyword);
    }

//    public Promotion getPromotionByPrice(BigDecimal price) {
//        return promotionRepositoryImpl.findByPrice(price);
//    }


//    public Promotion getPromotionDiscountPriceMax() {
//        return promotionRepositoryImpl.findAllWithDiscountPriceMax();
//    }

//    public Promotion getPromotionDiscountPercentMax(){
//        return promotionRepositoryImpl.findAllWithDiscountPercentMax();
//    }

    public Promotion getActivePromotion(BigDecimal totalAmount) {
        return promotionRepositoryImpl.findActivePromotion(totalAmount);
    }

}
