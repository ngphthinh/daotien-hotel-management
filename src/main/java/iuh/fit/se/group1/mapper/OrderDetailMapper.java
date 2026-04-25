package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.OrderDetailDTO;
import iuh.fit.se.group1.entity.OrderDetail;

public class OrderDetailMapper {

    private final AmenityMapper amenityMapper;

    public OrderDetailMapper() {
        this.amenityMapper = new AmenityMapper();
    }

    public OrderDetailDTO toDTO(OrderDetail orderDetail) {
        if (orderDetail == null) return null;
        return OrderDetailDTO.builder()
                .amenity(amenityMapper.toAmenityDTO(orderDetail.getAmenity()))
                .unitPrice(orderDetail.getUnitPrice())
                .quantity(orderDetail.getQuantity())
                .createdAt(orderDetail.getCreatedAt())
                .build();
    }

    public OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO) {
        if (orderDetailDTO == null) return null;
        return OrderDetail.builder()
                .amenity(amenityMapper.toAmenity(orderDetailDTO.getAmenity()))
                .unitPrice(orderDetailDTO.getUnitPrice())
                .quantity(orderDetailDTO.getQuantity())
                .createdAt(orderDetailDTO.getCreatedAt())
                .build();
    }


}
