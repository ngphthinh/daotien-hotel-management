package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.repository.AmenityRepository;

import java.util.List;

public class AmenityService {
    private final AmenityRepository amenityRepository;

    public AmenityService() {
        this.amenityRepository = new AmenityRepository();
    }

    public Amenity createAmenity(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    public void deleteAmenity(Long amenityId) {
        amenityRepository.deleteById(amenityId);
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }


    public Amenity updateAmenity(Amenity amenity) {
        return amenityRepository.update(amenity);
    }


}
