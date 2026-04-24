package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.repository.interfaces.AmenityRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AmenityRepositoryImpl extends AbstractRepositoryImpl<Amenity, Long> implements AmenityRepository {
    public AmenityRepositoryImpl() {
        super(Amenity.class);
    }

    @Override
    public List<Amenity> findByAmenityNameOrId(EntityManager em, String keyword) {
        String jpql = """
                    SELECT a
                    FROM Amenity a
                    WHERE LOWER(a.nameAmenity) LIKE LOWER(:keyword)
                       OR CAST(a.amenityId AS string) LIKE :keyword
                    ORDER BY a.amenityId ASC, a.nameAmenity ASC
                """;

        String likeKeyword = "%" + keyword + "%";

        return em.createQuery(jpql, Amenity.class)
                .setParameter("keyword", likeKeyword)
                .getResultList();
    }

    @Override
    public Amenity findByAmenityName(EntityManager em, String nameAmenity) {

        String jpql = """
                    SELECT a
                    FROM Amenity a
                    WHERE a.nameAmenity = :name
                """;

        return em.createQuery(jpql, Amenity.class)
                .setParameter("name", nameAmenity)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


}

