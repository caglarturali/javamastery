package com.fosposs.common.service.category;

import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.category.CategoryRepository;
import com.fosposs.common.model.Category;
import com.fosposs.common.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;

    public DefaultCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) throws ServiceException {
        try {
            return categoryRepository.save(category);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to create category: " + e.getMessage(), e);
        }
    }

    @Override
    public Category update(Category category) throws ServiceException {
        try {
            // Verify the category exists
            if (!categoryRepository.exists(category.id())) {
                throw new ServiceException("Category not found with id: " + category.id());
            }
            return categoryRepository.save(category);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to update category: " + e.getMessage(), e);
        }
    }

    @Override
    public Category getById(UUID id) throws ServiceException {
        try {
            return categoryRepository.findById(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get category by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getAll() throws ServiceException {
        try {
            return categoryRepository.findAll();
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get all categories: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(UUID id) throws ServiceException {
        try {
            return categoryRepository.delete(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to delete category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> search(String searchTerm) throws ServiceException {
        try {
            return categoryRepository.search(searchTerm);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to search categories: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getRootCategories() throws ServiceException {
        try {
            return categoryRepository.findRootCategories();
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get root categories: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getChildCategories(UUID parentId) throws ServiceException {
        try {
            return categoryRepository.findChildCategories(parentId);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get child categories: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getCategoryPath(UUID id) throws ServiceException {
        try {
            return categoryRepository.getCategoryPath(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get category path: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasChildren(UUID id) throws ServiceException {
        try {
            return categoryRepository.hasChildren(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to check if category has children: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasProducts(UUID id) throws ServiceException {
        try {
            return categoryRepository.hasProducts(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to check if category has products: " + e.getMessage(), e);
        }
    }
}
