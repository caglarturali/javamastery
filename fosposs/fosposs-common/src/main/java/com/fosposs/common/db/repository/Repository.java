package com.fosposs.common.db.repository;

import com.fosposs.common.db.exception.DatabaseException;

import java.util.List;

public interface Repository<T, ID> {
    T save(T entity) throws DatabaseException;

    T findById(ID id) throws DatabaseException;

    List<T> findAll() throws DatabaseException;

    boolean delete(ID id) throws DatabaseException;

    boolean exists(ID id) throws DatabaseException;

    List<T> search(String searchTerm) throws DatabaseException;

    void clear() throws DatabaseException;
}
