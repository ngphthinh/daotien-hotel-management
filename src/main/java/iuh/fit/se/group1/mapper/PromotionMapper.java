package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.PromotionDTO;
import iuh.fit.se.group1.entity.Promotion;

public class PromotionMapper {
    public PromotionDTO toDTO(Promotion promotion) {
        if (promotion == null)
            return null;
        return PromotionDTO.builder()
                .promotionId(promotion.getPromotionId())
                .promotionName(promotion.getPromotionName())
                .description(promotion.getDescription())
                .discountPercent(promotion.getDiscountPercent())
                .minOrderAmount(promotion.getMinOrderAmount())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .createdAt(promotion.getCreatedAt())
                .build();
    }

    public Promotion toPromotion(PromotionDTO promotionDTO) {

        if (promotionDTO == null) return null;

        return Promotion.builder()
                .promotionId(promotionDTO.getPromotionId())
                .promotionName(promotionDTO.getPromotionName())
                .description(promotionDTO.getDescription())
                .discountPercent(promotionDTO.getDiscountPercent())
                .minOrderAmount(promotionDTO.getMinOrderAmount())
                .startDate(promotionDTO.getStartDate())
                .endDate(promotionDTO.getEndDate())
                .createdAt(promotionDTO.getCreatedAt())
                .build();


    }

}
