package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.repository.interfaces.AmenityRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
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
                    WHERE ( LOWER(a.nameAmenity) LIKE LOWER(:keyword)
                       OR CAST(a.amenityId AS string) LIKE :keyword )  and a.isDeleted = false
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
                    WHERE a.nameAmenity = :name and a.isDeleted = false
                """;

        return em.createQuery(jpql, Amenity.class)
                .setParameter("name", nameAmenity)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


    @Override
    public List<Amenity> findAll(EntityManager em) {
        String query = """
                    SELECT a
                    FROM Amenity a
                    WHERE a.isDeleted = false
                    ORDER BY a.amenityId ASC, a.nameAmenity ASC
                """;
        return em.createQuery(query, Amenity.class).getResultList();

    }
    public List<Amenity> saveAll(EntityManager em, List<Amenity> amenityEntities) {
        List<Amenity> result = new ArrayList<>();

        for (Amenity amenity : amenityEntities) {


            Amenity existing = em.createQuery(
                            "SELECT a FROM Amenity a WHERE a.nameAmenity = :name", Amenity.class)
                    .setParameter("name", amenity.getNameAmenity())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existing != null) {

                existing.setPrice(amenity.getPrice());
                result.add(existing);
            } else {
                amenity.setAmenityId(null);
                em.persist(amenity);
                result.add(amenity);
            }
        }

        return result;
    }
}

