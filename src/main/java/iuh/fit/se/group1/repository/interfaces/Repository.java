package iuh.fit.se.group1.repository.interfaces;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface Repository<T, ID> {
    T save(EntityManager em, T entity);

    T findById(EntityManager em,ID id);

    void deleteById(EntityManager em,ID id);

    List<T> findAll(EntityManager em);

    T update(EntityManager em,T entity);
}
