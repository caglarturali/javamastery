package com.fosposs.common.db.repository;

import com.fosposs.common.db.exception.DatabaseException;

public interface Repository<T, ID> {
    T save(T entity) throws DatabaseException;

    T findById(ID id) throws DatabaseException;

    void delete(ID id) throws DatabaseException;

    boolean exists(ID id) throws DatabaseException;
}
