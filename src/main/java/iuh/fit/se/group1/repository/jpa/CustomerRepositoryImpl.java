package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.repository.interfaces.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class CustomerRepositoryImpl extends AbstractRepositoryImpl<Customer, Long> implements CustomerRepository {
    public CustomerRepositoryImpl() {
        super(Customer.class);
    }

    @Override
    public List<Customer> findByCustomerNameOrId(EntityManager em, String keyword) {


        String jpql = """
                    SELECT c
                    FROM Customer c
                    WHERE (
                        LOWER(c.fullName) LIKE LOWER(:kw)
                        OR CAST(c.customerId AS string) LIKE :kw
                
                    )AND  c.isDeleted = false
                    ORDER BY c.customerId ASC, c.fullName ASC
                """;

        return em.createQuery(jpql, Customer.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();

    }

    @Override
    public boolean isCitizenIdExists(EntityManager em, String citizenId) {
        return
                em.createQuery(
                                "SELECT 1 FROM Customer e WHERE e.citizenId = :cid AND  e.isDeleted = false",
                                Integer.class
                        )
                        .setParameter("cid", citizenId)
                        .getSingleResult() > 0
                ;
    }

    @Override
    public Customer findByCitizenId(EntityManager em, String citizenId) {


        String jpql = """
                    SELECT c
                    FROM Customer c
                    WHERE c.citizenId = :cid AND c.isDeleted = false
                """;

        return em.createQuery(jpql, Customer.class)
                .setParameter("cid", citizenId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Customer> findAll(EntityManager em) {
        String query = """
               Select c from Customer c
               where c.isDeleted = false
           """;
        return em.createQuery(query, entityClass).getResultList();
    }
}
