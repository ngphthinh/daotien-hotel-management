package iuh.fit.se.group1.repository.interfaces;

import iuh.fit.se.group1.entity.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findByCustomerNameOrId(String keyword);

    boolean isCitizenIdExists(String citizenId);

    Customer findByCitizenId(String citizenId);
}
