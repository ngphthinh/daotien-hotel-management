package iuh.fit.se.group1.service;


import iuh.fit.se.group1.infrastructure.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Function;

public abstract class Service {

    protected <R> R doInTransaction(Function<EntityManager, R> action) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            R result = action.apply(em);

            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    protected void doInTransactionVoid(java.util.function.Consumer<EntityManager> action) {
        doInTransaction(em -> {
            action.accept(em);
            return null;
        });
    }
}