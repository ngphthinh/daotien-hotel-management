package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.SurchargeDetailDTO;
import iuh.fit.se.group1.entity.SurchargeDetail;

public class SurchargeDetailMapper {

    private OrderMapper orderMapper;
    private SurchargeMapper surchargeMapper;

    public SurchargeDetailMapper() {
        this.orderMapper = new OrderMapper();
        this.surchargeMapper = new SurchargeMapper();
    }

    public SurchargeDetailDTO toDTO(SurchargeDetail surchargeDetail) {

        if (surchargeDetail == null) {
            return null;
        }

        return SurchargeDetailDTO.builder()
                .order(orderMapper.toOrderDTO(surchargeDetail.getOrder()))
                .surcharge(surchargeMapper.toSurchargeDTO(surchargeDetail.getSurcharge()))
                .quantity(surchargeDetail.getQuantity())

                .build();
    }

    public SurchargeDetail toSurchargeDetail(SurchargeDetailDTO surchargeDetailDTO) {
        if (surchargeDetailDTO == null) {
            return null;
        }
        return SurchargeDetail.builder()
                .order(orderMapper.toOrder(surchargeDetailDTO.getOrder()))
                .surcharge(surchargeMapper.toSurcharge(surchargeDetailDTO.getSurcharge()))
                .quantity(surchargeDetailDTO.getQuantity())
                .build();

    }

}
