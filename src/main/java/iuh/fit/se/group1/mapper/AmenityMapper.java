package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Amenity;

public class AmenityMapper {
    public AmenityDTO toAmenityDTO(Amenity amenity) {
        if (amenity == null) {
            return null;
        }

        return AmenityDTO.builder()
                .amenityId(amenity.getAmenityId())
                .nameAmenity(amenity.getNameAmenity())
                .price(amenity.getPrice())
                .build();
    }

    public Amenity toAmenity(AmenityDTO amenityDTO) {
        if (amenityDTO == null) {
            return null;
        }

        return Amenity.builder()
                .amenityId(amenityDTO.getAmenityId())
                .nameAmenity(amenityDTO.getNameAmenity())
                .price(amenityDTO.getPrice())
                .build();
    }
}
