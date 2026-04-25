package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.mapper.AccountMapper;
import iuh.fit.se.group1.mapper.AmenityMapper;
import iuh.fit.se.group1.repository.interfaces.AccountRepository;
import iuh.fit.se.group1.repository.jpa.AmenityRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class AmenityService extends Service {
    private final AmenityRepositoryImpl amenityRepositoryImpl;
    private final AmenityMapper amenityMapper;

    public AmenityService() {
        this.amenityMapper = new AmenityMapper();
        this.amenityRepositoryImpl = new AmenityRepositoryImpl();
    }

    public AmenityDTO createAmenity(AmenityDTO amenity) {

        if (getAmenityByName(amenity.getNameAmenity()) != null) {
            return null;
        }

        return doInTransaction(entityManager ->
                amenityMapper.toAmenityDTO(amenityRepositoryImpl.save(entityManager, amenityMapper.toAmenity(amenity)))
        );
    }

    private AmenityDTO getAmenityByName(String nameAmenity) {
        return doInTransaction(entityManager -> amenityMapper.toAmenityDTO(amenityRepositoryImpl.findByAmenityName(entityManager, nameAmenity)));
    }

    public void deleteAmenity(Long amenityId) {
        doInTransactionVoid(entityManager -> amenityRepositoryImpl.deleteById(entityManager, amenityId));
    }

    public List<AmenityDTO> getAllAmenities() {
        return doInTransaction(amenityRepositoryImpl::findAll).stream().map(amenityMapper::toAmenityDTO).collect(Collectors.toList());
    }


    public AmenityDTO updateAmenity(AmenityDTO amenity) {
        return doInTransaction(entityManager -> amenityMapper.toAmenityDTO(amenityRepositoryImpl.update(entityManager, amenityMapper.toAmenity(amenity))));
    }

    public List<AmenityDTO> getAmenityByKeyword(String keyword) {
        return doInTransaction(entityManager -> amenityRepositoryImpl.findByAmenityNameOrId(entityManager, keyword).stream().map(amenityMapper::toAmenityDTO).collect(Collectors.toList()));
    }


    public AmenityDTO getAmenityById(Long amenityId) {
        return doInTransaction(entityManager -> amenityMapper.toAmenityDTO(amenityRepositoryImpl.findById(entityManager, amenityId)));
    }

    public Amenity getAmenityEntityById(EntityManager em, Long amenityId) {
        return amenityRepositoryImpl.findById(em, amenityId);
    }
}
