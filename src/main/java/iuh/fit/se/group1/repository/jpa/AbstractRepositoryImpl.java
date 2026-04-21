package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.infrastructure.JPAUtil;
import iuh.fit.se.group1.repository.interfaces.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class AbstractRepositoryImpl<T, ID> implements Repository<T, ID> {


    protected final Class<T> entityClass;

    protected void runInTransaction(Consumer<EntityManager> consumer) {
        EntityTransaction entityTransaction = null;

        try (EntityManager entityManager = JPAUtil.getEntityManager()) {

            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            consumer.accept(entityManager);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    protected <R> R callInTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            tx = entityManager.getTransaction();

            tx.begin();
            R rs = function.apply(entityManager);
            tx.commit();

            return rs;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();   // lúc này connection vẫn còn mở
            }
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();   // đóng sau cùng
            }
        }
    }

    @Override
    public T save(T o) {
        return callInTransaction(entityManager -> {
            entityManager.persist(o);
            return o;
        });
    }

    @Override
    public T findById(ID id) {
        return callInTransaction(entityManager -> entityManager.find(entityClass, id));
    }

    @Override
    public void deleteById(ID id) {
        runInTransaction(entityManager -> {
            T entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);
        });
    }

    @Override
    public T update(T o) {
        return callInTransaction(entityManager -> entityManager.merge(o));
    }

    @Override
    public List<T> findAll() {
        String query = String.format("SELECT o FROM %s o", entityClass.getSimpleName());
        return callInTransaction(entityManager -> entityManager.createQuery(query, entityClass).getResultList());
    }
}
