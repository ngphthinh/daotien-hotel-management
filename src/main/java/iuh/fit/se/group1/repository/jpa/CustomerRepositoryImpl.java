package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.repository.interfaces.CustomerRepository;

import java.util.List;

public class CustomerRepositoryImpl extends AbstractRepositoryImpl<Customer, Long> implements CustomerRepository {
    public CustomerRepositoryImpl() {
        super(Customer.class);
    }

    @Override
    public List<Customer> findByCustomerNameOrId(String keyword) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT c
                        FROM Customer c
                        WHERE (
                            LOWER(c.fullName) LIKE LOWER(:kw)
                            OR CAST(c.customerId AS string) LIKE :kw
                        )
                        ORDER BY c.customerId ASC, c.fullName ASC
                    """;

            return em.createQuery(jpql, Customer.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        });
    }

    @Override
    public boolean isCitizenIdExists(String citizenId) {
        return callInTransaction(em ->
                em.createQuery(
                                "SELECT 1 FROM Employee e WHERE e.citizenId = :cid",
                                Integer.class
                        )
                        .setParameter("cid", citizenId)
                        .getSingleResult() > 0
        );
    }

    @Override
    public Customer findByCitizenId(String citizenId) {
        return callInTransaction(em -> {

            String jpql = """
                        SELECT c
                        FROM Customer c
                        WHERE c.citizenId = :cid
                    """;

            return em.createQuery(jpql, Customer.class)
                    .setParameter("cid", citizenId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        });
    }
}
