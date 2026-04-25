package iuh.fit.se.group1.repository.jpa;

import iuh.fit.se.group1.repository.interfaces.Repository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AbstractRepositoryImpl<T, ID> implements Repository<T, ID> {

    protected final Class<T> entityClass;

    @Override
    public T save(EntityManager em, T o) {
        em.persist(o);
        return o;
    }

    @Override
    public T update(EntityManager em, T o) {
        return em.merge(o);
    }

    @Override

    public T findById(EntityManager em, ID id) {
        return em.find(entityClass, id);
    }

    @Override
    public void deleteById(EntityManager em, ID id) {
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public List<T> findAll(EntityManager em) {
        String query = String.format("SELECT o FROM %s o", entityClass.getSimpleName());
        return em.createQuery(query, entityClass).getResultList();
    }
}
