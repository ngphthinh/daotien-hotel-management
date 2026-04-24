package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Surcharge;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface SurchargeRepository {
    List<Surcharge> findBySurchargeNameOrId(EntityManager em, String keyword);

    Surcharge findBySurchargeName(EntityManager em, String name);
}
