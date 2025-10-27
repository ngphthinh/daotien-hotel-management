package iuh.fit.se.group1.repository;

import java.util.List;

public interface Repository <T, ID> {
    T save(T entity);
    T findById(ID id);
    void deleteById(ID id);
    List<T> findAll();
    T update(T entity);
}
