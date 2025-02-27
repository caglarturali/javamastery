package com.fosposs.common.service;

import com.fosposs.common.service.exception.ServiceException;

import java.util.List;

/**
 * Generic service interface for entity operations
 *
 * @param <T>  Entity type
 * @param <ID> Entity identifier type
 */
public interface Service<T, ID> {

    T create(T entity) throws ServiceException;

    T update(T entity) throws ServiceException;

    T getById(ID id) throws ServiceException;

    List<T> getAll() throws ServiceException;

    boolean delete(ID id) throws ServiceException;

    List<T> search(String searchTerm) throws ServiceException;
}
