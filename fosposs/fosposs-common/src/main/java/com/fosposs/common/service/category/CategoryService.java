package com.fosposs.common.service.category;

import com.fosposs.common.model.Category;
import com.fosposs.common.service.Service;
import com.fosposs.common.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing category operations
 */
public interface CategoryService extends Service<Category, UUID> {

    List<Category> getRootCategories() throws ServiceException;

    List<Category> getChildCategories(UUID parentId) throws ServiceException;

    List<String> getCategoryPath(UUID id) throws ServiceException;

    boolean hasChildren(UUID id) throws ServiceException;

    boolean hasProducts(UUID id) throws ServiceException;
}
