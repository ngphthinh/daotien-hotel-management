package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Amenity;

import java.util.List;

public interface AmenityRepository {
    List<Amenity> findByAmenityNameOrId(String keyword);

    Amenity findByAmenityName(String nameAmenity);
}
