package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.OrderTypeDTO;
import iuh.fit.se.group1.entity.OrderType;

public class OrderTypeMapper {
    public OrderType toOrderType(OrderTypeDTO orderTypeDTO) {
        if (orderTypeDTO == null) return null;
        return OrderType.builder()
                .orderTypeId(orderTypeDTO.getOrderTypeId())
                .name(orderTypeDTO.getName())
                .build();
    }

    public OrderTypeDTO toOrderTypeDTO(OrderType orderType) {
        if (orderType == null) return null;
        return OrderTypeDTO.builder()
                .orderTypeId(orderType.getOrderTypeId())
                .name(orderType.getName())

                .build();
    }

}
