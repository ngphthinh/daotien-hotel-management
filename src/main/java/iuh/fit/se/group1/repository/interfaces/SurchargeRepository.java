package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Surcharge;

import java.util.List;

public interface SurchargeRepository {
    List<Surcharge> findBySurchargeNameOrId(String keyword);

    Surcharge findBySurchargeName(String name);
}
