package iuh.fit.se.group1.service;

import java.util.List;

import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.repository.PromotionRepository;

public class PromotionService {
    private final PromotionRepository promotionRepository;
    public PromotionService() {
        this.promotionRepository = new PromotionRepository();
    }
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
    public void deletePromotion(Long promotionId) {
        promotionRepository.deleteById(promotionId);
    }
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    public Promotion updatePromotion(Promotion promotion) {
        return promotionRepository.update(promotion);
    }
    public List<Promotion> getPromotionByKeyword(String keyword) {
        return promotionRepository.findByPromotionIdOrName(keyword);
    }


    public Promotion getPromotionDiscountPriceMax() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotionDiscountPercentMax(){
        return promotionRepository.findAllWithDiscountMax();
    }
}
