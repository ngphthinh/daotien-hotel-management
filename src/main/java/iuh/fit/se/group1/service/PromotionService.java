package iuh.fit.se.group1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import iuh.fit.se.group1.dto.PromotionDTO;
import iuh.fit.se.group1.entity.Promotion;
import iuh.fit.se.group1.mapper.PromotionMapper;
import iuh.fit.se.group1.repository.jpa.PromotionRepositoryImpl;

public class PromotionService extends Service {
    private final PromotionRepositoryImpl promotionRepositoryImpl;

    private final PromotionMapper promotionMapper = new PromotionMapper();

    public PromotionService() {
        this.promotionRepositoryImpl = new PromotionRepositoryImpl();
    }

    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
//        return promotionRepositoryImpl.save(promotion);

        Promotion promotion = promotionMapper.toPromotion(promotionDTO);

        promotion.setCreatedAt(LocalDate.now());


        return doInTransaction(entityManager -> promotionMapper.toDTO(promotionRepositoryImpl.save(entityManager, promotion)));
    }

    public void deletePromotion(Long promotionId) {
//        promotionRepositoryImpl.deleteById(promotionId);
        doInTransactionVoid(entityManager -> promotionRepositoryImpl.deleteById(entityManager, promotionId));
    }

    public PromotionDTO getPromotionById(Long promotionId) {
//        return promotionRepositoryImpl.findById(promotionId);
        return doInTransaction(entityManager -> promotionMapper.toDTO(promotionRepositoryImpl.findById(entityManager, promotionId)));
    }

    public List<PromotionDTO> getAllPromotions() {
        return doInTransaction(promotionRepositoryImpl::findAll).stream()
                .map(promotionMapper::toDTO)
                .toList();
    }

    public PromotionDTO updatePromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionMapper.toPromotion(promotionDTO);
//        return promotionRepositoryImpl.update(promotion);
        return doInTransaction(entityManager -> promotionMapper.toDTO(promotionRepositoryImpl.update(entityManager, promotion)));
    }

    public List<PromotionDTO> getPromotionByKeyword(String keyword) {
//        return promotionRepositoryImpl.findByPromotionIdOrName(keyword);
        return doInTransaction(entityManager -> promotionRepositoryImpl.findByPromotionIdOrName(entityManager, keyword)).stream()
                .map(promotionMapper::toDTO)
                .toList();

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

    public PromotionDTO getActivePromotion(BigDecimal totalAmount) {
//        return promotionRepositoryImpl.findActivePromotion(totalAmount);
        return doInTransaction(entityManager -> promotionMapper.toDTO(promotionRepositoryImpl.findActivePromotion(entityManager, totalAmount)));
    }

}
