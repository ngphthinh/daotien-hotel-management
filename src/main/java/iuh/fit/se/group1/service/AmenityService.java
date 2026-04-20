package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.repository.jpa.AmenityRepositoryImpl;

import java.util.List;

public class AmenityService {
    private final AmenityRepositoryImpl amenityRepositoryImpl;

    public AmenityService() {
        this.amenityRepositoryImpl = new AmenityRepositoryImpl();
    }

    public Amenity createAmenity(Amenity amenity) {

        if (getAmenityByName(amenity.getNameAmenity()) != null) {
            return null;
        }

        return amenityRepositoryImpl.save(amenity);
    }

    private Amenity getAmenityByName(String nameAmenity) {
        return amenityRepositoryImpl.findByAmenityName(nameAmenity);
    }

    public void deleteAmenity(Long amenityId) {
        amenityRepositoryImpl.deleteById(amenityId);
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepositoryImpl.findAll();
    }


    public Amenity updateAmenity(Amenity amenity) {
        return amenityRepositoryImpl.update(amenity);
    }

    public List<Amenity> getAmenityByKeyword(String keyword) {
        return amenityRepositoryImpl.findByAmenityNameOrId(keyword);
    }


    public AmenityDTO toAmenityDTO(Amenity amenity) {
        if (amenity == null) {
            return null;
        }

        return new AmenityDTO(
                amenity.getAmenityId(),
                amenity.getNameAmenity(),
                amenity.getPrice().doubleValue(), 0
        );
    }

    public Amenity getAmenityById(Long amenityId) {
        return amenityRepositoryImpl.findById(amenityId);
    }
}
