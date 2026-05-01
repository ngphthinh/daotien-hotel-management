package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Customer;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findByCustomerNameOrId(EntityManager em, String keyword);

    boolean isCitizenIdExists(EntityManager em,String citizenId);

    Customer findByCitizenId(EntityManager em, String citizenId);
}
