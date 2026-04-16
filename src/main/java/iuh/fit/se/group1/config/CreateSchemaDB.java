package iuh.fit.se.group1.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class CreateSchemaDB {
    public static void main(String[] args) {
        EntityManager em = Persistence
                .createEntityManagerFactory("daotien")
                .createEntityManager();

        em.getTransaction().begin();
        em.createQuery("select 1").getSingleResult();
        em.getTransaction().commit();
    }
}
