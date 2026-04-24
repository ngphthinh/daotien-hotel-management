package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.repository.jpa.AmenityRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AmenityService extends Service {
    private final AmenityRepositoryImpl amenityRepositoryImpl;

    public AmenityService() {
        this.amenityRepositoryImpl = new AmenityRepositoryImpl();
    }

    public Amenity createAmenity(Amenity amenity) {

        if (getAmenityByName(amenity.getNameAmenity()) != null) {
            return null;
        }

        return doInTransaction(entityManager -> amenityRepositoryImpl.save(entityManager, amenity));
    }

    private Amenity getAmenityByName(String nameAmenity) {
        return doInTransaction(entityManager -> amenityRepositoryImpl.findByAmenityName(entityManager, nameAmenity));
    }

    public void deleteAmenity(Long amenityId) {
        doInTransactionVoid(entityManager -> amenityRepositoryImpl.deleteById(entityManager, amenityId));
    }

    public List<Amenity> getAllAmenities() {
        return doInTransaction(amenityRepositoryImpl::findAll);
    }


    public Amenity updateAmenity(Amenity amenity) {
        return doInTransaction(entityManager -> amenityRepositoryImpl.update(entityManager, amenity));
    }

    public List<Amenity> getAmenityByKeyword(String keyword) {
        return doInTransaction(entityManager -> amenityRepositoryImpl.findByAmenityNameOrId(entityManager, keyword));
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

    public Amenity getAmenityById(EntityManager em, Long amenityId) {
        return amenityRepositoryImpl.findById(em, amenityId);
    }
}
