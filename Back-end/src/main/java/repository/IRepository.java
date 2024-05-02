package repository;

import domain.Entity;
import server.SrvException;

import java.util.List;

public interface IRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    List<E> findAll();
    void save(E entity) throws SrvException;
    void update(E entity);
    void delete(E entity);
}
