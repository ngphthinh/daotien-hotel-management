package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.entity.Surcharge;

public class SurchargeMapper {
    public Surcharge toSurcharge(SurchargeDTO surchargeDTO) {
        if (surchargeDTO == null)
            return null;
        return Surcharge.builder()
                .surchargeId(surchargeDTO.getSurchargeId())
                .name(surchargeDTO.getName())
                .price(surchargeDTO.getPrice())
                .build();
    }

    public SurchargeDTO toSurchargeDTO(Surcharge surcharge) {
        if (surcharge == null)
            return null;
        return SurchargeDTO.builder()
                .surchargeId(surcharge.getSurchargeId())
                .name(surcharge.getName())
                .price(surcharge.getPrice())
                .build();
    }
}
