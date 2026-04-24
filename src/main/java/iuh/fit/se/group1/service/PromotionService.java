package iuh.fit.se.group1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.repository.jpa.PromotionRepositoryImpl;

public class PromotionService extends Service {
    private final PromotionRepositoryImpl promotionRepositoryImpl;

    public PromotionService() {
        this.promotionRepositoryImpl = new PromotionRepositoryImpl();
    }

    public Promotion createPromotion(Promotion promotion) {
//        return promotionRepositoryImpl.save(promotion);

        promotion.setCreatedAt(LocalDate.now());

        return doInTransaction(entityManager -> promotionRepositoryImpl.save(entityManager, promotion));
    }

    public void deletePromotion(Long promotionId) {
//        promotionRepositoryImpl.deleteById(promotionId);
        doInTransactionVoid(entityManager -> promotionRepositoryImpl.deleteById(entityManager, promotionId));
    }

    public Promotion getPromotionById(Long promotionId) {
//        return promotionRepositoryImpl.findById(promotionId);
        return doInTransaction(entityManager -> promotionRepositoryImpl.findById(entityManager, promotionId));
    }

    public List<Promotion> getAllPromotions() {
//        return promotionRepositoryImpl.findAll();
        return doInTransaction(promotionRepositoryImpl::findAll);
    }

    public Promotion updatePromotion(Promotion promotion) {
//        return promotionRepositoryImpl.update(promotion);
        return doInTransaction(entityManager -> promotionRepositoryImpl.update(entityManager, promotion));
    }

    public List<Promotion> getPromotionByKeyword(String keyword) {
//        return promotionRepositoryImpl.findByPromotionIdOrName(keyword);
        return doInTransaction(entityManager -> promotionRepositoryImpl.findByPromotionIdOrName(entityManager, keyword));
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
//        return promotionRepositoryImpl.findActivePromotion(totalAmount);
        return doInTransaction(entityManager -> promotionRepositoryImpl.findActivePromotion(entityManager, totalAmount));
    }

}
