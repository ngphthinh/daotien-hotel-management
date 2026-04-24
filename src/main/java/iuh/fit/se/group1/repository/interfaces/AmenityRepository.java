package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Amenity;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface AmenityRepository {
    List<Amenity> findByAmenityNameOrId(EntityManager em, String keyword);

    Amenity findByAmenityName(EntityManager em, String nameAmenity);
}
